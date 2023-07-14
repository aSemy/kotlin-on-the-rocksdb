import buildsrc.kotr.RdbWrapperGenerator
import org.gradle.kotlin.dsl.support.serviceOf
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
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

    val rocksDbVersion: String = libs.versions.rocksDb.get()

    fun rdbTargetDir(targetFamily: Family): String {
      val familyDirName = when (targetFamily) {
        LINUX -> "rocksdb-${rocksDbVersion}-x64-linux-release"
        MINGW -> "rocksdb-${rocksDbVersion}-x64-mingw-static"
        OSX   -> "rocksdb-${rocksDbVersion}-x64-osx-release"
        else  -> error("$targetFamily is not supported")
      }
      return "$projectDir/src/nativeInterop/external/$familyDirName"
    }

    compilations.getByName("main") mainCompilation@{

      val rdbTargetDir = rdbTargetDir(target.konanTarget.family)

      cinterops.create("rocksdb") {

        includeDirs("$rdbTargetDir/include")

        extraOpts += listOf(
          "-libraryPath", "$rdbTargetDir/lib",
//            "-staticLibrary", "librocksdb.a"
        )

//        compilerOpts += listOf(
//          "-I$targetDir/include",
//        )

//          fun extFile(path:String) = "$projectDir/src/nativeInterop/external/$dirName/$path"

//          linkerOpts(
//            "-L${extFile("lib")}",
//            "$projectDir/src/nativeInterop/external/$dirName/lib/libbz2.a",
//            "$projectDir/src/nativeInterop/external/$dirName/lib/liblz4.a",
//            "$projectDir/src/nativeInterop/external/$dirName/lib/librocksdb.a",
//            "$projectDir/src/nativeInterop/external/$dirName/lib/libsnappy.a",
//            "$projectDir/src/nativeInterop/external/$dirName/lib/libz.a",
//            "$projectDir/src/nativeInterop/external/$dirName/lib/libzstd.a",
//          )
      }
    }
    binaries {
      binaries.staticLib {

        val rdbTargetDir = rdbTargetDir(target.konanTarget.family)

        linkerOpts(
          "-L$rdbTargetDir/lib",
//          "-I$targetDir/lib",
//          "$targetDir/libbz2.a",
//          "$targetDir/liblz4.a",
//          "$targetDir/librocksdb.a",
//          "$targetDir/libsnappy.a",
//          "$targetDir/libz.a",
//          "$targetDir/libzstd.a",
        )

//        logger.lifecycle("linkerOpts for staticLib:${this@staticLib.name}: $linkerOpts (targetFamily:$targetFamily)")
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
