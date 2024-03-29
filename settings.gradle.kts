pluginManagement {
  repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
  }
  plugins {
    kotlin("jvm").version(extra["kotlin.version"] as String)
    kotlin("multiplatform").version(extra["kotlin.version"] as String)
    kotlin("plugin.serialization").version(extra["kotlin.version"] as String)
    id("com.android.library").version(extra["agp.version"] as String)
    id("org.jetbrains.dokka").version(extra["kotlin.version"] as String)
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0" }

rootProject.name = "zeroauth"

include(":lib")

project(":lib").name = "zero"
