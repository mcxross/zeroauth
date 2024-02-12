import java.net.URL
import java.util.*
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.signing
import org.jetbrains.dokka.gradle.DokkaTask

plugins {
  kotlin("multiplatform")
  id("com.android.library")
  kotlin("plugin.serialization")
  id("org.jetbrains.dokka") version "1.9.0"
  id("maven-publish")
  id("signing")
}

group = "xyz.mcxross.zero"

version = "1.1.0-SNAPSHOT"

repositories {
  mavenCentral()
  mavenLocal()
  google()
  maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
}

ext["signing.keyId"] = null

ext["signing.password"] = null

ext["signing.secretKeyRingFile"] = null

ext["sonatypeUser"] = null

ext["sonatypePass"] = null

kotlin {
  androidTarget { publishLibraryVariants("release", "debug") }

  ios()
  iosSimulatorArm64()

  jvm { testRuns.named("test") { executionTask.configure { useJUnitPlatform() } } }

  js {
    moduleName = "@mcxross/zero"
    browser {
      webpackTask(Action { output.library = "zero" })
      @OptIn(org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl::class)
      distribution(
          Action {
            outputDirectory.set(File(rootProject.buildDir, "js/packages/@mcxross/zero/dist"))
          })
    }
    nodejs()
    compilations.all { kotlinOptions.sourceMap = true }
    compilations["main"].packageJson {}
    binaries.executable()
    generateTypeScriptDefinitions()
  }

  linuxX64()
  macosArm64()
  macosX64()
  mingwX64()
  tvosArm64()
  watchosArm64()

  applyDefaultHierarchyTemplate()

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.serialization)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.client.json)
        implementation(libs.ktor.client.auth)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ksui)
        implementation(libs.uri.kmp)
        implementation(libs.sc)
      }
    }
    val commonTest by getting { dependencies { implementation(kotlin("test")) } }
    val jvmMain by getting {
      dependencies {
        implementation(libs.ktor.client.cio)
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.netty)
        implementation("ch.qos.logback:logback-classic:1.2.3")
      }
    }
    val jvmTest by getting

    val androidMain by getting {
      dependencies {
        implementation(libs.appcompat)
        implementation(libs.browser)
        implementation(libs.ktor.client.okhttp)
        implementation(libs.lifecycle.runtime.ktx)
        implementation(libs.kotlinx.coroutines.android)
        implementation(libs.activity.ktx)
      }
    }
    val jsMain by getting {
      dependencies {
        implementation(libs.ktor.client.js)
        implementation(npm("@mysten/zklogin", "0.3.6"))
        implementation(npm("@mysten/sui.js", "0.38.0"))
      }
    }
    val jsTest by getting
    val iosMain by getting
    val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
    macosMain.dependencies {
      implementation(libs.ktor.server.core)
      implementation(libs.ktor.server.cio)
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
  sourceSets {
    named("main") {
      manifest.srcFile("src/androidMain/AndroidManifest.xml")
      res.srcDirs("src/androidMain/res")
    }
  }
}

tasks.getByName<DokkaTask>("dokkaHtml") {
  moduleName.set("ZeroAuth")
  outputDirectory.set(file(buildDir.resolve("dokka")))
  dokkaSourceSets {
    configureEach {
      includes.from("Module.md")
      sourceLink {
        localDirectory.set(file("commonMain/kotlin"))
        remoteUrl.set(
            URL("https://github.com/mcxross/zeroauth/blob/master/core/src/commonMain/kotlin"))
        remoteLineSuffix.set("#L")
      }
    }
  }
}

val javadocJar =
    tasks.register<Jar>("javadocJar") {
      archiveClassifier.set("javadoc")
      dependsOn("dokkaHtml")
      from(buildDir.resolve("dokka"))
    }

val secretPropsFile = project.rootProject.file("local.properties")

if (secretPropsFile.exists()) {
  secretPropsFile
      .reader()
      .use { Properties().apply { load(it) } }
      .onEach { (name, value) -> ext[name.toString()] = value }
} else {
  ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
  ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
  ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_IN_MEMORY_SECRET_KEY")
  ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
  ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
  repositories {
    maven {
      name = "sonatype"
      val isSnapshot = version.toString().endsWith("-SNAPSHOT")
      setUrl(
          if (isSnapshot) {
            "https://s01.oss.sonatype.org/content/repositories/snapshots/"
          } else {
            "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
          },
      )
      credentials {
        username = getExtraString("sonatypeUser")
        password = getExtraString("sonatypePass")
      }
    }
  }

  publications.withType<MavenPublication> {
    // artifact(javadocJar.get())

    pom {
      name.set("ZeroAuth")
      description.set("A Kotlin Multiplatform SDK for the zkLogin.")
      url.set("https://github.com/mcxross")

      licenses {
        license {
          name.set("Apache License, Version 2.0")
          url.set("https://opensource.org/licenses/APACHE-2.0")
        }
      }
      developers {
        developer {
          id.set("mcxross")
          name.set("Mcxross")
          email.set("oss@mcxross.xyz")
        }
      }
      scm { url.set("https://github.com/mcxross/zeroauth") }
    }
  }
}

signing {
  // sign(publishing.publications)
}
