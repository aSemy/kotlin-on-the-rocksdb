import buildsrc.ext.asConsumer
import buildsrc.ext.dropDirectories
import buildsrc.kotr.KLibProcessor
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
  buildsrc.conventions.`kotlin-multiplatform-native-host`
}

// generated wrappers will be written into this dir
val generatedMainSrcDir = layout.projectDirectory.dir("src/nativeMain/kotlinGen")

kotlin {

  targets.withType<KotlinNativeTarget>().configureEach {
    compilations.getByName("main") {
      cinterops {
        val rocksdb by creating rocksdb@{

          defFileProperty.set(file("$projectDir/rocksdb.def"))
        }
      }
    }
    binaries {
      binaries.staticLib()
    }
  }

  sourceSets {
    configureEach {
      languageSettings.optIn("kotlin.ExperimentalStdlibApi")
      languageSettings.optIn("dev.adamko.kotlin.on.the.rocksdb.KotlinOnTheRocksDbInternalApi")
    }

    commonTest {
      dependencies {
        implementation(kotlin("test"))

        // Kotlinx Coroutines
        implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.6.4"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
      }
    }

    nativeMain {
      kotlin {
        srcDir(generatedMainSrcDir)
      }
    }
  }
}


interface Services {
  @get:Inject
  val files: FileSystemOperations
}

val generateRocksDbWrappers by tasks.registering {
  group = project.name
  description = "generate idiomatic-Kotlin wrappers for RocksDB cinterop code"

  outputs.dir(temporaryDir)

  val services = objects.newInstance<Services>()

  val klibFile: Provider<File> = tasks
    .withType<CInteropProcess>()
    .named("cinteropRocksdbLinuxX64")
    .flatMap { it.outputFileProvider }

  inputs.file(klibFile)
  dependsOn(tasks.commonizeCInterop)

  mustRunAfter(tasks.withType<CInteropProcess>())

  doFirst {
    services.files.delete { delete(temporaryDir) }
    temporaryDir.mkdirs()

    val files = KLibProcessor.generateRdbInterop(klibFile.get())

    files.forEach { (name, content) ->
      temporaryDir.resolve("$name.kt").writeText(content)
    }
  }
}


val syncRocksDbWrappers by tasks.registering(Sync::class) {
  group = project.name

  from(generateRocksDbWrappers.map { it.temporaryDir })
  into(generatedMainSrcDir)

//  dependsOn(tasks.matching { it.name == "copyCommonizeCInteropForIde" })
}


val rocksDbSource by configurations.creating<Configuration> {
  asConsumer()
  attributes {
    attribute(Usage.USAGE_ATTRIBUTE, objects.named("rocksdb-src"))
  }
}

dependencies {
  // https://github.com/facebook/rocksdb/archive/refs/tags/v7.9.2.zip
  val rocksDbVersion = "7.9.2"
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
  description = "Copy the Raylib headers into the 'include' directory"
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

  executable("make")
  args(
    "static_lib",   // build the static version
    "libzstd.a",
    "libz.a",
    "libbz2.a",
    "libsnappy.a",
    "liblz4.a",
  )

  environment("MAKECMDGOALS", "release")

  when {
    HostManager.hostIsMac -> environment("MACOSX_DEPLOYMENT_TARGET", "10.13")
    //HostManager.hostIsLinux -> environment("BUILD_SHARED_LIBS", "ON")
  }

  dependsOn(rocksDbPrepareMakeStaticLib)
  inputs.dir(rocksDbPrepareSource.map { it.destinationDir })
  outputs.dir(temporaryDir)
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
