import buildsrc.ext.asConsumer
import buildsrc.ext.dropDirectories
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
  buildsrc.conventions.`kotlin-multiplatform-native-host`
}

// generated wrappers will be written into this dir
val generatedMainSrcDir = layout.projectDirectory.dir("src/nativeMain/kotlinGen")


kotlin {
  targets.withType<KotlinNativeTarget>().configureEach {
    binaries {
      executable {
        entryPoint = "dummy.main"
      }
    }
  }
}

interface Services {
  @get:Inject
  val files: FileSystemOperations
}

interface VcpkgConfig {
  val vcpkgDir: DirectoryProperty
  val vcpkgVersion: Property<String>
  val rocksDbVersion: Property<String>

  val vcpkgCmd: Property<String>

  val targetTriplet: Property<String>
  val targetTripletFile: RegularFileProperty
  val tripletFileAdditions: MapProperty<String, String>

  val userHome: DirectoryProperty
  val konanDir: DirectoryProperty
  val konanGcc: Property<String>
  val konanGpp: Property<String>

  val hostFamily: Property<Family>
}

val config = extensions.create<VcpkgConfig>("vcpkg").apply {

  userHome.set(providers.systemProperty("user.home").flatMap {
    layout.dir(provider { file(it) })
  })

  hostFamily.set(HostManager.host.family)

  konanDir.set(
    providers.environmentVariable("KONAN_DATA_DIR").flatMap { konanDir ->
      layout.dir(provider { file(konanDir) })
    }.orElse(
      userHome.dir(".konan")
    )
  )

  when {
    HostManager.hostIsMingw -> {
      val bin = "dependencies/msys2-mingw-w64-x86_64-2/bin"
      konanGcc.set(konanDir.file("$bin/gcc.exe").map { it.asFile.invariantSeparatorsPath })
      konanGpp.set(konanDir.file("$bin/g++.exe").map { it.asFile.invariantSeparatorsPath })
    }

    HostManager.hostIsLinux -> {
      val bin = "dependencies/x86_64-unknown-linux-gnu-gcc-8.3.0-glibc-2.19-kernel-4.9-2/bin"
      konanGcc.set(
        konanDir.file("$bin/x86_64-unknown-linux-gnu-gcc").map { it.asFile.invariantSeparatorsPath }
      )
      konanGpp.set(
        konanDir.file("$bin/x86_64-unknown-linux-gnu-g++").map { it.asFile.invariantSeparatorsPath }
      )
    }

//    HostManager.hostIsMac   -> {
//      val bin = "dependencies/apple-llvm-20200714-macos-x64-essentials/bin"
//      konanGcc.set(konanDir.file("$bin/clang").map { it.asFile.canonicalPath })
//      konanGpp.set(konanDir.file("$bin/clang-11").map { it.asFile.canonicalPath })
//    }

    else                    -> {
      konanGcc.set("/usr/bin/gcc")
      konanGpp.set("/usr/bin/g++")
    }
  }
}


val rocksDbSource by configurations.creating<Configuration> {
  asConsumer()
  attributes {
    attribute(Usage.USAGE_ATTRIBUTE, objects.named("rocksdb-src"))
  }
}

// https://github.com/facebook/rocksdb/archive/refs/tags/v7.9.2.zip
val rocksDbVersion = "7.9.2"
dependencies {
  rocksDbSource("facebook:rocksdb:$rocksDbVersion@zip")
}


val rocksDbPrepareSource by tasks.registering(Sync::class) {
  group = project.name
  description = "Download & unpack the RocksDB source code"
  from(
    rocksDbSource
      .incoming
      .artifactView { lenient(true) }
      .artifacts
      .resolvedArtifacts
      .map { artifacts -> artifacts.map { zipTree(it.file) } }
  ) {
    // drop the first dir (rocksdb-$version)
    eachFile { relativePath = relativePath.dropDirectories(1) }
    includeEmptyDirs = false
    exclude("**/java/**", "**/.github/", "**/.circleci/", "**/buckifier/", "**/docs/")
  }
  into(temporaryDir)
}


val rocksDbSyncHeaders by tasks.registering(Sync::class) {
  group = project.name
  description = "Copy the RocksDB headers into the 'include' directory"
  from(rocksDbPrepareSource) {
    // flatten directories
    eachFile { path = name }
    includeEmptyDirs = false
    include("**/include/rocksdb/c.h")
  }
  into(layout.projectDirectory.dir("include"))
}

val rocksDbPrepareMakeStaticLib: TaskProvider<Sync> by tasks.registering(Sync::class) {
  group = project.name
  description = "Prepare source code for Make"

  from(rocksDbPrepareSource.map { it.destinationDir })
  into(rocksDbMakeStaticLib.map { it.temporaryDir })
}

val rocksDbMakeStaticLib: TaskProvider<Exec> by tasks.registering(Exec::class) {
  group = project.name
  description = "Use Make to build the RocksDB library from source"

  workingDir(temporaryDir)

  dependsOn(tasks.withType<KotlinCompile>())
  dependsOn(rocksDbPrepareMakeStaticLib)

  inputs.dir(rocksDbPrepareSource.map { it.destinationDir })
  outputs.dir(temporaryDir)

  executable("make")
  args(
    // build the static version
    "static_lib",
    // and all dependencies...
    "libzstd.a",
    "libz.a",
    "libbz2.a",
    "libsnappy.a",
    "liblz4.a",
  )

  environment("MAKECMDGOALS", "release")

  when {
    HostManager.hostIsMac -> environment("MACOSX_DEPLOYMENT_TARGET", "10.13")
  }

  environment("CC", config.konanGcc.get())
  environment("CXX", config.konanGpp.get())
}


val rocksDbSyncStaticLibs: TaskProvider<Sync> by tasks.registering(Sync::class) {
  group = project.name

  from(rocksDbMakeStaticLib.map { it.temporaryDir }) {
    include("*.a")
    // flatten directories
    eachFile { path = name }
  }
  into(layout.projectDirectory.dir("lib"))
}

val rocksDbBuild by tasks.registering {
  group = project.name
  description = "Lifecycle task for building RocksDB static libraries"

  dependsOn(
    rocksDbPrepareSource,
    rocksDbSyncHeaders,
    rocksDbMakeStaticLib,
    rocksDbSyncStaticLibs,
  )
}

tasks.matching { it.name == "prepareKotlinIdeaImport" }.configureEach {
  dependsOn(rocksDbSyncHeaders)
}

val hostName = HostManager.host.name.lowercase()

val rocksDbZip by tasks.registering(Zip::class) {
  group = project.name
  description = "Package the RocksDB libs"

  from(rocksDbSyncHeaders) { into("include") }
  from(rocksDbSyncStaticLibs) { into("lib") }

  into(archiveFileName.map { it.substringBeforeLast(".") })

  archiveBaseName.set("rocksdb")
  archiveVersion.set(rocksDbVersion)
  archiveClassifier.set(hostName)

  destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}
