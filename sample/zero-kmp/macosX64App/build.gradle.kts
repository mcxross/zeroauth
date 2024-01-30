plugins {
  kotlin("multiplatform")
}
group = "xyz.mcxross.zero.zero_kmp.macos"

version = "1.3.1"

repositories {
  mavenLocal()
  mavenCentral()
}

kotlin {

  macosArm64().apply {
    binaries {
      executable {
        entryPoint = "main"
      }
    }
  }

  sourceSets {
    macosMain.dependencies {}
  }
}

