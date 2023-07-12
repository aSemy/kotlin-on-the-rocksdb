import buildsrc.kotr.KLibProcessor
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.konan.target.Family
import org.gradle.kotlin.dsl.support.*

plugins {
  buildsrc.conventions.`kotlin-multiplatform-native`
}

// generated wrappers will be written into this dir
val generatedMainSrcDir: Directory = layout.projectDirectory.dir("src/nativeMain/kotlinGen")

val rocksDbVersion: String = libs.versions.rocksDb.get()

kotlin {

  targets.withType<KotlinNativeTarget>().configureEach {
    compilations.getByName("main") mainCompilation@{
      cinterops {
        val rocksdb by creating {

          val dirName = when (
            val targetFamily = this@mainCompilation.target.konanTarget.family
          ) {
            Family.LINUX -> "rocksdb-${rocksDbVersion}-x64-linux-release"
            Family.MINGW -> "rocksdb-${rocksDbVersion}-x64-mingw-static"
            Family.OSX   -> "rocksdb-${rocksDbVersion}-x64-osx-release"
            else         -> error("$targetFamily is not supported")
          }

          val targetDir = "$projectDir/src/nativeInterop/external/$dirName"

          includeDirs("$targetDir/include")
//          includeDirs("$projectDir/src/nativeInterop/external/$dirName/lib")

          extraOpts += listOf(
            "-libraryPath", "$targetDir/lib",
//            "-staticLibrary", "librocksdb.a"
          )

//          compilerOpts += listOf(
//            "-I$targetDir/include",
//          )

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
    }
    binaries {
      binaries.staticLib {

        val dirName = when (target.konanTarget.family) {
          Family.LINUX -> "rocksdb-${rocksDbVersion}-x64-linux-release"
          Family.MINGW -> "rocksdb-${rocksDbVersion}-x64-mingw-static"
          Family.OSX   -> "rocksdb-${rocksDbVersion}-x64-osx-release"
          else         -> error("${target.konanTarget.family} is not supported")
        }

        val targetDir = "$projectDir/src/nativeInterop/external/$dirName"

        linkerOpts(
          "-L$targetDir/lib",
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

  outputs.dir(temporaryDir)

  val fs = serviceOf<FileSystemOperations>()

  val klibFile: Provider<File> = tasks
    .withType<CInteropProcess>()
    .named("cinteropRocksdbLinuxX64")
    .flatMap { it.outputFileProvider }

  inputs.file(klibFile)
  dependsOn(tasks.commonizeCInterop)

  mustRunAfter(tasks.withType<CInteropProcess>())

  doFirst {
    fs.delete { delete(temporaryDir) }
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
