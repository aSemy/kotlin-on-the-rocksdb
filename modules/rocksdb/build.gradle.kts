import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  buildsrc.conventions.`kotlin-multiplatform-native`
  kotlin("plugin.serialization")
}

kotlin {

  targets.withType<KotlinNativeTarget>().configureEach {
    binaries {
      binaries.staticLib()
    }
  }

  sourceSets {
    configureEach {
      languageSettings.optIn("kotlin.ExperimentalStdlibApi")
      languageSettings.optIn("dev.adamko.kotlin.on.the.rocksdb.KotlinOnTheRocksDbInternalApi")
    }

    commonMain {
      dependencies {
        implementation(projects.modules.rocksdbInterop)

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
