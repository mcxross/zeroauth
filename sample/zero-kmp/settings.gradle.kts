pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
  }
}

dependencyResolutionManagement {
  repositories {
    google()
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
    mavenCentral()
    mavenLocal()
  }
}

rootProject.name = "zero-kmp"

include(":androidApp", ":shared", ":jvmApp")
