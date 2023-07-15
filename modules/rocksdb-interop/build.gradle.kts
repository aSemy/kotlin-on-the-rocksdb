import buildsrc.kotr.RdbWrapperGenerator
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.TestExecutable
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.Family.*

plugins {
  buildsrc.conventions.`kotlin-multiplatform-native`
}

// generated wrappers will be written into this dir
val generatedMainSrcDir: Directory = layout.projectDirectory.dir("src/nativeMain/kotlinGen")


kotlin {

  targets.withType<KotlinNativeTarget>().configureEach {

    val externalBasePath = projectDir.resolve("src/nativeInterop/external")
    val rdbBasePath = externalBasePath.resolve("rocksdb")
    val rdbHeaderDir = rdbBasePath.resolve("include")
    fun rdbLibDir(targetFamily: Family): File {
      val libDir = when (targetFamily) {
        LINUX -> "lib-x64-linux-release"
        MINGW -> "lib-x64-mingw-static"
        OSX   -> "lib-x64-osx-release"
        else  -> error("$targetFamily is not supported")
      }
      return rdbBasePath.resolve(libDir)
    }

    compilations.getByName("main") mainCompilation@{
      val rdbLibDir = rdbLibDir(target.konanTarget.family)
      cinterops.create("rocksdb") {
        includeDirs(rdbHeaderDir)
        extraOpts += listOf(
          "-libraryPath", rdbLibDir.invariantSeparatorsPath,
        )
      }
    }
    binaries {
      binaries.staticLib {
        val rdbLibDir = rdbLibDir(target.konanTarget.family)
        linkerOpts(
          "-I${rdbHeaderDir.invariantSeparatorsPath}",
          "-L${rdbLibDir.invariantSeparatorsPath}",

        "-lrocksdb",
        if (target.konanTarget.family == MINGW) "-lzlib" else "-lz",
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
        if (target.konanTarget.family == MINGW) "-lzlib" else "-lz",
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
      languageSettings.optIn("dev.adamko.kotlin.on.the.rocksdb.KotlinOnTheRocksDbInternalApi")
    }

    commonTest {
      dependencies {
        implementation(kotlin("test"))

        // Kotlinx Coroutines
        implementation(project.dependencies.platform(libs.kotlinx.coroutines.bom))
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.coroutines.test)
      }
    }

    nativeMain {
      kotlin {
        srcDir(generatedMainSrcDir)
      }
    }
  }
}

val generateRocksDbWrappers by tasks.registering {
  group = project.name
  description = "generate idiomatic-Kotlin wrappers for RocksDB cinterop code"

  val generatedMainSrcDir = generatedMainSrcDir
  outputs.dir(generatedMainSrcDir).withPropertyName("generatedMainSrcDir")

  val fs = serviceOf<FileSystemOperations>()

  val klibFile: Provider<File> = tasks
    .withType<CInteropProcess>()
    .named("cinteropRocksdbMacosX64")
    .flatMap { it.outputFileProvider }

  inputs.file(klibFile).withPropertyName("klibFile")

  dependsOn(tasks.commonizeCInterop)
  mustRunAfter(tasks.withType<CInteropProcess>())

  doLast {
    fs.delete { delete(temporaryDir) }
    temporaryDir.mkdirs()

    val klibFileValue = klibFile.get()

    println("klibFileValue: $klibFileValue")

    val files = RdbWrapperGenerator.generateRdbInterop(klibFileValue)

    files.forEach { (name, content) ->
      temporaryDir.resolve("$name.kt").writeText(content)
    }
  }

  doLast {
    fs.sync {
      into(generatedMainSrcDir)
      from(temporaryDir)
    }
  }
}
