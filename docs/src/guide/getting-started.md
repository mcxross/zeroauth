# Getting Started

ZeroAuth provides SDKs for various platforms. Therefore, installation and usage vary depending on the platform. However,
the general process is as follows:

1. Install the SDK for your platform.
2. Configure the SDK.
3. Trigger the login process.
4. Handle the login response.

Please refer to the platform-specific guides for more information.

## Installation

### Android

> **Note**: ZeroAuth for Android is currently in beta. Please report any issues you encounter.
> Currently only snapshot versions are available. Stable versions will be available soon.
> Expect breaking changes until the first stable release.

Add the following to your `build.gradle` file:

```kotlin
dependencies {
    implementation("xyz.mcxross.zero:zero-android-debug:<version>")
}
```

## Usage

### Android

Create a result launcher:

```kotlin
 val zkLoginGoogleResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        // Handle the login response
      }
    }
```

Initialize the SDK with a `ZKLoginRequest` object:

```kotlin
val zkLoginRequest = ZKLoginRequest(Google(), "xyz.mcxross.zero", "xyz.mcxross.zero.zero_kmp.android")
```

Pass the `ZKLoginRequest` object to the `zkLoginIntent` infix function:

```kotlin
val intent = this@MainActivity zkLoginIntent zkLoginRequest
```

Trigger the login process by calling `launch` on the intent:

```kotlin
zkLoginGoogleResultLauncher.launch(intent)
```

The complete code is as follows using a button click listener:

```kotlin
class MainActivity : AppCompatActivity() {
  private val zkLoginGoogleResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        val data: Intent? = result.data
        // Handle the login response
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val zkLoginRequest = ZKLoginRequest(Google(), "xyz.mcxross.zero", "xyz.mcxross.zero.zero_kmp.android")
    val intent = this@MainActivity zkLoginIntent zkLoginRequest

    findViewById<Button>(R.id.button).setOnClickListener {
      zkLoginGoogleResultLauncher.launch(intent)
    }
  }
}
```

Or using Jetpack Compose:

```kotlin
class MainActivity : AppCompatActivity() {
  private val zkLoginGoogleResultLauncher =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        val data: Intent? = result.data
        // Handle the login response
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val zkLoginRequest = ZKLoginRequest(Google(), "xyz.mcxross.zero", "xyz.mcxross.zero.zero_kmp.android")
      val intent = this@MainActivity zkLoginIntent zkLoginRequest

      Button(onClick = {
        zkLoginGoogleResultLauncher.launch(intent)
      }) {
        Text("Login")
      }
    }
  }
}
```
