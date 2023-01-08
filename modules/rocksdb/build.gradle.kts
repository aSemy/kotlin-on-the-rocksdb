plugins {
  buildsrc.conventions.`kotlin-multiplatform-native`
  kotlin("plugin.serialization")
}

kotlin {

  targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
    compilations.getByName("main") {
      cinterops {
        val rocksdb by creating {
          includeDirs("$projectDir/src/nativeInterop/libs/rocksdb")
        }

        // maybe download librocksdb.a for each platform, and dynamically embed, so there's no need to install the lib locally?
//        kotlinOptions.freeCompilerArgs = listOf(
//          "-include-binary", "$projectDir/src/nativeInterop/libs/rocksdb/librocksdb.a",
//        )
      }
    }
    binaries {
      binaries.staticLib()
//      executable { entryPoint = "dev.adamko.kotlin.on.the.rocksdb.main" }
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

val rocksDbOptionsGen by tasks.registering {
  group = project.name

  description = "util for helping generate vals/vars for rocksdb option classes"

  val headers = layout.projectDirectory.file("src/nativeInterop/libs/rocksdb/c.h")
  inputs.file(headers)
  outputs.dir(temporaryDir)
  doLast {
    val headersText = headers.asFile.readText()

    val getters = Regex("rocksdb_options_get_([a-zA-Z0-9_]+)").findAll(headersText).map {
      it.groupValues[1]
    }.toSet()

    val setters = Regex("rocksdb_options_set_([a-zA-Z0-9_]+)").findAll(headersText).map {
      it.groupValues[1]
    }.toSet()

    (getters union setters).map { option ->
      if (option in setters && option in getters) {
        "var $option get() = rocksdb_options_get_$option(options); set(value) { rocksdb_options_set_$option(options, value) }"
      } else if (option in getters) {
        "val $option get() = rocksdb_options_get_$option(options)"
      } else {
        "fun $option() = rocksdb_options_set_$option(options)"
      }
    }.sorted().forEach {
      println(it)
    }

    println("// functions")

    Regex("rocksdb_options_([^get|set][a-zA-Z0-9_]+)").findAll(headersText).map {
      it.groupValues[1]
    }.toSet()
      .forEach { option ->
        println("fun $option() = rocksdb_options_$option(options)")
      }
  }
}
