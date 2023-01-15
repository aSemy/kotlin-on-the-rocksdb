rootProject.name = "kotlin-on-the-rocksdb"

include(
  ":modules:rocksdb",
  ":modules:rocksdb-interop",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")


apply(from = "./buildSrc/repositories.settings.gradle.kts")
