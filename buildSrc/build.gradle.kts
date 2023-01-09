import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


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


val buildSrcJvmTarget = "11"

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(buildSrcJvmTarget))
  }
}

tasks.withType<KotlinCompile>().configureEach {
  kotlinOptions {
    jvmTarget = buildSrcJvmTarget
  }
}

kotlinDslPluginOptions {
  jvmTarget.set(buildSrcJvmTarget)
}
