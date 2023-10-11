plugins {
  kotlin("multiplatform")
  id("com.android.library")
}

group = "xyz.mcxross.zero"

version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  google()
}

kotlin {

  androidTarget {
    publishLibraryVariants("release", "debug")
  }

  ios()
  iosSimulatorArm64()

  jvm {
    testRuns.named("test") { executionTask.configure { useJUnitPlatform() } }
  }

  js { browser { commonWebpackConfig { cssSupport { enabled.set(true) } } } }

  linuxX64()
  macosArm64()
  macosX64()
  mingwX64()
  tvosArm64()
  watchosArm64()

  sourceSets {
    val commonMain by getting
    val commonTest by getting { dependencies { implementation(kotlin("test")) } }
    val jvmMain by getting
    val jvmTest by getting
    val jsMain by getting
    val jsTest by getting
    val iosMain by getting
    val iosSimulatorArm64Main by getting {
      dependsOn(iosMain)
    }
  }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

android {
  namespace = "mcxross.zero"
  defaultConfig {
    minSdk = 24
    compileSdk = 33
  }
}
