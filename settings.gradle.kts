rootProject.name = "kotlin-on-the-rocksdb"

pluginManagement {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)

  repositories {
    mavenCentral()

    // workaround for https://youtrack.jetbrains.com/issue/KT-51379
    exclusiveContent {
      forRepository {
        ivy("https://download.jetbrains.com/kotlin/native/builds") {
          name = "Kotlin Native"
          patternLayout {

            // example download URLs:
            // https://download.jetbrains.com/kotlin/native/builds/releases/1.7.20/linux-x86_64/kotlin-native-prebuilt-linux-x86_64-1.7.20.tar.gz
            // https://download.jetbrains.com/kotlin/native/builds/releases/1.7.20/windows-x86_64/kotlin-native-prebuilt-windows-x86_64-1.7.20.zip
            // https://download.jetbrains.com/kotlin/native/builds/releases/1.7.20/macos-x86_64/kotlin-native-prebuilt-macos-x86_64-1.7.20.tar.gz
            listOf(
              "macos-x86_64",
              "macos-aarch64",
              "osx-x86_64",
              "osx-aarch64",
              "linux-x86_64",
              "windows-x86_64",
            ).forEach { os ->
              listOf("dev", "releases").forEach { stage ->
                artifact("$stage/[revision]/$os/[artifact]-[revision].[ext]")
              }
            }
          }
          metadataSources.artifact()
        }
      }
      filter { includeModuleByRegex(".*", ".*kotlin-native-prebuilt.*") }
    }

    ivy("https://github.com/") {
      name = "GitHub Release"
      patternLayout {
        artifact("[organization]/[module]/archive/[revision].[ext]")
        artifact("[organization]/[module]/archive/refs/tags/[revision].[ext]")
        artifact("[organization]/[module]/archive/refs/tags/v[revision].[ext]")
      }
      metadataSources { artifact() }
    }
  }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

include(
  ":modules:rocksdb",
  ":modules:rocksdb-interop",

  ":externals:lib-rocksdb",
)
