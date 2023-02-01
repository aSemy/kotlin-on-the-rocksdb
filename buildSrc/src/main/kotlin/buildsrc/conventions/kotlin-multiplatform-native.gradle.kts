package buildsrc.conventions

import buildsrc.ext.uppercaseFirstChar
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.*
import org.jetbrains.kotlin.gradle.testing.KotlinTaskTestRun

plugins {
  id("buildsrc.conventions.base")
  kotlin("multiplatform")
}


kotlin {

  // Native targets all extend commonMain and commonTest.
  //
  // common/
  // └── native/
  //     ├── linux/
  //     │   └── linuxX64
  //     ├── windows/
  //     │   └── mingwX64
  //     └── macos/
  //         ├── macosX64
  //         └── macosArm64

  targets {
    linuxX64()
    mingwX64()
    macosX64()
//    macosArm64() // missing kafka-kotlin-native
  }

  @Suppress("UNUSED_VARIABLE")
  sourceSets {
    val commonMain by getting {}
    val commonTest by getting {}

    val nativeMain by creating { dependsOn(commonMain) }
    val nativeTest by creating { dependsOn(commonTest) }

    // Linux
    val linuxMain by creating { dependsOn(nativeMain) }
    val linuxTest by creating { dependsOn(nativeTest) }

    val linuxX64Main by getting { dependsOn(linuxMain) }
    val linuxX64Test by getting { dependsOn(linuxTest) }

    // Windows
    val windowsMain by creating { dependsOn(nativeMain) }
    val windowsTest by creating { dependsOn(nativeTest) }

    val mingwX64Main by getting { dependsOn(windowsMain) }
    val mingwX64Test by getting { dependsOn(windowsTest) }

    // macOS
    val macosMain by creating { dependsOn(nativeMain) }
    val macosTest by creating { dependsOn(nativeTest) }

    val macosX64Main by getting { dependsOn(macosMain) }
    val macosX64Test by getting { dependsOn(macosTest) }

//    val macosArm64Main by getting { dependsOn(macosMain) }
//    val macosArm64Test by getting { dependsOn(macosTest) }
  }
}


// create lifecycle task for each Kotlin Platform, that will run all tests
KotlinPlatformType.values().forEach { kotlinPlatform ->
  val kotlinPlatformName = kotlinPlatform.name.uppercaseFirstChar()

  val testKotlinTargetLifecycleTask = tasks.create("allKotlin${kotlinPlatformName}Tests") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Run all Kotlin/${kotlinPlatformName} tests"
  }

  kotlin.testableTargets.matching {
    it.platformType == kotlinPlatform
  }.configureEach {
    testRuns.configureEach {
      if (this is KotlinTaskTestRun<*, *>) {
        testKotlinTargetLifecycleTask.dependsOn(executionTask)
      }
    }
  }
}
