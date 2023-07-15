import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable
import org.jetbrains.kotlin.konan.target.Family

plugins {
  buildsrc.conventions.`kotlin-multiplatform-native`
  kotlin("plugin.serialization")
}

kotlin {

  targets.withType<KotlinNativeTarget>().configureEach {

    val externalBasePath = rootDir.resolve("modules/rocksdb-interop/src/nativeInterop/external/")
    val rdbBasePath = externalBasePath.resolve("rocksdb")
    val rdbHeaderDir = rdbBasePath.resolve("include")
    fun rdbLibDir(targetFamily: Family): File {
      val libDir = when (targetFamily) {
        Family.LINUX -> "lib-x64-linux-release"
        Family.MINGW -> "lib-x64-mingw-static"
        Family.OSX   -> "lib-x64-osx-release"
        else         -> error("$targetFamily is not supported")
      }
      return rdbBasePath.resolve(libDir)
    }

    binaries {
      binaries.staticLib {
        val rdbLibDir = rdbLibDir(target.konanTarget.family)
        linkerOpts(
          "-I${rdbHeaderDir.invariantSeparatorsPath}",
          "-L${rdbLibDir.invariantSeparatorsPath}",

          "-lrocksdb",
          if (target.konanTarget.family == Family.MINGW) "-lzlib" else "-lz",
          "-lbz2",
          "-llz4",
          "-lsnappy",
          "-lzstd",
        )
      }
      binaries.withType<TestExecutable>().configureEach {
        val rdbLibDir = rdbLibDir(target.konanTarget.family)
        linkerOpts(
          //"-I${rdbHeaderDir.invariantSeparatorsPath}",
          "-L${rdbLibDir.invariantSeparatorsPath}",

          "-lrocksdb",
          if (target.konanTarget.family == Family.MINGW) "-lzlib" else "-lz",
          "-lbz2",
          "-llz4",
          "-lsnappy",
          "-lzstd",
        )
      }
    }
  }

  sourceSets {
    configureEach {
      languageSettings.optIn("kotlin.ExperimentalStdlibApi")
      languageSettings.optIn("kotlinx.cinterop.ExperimentalForeignApi")
      languageSettings.optIn("kotlin.experimental.ExperimentalNativeApi")
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
        implementation(kotlin("test"))
        implementation(libs.kotlinx.coroutines.test)
      }
    }
  }
}
