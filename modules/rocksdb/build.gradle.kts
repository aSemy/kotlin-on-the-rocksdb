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

        implementation(project.dependencies.platform(libs.kotlinx.coroutines.bom))
        implementation(libs.kotlinx.coroutines.core)

        implementation(project.dependencies.platform(libs.kotlinx.serialization.bom))
        implementation(libs.kotlinx.serialization.json)
      }
    }

    commonTest {
      dependencies {
        implementation(projects.modules.rocksdbInterop)
        implementation(kotlin("test"))
      }
    }
  }
}
