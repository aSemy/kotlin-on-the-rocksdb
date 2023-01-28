rootProject.name = "kotlin-on-the-rocksdb"

include(
  ":modules:rocksdb",
  ":modules:rocksdb-interop",
  ":modules:rocksdb-interop-2",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")


apply(from = "./buildSrc/repositories.settings.gradle.kts")

@Suppress("UnstableApiUsage") // centralised repositories are incubating
dependencyResolutionManagement {
  repositories {
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
