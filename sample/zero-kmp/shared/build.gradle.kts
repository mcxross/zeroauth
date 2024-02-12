plugins {
  kotlin("multiplatform")
  id("com.android.library")
}

repositories {
  mavenCentral()
  mavenLocal()
  google()
  maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
  targetHierarchy.default()

  androidTarget { compilations.all { kotlinOptions { jvmTarget = "17" } } }

  jvm { testRuns["test"].executionTask.configure { useJUnitPlatform() } }

  listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
    it.binaries.framework { baseName = "shared" }
  }

  sourceSets {
    val commonMain by getting { dependencies { implementation("xyz.mcxross.ksui:ksui:1.3.2") } }
    val commonTest by getting { dependencies { implementation(kotlin("test")) } }
  }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

android {
  namespace = "xyz.mcxross.zero.zero_kmp"
  compileSdk = 33
  defaultConfig { minSdk = 24 }
}
