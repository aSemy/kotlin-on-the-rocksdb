import buildsrc.kotr.KLibProcessor
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess

plugins {
  buildsrc.conventions.`kotlin-multiplatform-native`
}

// generated wrappers will be written into this dir
val generatedMainSrcDir = layout.projectDirectory.dir("src/nativeMain/kotlinGen")

kotlin {

  targets.withType<KotlinNativeTarget>().configureEach {
    compilations.getByName("main") {
      cinterops {
        val rocksdb by creating rocksdb@{
          includeDirs("$projectDir/src/nativeInterop/external/rocksdb/include")

//          if (target?.konanTarget?.family == org.jetbrains.kotlin.konan.target.Family.MINGW) {
//            //            val msys2root = File(System.getenv("MSYS2_ROOT") ?: "C:/msys2/msys64/mingw64/lib/")
//            fun lib(lib: String) = "C:/msys2/msys64/mingw64/lib/lib${lib}.a"
//            linkerOpts(
//              lib("rocksdb"),
//              lib("zstd"),
//              lib("z"),
//              lib("snappy"),
//              lib("lz4"),
//            )
//            logger.lifecycle("linkerOpts for ${this@rocksdb.name}: $linkerOpts")
//          }
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
}
