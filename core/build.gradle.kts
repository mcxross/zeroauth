import java.net.URL
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
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

version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
  google()
  maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
}

kotlin {
  androidTarget { publishLibraryVariants("release", "debug") }

  ios()
  iosSimulatorArm64()

  jvm { testRuns.named("test") { executionTask.configure { useJUnitPlatform() } } }

  js {
    moduleName = "@mcxross/zero"
    browser {
      webpackTask(Action {
        output.library = "zero"
      })
    }
    nodejs()
    compilations.all { kotlinOptions.sourceMap = true }
    compilations["main"].packageJson {}
    binaries.executable()
  }

  linuxX64()
  macosArm64()
  macosX64()
  mingwX64()
  tvosArm64()
  watchosArm64()

  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("io.ktor:ktor-client-core:2.3.5")
        implementation("io.ktor:ktor-client-serialization:2.3.5")
        implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
        implementation("io.ktor:ktor-client-json:2.3.5")
        implementation("io.ktor:ktor-client-auth:2.3.5")
        implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
        implementation("com.eygraber:uri-kmp:0.0.15")
      }
    }
    val commonTest by getting { dependencies { implementation(kotlin("test")) } }
    val jvmMain by getting {
      dependencies {
        implementation("io.ktor:ktor-client-cio:2.3.5")
      }
    }
    val jvmTest by getting
    val androidMain by getting {
      dependencies {
        implementation("androidx.appcompat:appcompat:1.6.1")
        implementation("androidx.browser:browser:1.6.0")
        implementation("io.ktor:ktor-client-okhttp:2.3.5")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
      }
    }
    val jsMain by getting { dependencies { implementation("io.ktor:ktor-client-js:2.3.5") } }
    val jsTest by getting
    val iosMain by getting
    val iosSimulatorArm64Main by getting { dependsOn(iosMain) }
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
          URL("https://github.com/mcxross/0Auth/blob/master/core/src/commonMain/kotlin")
        )
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

publishing {
  if (hasProperty("sonatypeUser") && hasProperty("sonatypePass")) {
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
          username = property("sonatypeUser") as String
          password = property("sonatypePass") as String
        }
      }
    }
  }

  publications.withType<MavenPublication> {
    // artifact(javadocJar.get())

    pom {
      name.set("zkLogin SDK")
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
      scm { url.set("https://github.com/mcxross/0Auth") }
    }
  }
}

 /*signing {
     val sonatypeGpgKey = System.getenv("SONATYPE_GPG_KEY")
     val sonatypeGpgKeyPassword = System.getenv("SONATYPE_GPG_KEY_PASSWORD")
     when {
       sonatypeGpgKey == null || sonatypeGpgKeyPassword == null -> useGpgCmd()
       else -> useInMemoryPgpKeys(sonatypeGpgKey, sonatypeGpgKeyPassword)
     }
     sign(publishing.publications)
   }
  */
