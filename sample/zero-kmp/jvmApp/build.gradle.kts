plugins {
  kotlin("jvm")
  application
}

group = "xyz.mcxross.zero.zero_kmp.android"

version = "1.3.1"

repositories {
  mavenLocal()
  mavenCentral()
}

kotlin { jvmToolchain(17) }

dependencies {
  implementation(project(":shared"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
  implementation("xyz.mcxross.zero:zero-jvm:1.0.0-SNAPSHOT")
}

tasks.getByName<Test>("test") { useJUnitPlatform() }

application { mainClass.set("xyz.mcxross.ksui.sample.MainKt") }
