package buildsrc.conventions

import buildsrc.ext.nativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.*

plugins {
  id("buildsrc.conventions.base")
  kotlin("multiplatform")
}

// only enable the Kotlin Native target compatible with the current host OS

kotlin {

  nativeTarget()

  @Suppress("UNUSED_VARIABLE")
  sourceSets {
    val commonMain by getting {}
    val commonTest by getting {}

    val nativeMain by getting { dependsOn(commonMain) }
    val nativeTest by getting { dependsOn(commonTest) }
  }
}
