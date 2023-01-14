import buildsrc.knparse.KLibProcessor

plugins {
  buildsrc.conventions.`kotlin-multiplatform-native`
  kotlin("plugin.serialization")
}

kotlin {

  targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
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
    }

    commonMain {
      dependencies {
        // Kotlinx Coroutines
        implementation(platform("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.6.4"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

        // Serialization
        implementation(platform("org.jetbrains.kotlinx:kotlinx-serialization-bom:1.4.1"))
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json")
      }
    }

    commonTest {
      dependencies {
        implementation(kotlin("test"))
      }
    }
  }
}

val generateRocksDbWrappers by tasks.registering {
  group = project.name
  description = "generate idiomatic-Kotlin wrappers for RocksDB cinterop code"
  val outFile = projectDir.resolve("src/nativeMain/kotlin/generatedOptions.kt")
  outputs.file(outFile)

  val klibFile: Provider<File> = tasks
      .withType<org.jetbrains.kotlin.gradle.tasks.CInteropProcess>()
      .named("cinteropRocksdbLinuxX64")
      .flatMap { it.outputFileProvider }

  inputs.file(klibFile)

  dependsOn(tasks.withType<org.jetbrains.kotlin.gradle.tasks.CInteropProcess>())

  doLast {
    val gen = KLibProcessor.processFeatureContext(
      klibFile.get()
    )
    outFile.writeText(gen)
  }
}
