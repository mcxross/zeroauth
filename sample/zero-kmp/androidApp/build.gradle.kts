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
    manifestPlaceholders["zeroAuthRedirectScheme"] = "xyz.mcxross.zero.zero_kmp.android"
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.2"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(project(":shared"))
  implementation("androidx.compose.ui:ui:1.5.4")
  implementation("androidx.compose.ui:ui-tooling:1.5.4")
  implementation("androidx.compose.ui:ui-tooling-preview:1.5.4")
  implementation("androidx.compose.foundation:foundation:1.5.4")
  implementation("androidx.compose.material:material:1.5.4")
  implementation("androidx.compose.material:material-icons-extended:1.5.4")
  implementation("androidx.activity:activity-compose:1.8.0")
  implementation("xyz.mcxross.zero:zero-android-debug:1.0.0-SNAPSHOT")
}
