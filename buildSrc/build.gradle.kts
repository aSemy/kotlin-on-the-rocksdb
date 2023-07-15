plugins {
  `kotlin-dsl`
}

dependencies {
  val kotlinVer = libs.versions.kotlin.get()
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVer")
  implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVer")

  implementation("org.jetbrains.kotlin:kotlin-tooling-metadata:$kotlinVer")
  implementation("org.jetbrains.kotlin:kotlin-util-klib-metadata:$kotlinVer")

  implementation(libs.kotlinx.metadataKlib)
}
