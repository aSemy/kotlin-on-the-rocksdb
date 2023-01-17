import buildsrc.kotr.KLibProcessor
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.konan.target.Family

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

          val targetFamily = this@rocksdb.target?.konanTarget?.family!!

          val dirName = when (targetFamily) {
            Family.LINUX -> "rocksdb-7.8.3-x64-linux-release"
            Family.MINGW -> "rocksdb-7.8.3-x64-mingw-static"
            Family.OSX   -> "rocksdb-7.8.3-x64-osx-release"
            else         -> error("$targetFamily is not supported")
          }

          logger.lifecycle("dirName for $name is $dirName (targetFamily:$targetFamily)")

          includeDirs("$projectDir/src/nativeInterop/external/$dirName/include")
          includeDirs("$projectDir/src/nativeInterop/external/$dirName/lib")

          this.extraOpts += listOf(
            "-libraryPath", "$projectDir/src/nativeInterop/external/$dirName/lib",
//            "-staticLibrary", "librocksdb.a"
          )

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

//        val targetFamily = target.konanTarget.family
//
//        val dirName = when (targetFamily) {
//          Family.LINUX -> "rocksdb-7.8.3-x64-linux-release"
//          Family.MINGW -> "rocksdb-7.8.3-x64-mingw-static"
//          Family.OSX   -> "rocksdb-7.8.3-x64-osx-release"
//          else         -> error("$targetFamily is not supported")
//        }
//
//        fun extFile(path:String) = "$projectDir/src/nativeInterop/external/$dirName/$path"
//
//        linkerOpts(
//          "-L${extFile("lib")}",
//          "$projectDir/src/nativeInterop/external/$dirName/lib/libbz2.a",
//          "$projectDir/src/nativeInterop/external/$dirName/lib/liblz4.a",
//          "$projectDir/src/nativeInterop/external/$dirName/lib/librocksdb.a",
//          "$projectDir/src/nativeInterop/external/$dirName/lib/libsnappy.a",
//          "$projectDir/src/nativeInterop/external/$dirName/lib/libz.a",
//          "$projectDir/src/nativeInterop/external/$dirName/lib/libzstd.a",
//          )
//
//
//        logger.lifecycle("linkerOpts for staticLib:${this@staticLib.name}: $linkerOpts (targetFamily:$targetFamily)")
      }
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

  dependsOn(tasks.matching { it.name == "copyCommonizeCInteropForIde" })
}
