package buildsrc.ext

import java.lang.System.getProperty
import org.gradle.api.GradleException
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests


fun KotlinMultiplatformExtension.nativeTarget(
  targetOs: HostOs = HostOs.current,
  configure: KotlinNativeTargetWithHostTests.() -> Unit = { },
) {
  when (targetOs) {
    HostOs.WINDOWS -> mingwX64("native", configure)
    HostOs.MAC     -> macosX64("native", configure)
    HostOs.LINUX   -> linuxX64("native", configure)
  }
}

enum class HostOs {
  WINDOWS, MAC, LINUX;

  companion object {
    val current: HostOs
      get() {
        val hostOs = getProperty("os.name")

        return when {
          "Mac OS X" in hostOs -> MAC
          "Linux" in hostOs    -> LINUX
          "Windows" in hostOs  -> WINDOWS
          else                 -> throw GradleException("unknown host OS '$hostOs'")
        }
      }
  }
}
