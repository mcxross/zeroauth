plugins {
  id("com.android.application")
  kotlin("android")
}

android {
  namespace = "xyz.mcxross.zero.zero_kmp.android"
  compileSdk = 34
  defaultConfig {
    applicationId = "xyz.mcxross.zero.zero_kmp.android"
    minSdk = 24
    targetSdk = 33
    versionCode = 1
    versionName = "1.0"
    manifestPlaceholders["zeroAuthRedirectScheme"] =
      System.getenv("ZERO_AUTH_REDIRECT_SCHEME") ?: "zeroauth"
  }
  buildFeatures { compose = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.5.8" }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
  signingConfigs {
    this.configureEach {
      storeFile = file("${rootDir}/zero.keystore")
      storePassword = "zeroauth"
      keyAlias = "zero"
      keyPassword = "zeroauth"
    }

    buildTypes {
      getByName("release") { isMinifyEnabled = false }
      getByName("debug") { this.signingConfig = signingConfigs.getByName("debug") }
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions { jvmTarget = "17" }
}

dependencies {
  implementation(project(":shared"))
  implementation("androidx.compose.ui:ui:1.6.1")
  implementation("androidx.compose.ui:ui-tooling:1.6.1")
  implementation("androidx.compose.ui:ui-tooling-preview:1.6.1")
  implementation("androidx.compose.foundation:foundation:1.6.1")
  implementation("androidx.compose.material:material:1.6.1")
  implementation("androidx.compose.material:material-icons-extended:1.6.1")
  implementation("androidx.activity:activity-compose:1.8.2")
  implementation("xyz.mcxross.zero:zero-android-debug:1.0.1-SNAPSHOT")
  implementation("xyz.mcxross.sc:sc-android-debug:1.0-SNAPSHOT")
  implementation("xyz.mcxross.ksui:ksui-android-debug:1.3.2")
}
