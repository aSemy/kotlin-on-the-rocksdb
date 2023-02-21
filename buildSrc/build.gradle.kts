plugins {
  `kotlin-dsl`
}

dependencies {
  val kotlinVer = "1.8.0"
  implementation(platform(kotlin("bom", kotlinVer)))
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVer")
  implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVer")

  implementation("org.jetbrains.kotlin:kotlin-tooling-metadata:$kotlinVer")
  implementation("org.jetbrains.kotlin:kotlin-util-klib-metadata:$kotlinVer")

  implementation("org.jetbrains.kotlinx:kotlinx-metadata-klib:0.0.3")
}


kotlin {
  jvmToolchain(11)
}
