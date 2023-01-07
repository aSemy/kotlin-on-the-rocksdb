import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
  `kotlin-dsl`
}

dependencies {
  val kotlinVer = "1.8.0"
  implementation(platform(kotlin("bom", kotlinVer)))
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVer")
  implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVer")
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
