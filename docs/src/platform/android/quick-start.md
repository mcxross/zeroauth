**Table of Contents**

[[toc]]

# Quick Start

In this guide, we are going to use the ZeroAuth zkLogin Android SDK to integrate zkLogin into an Android application.
From installation to usage, this guide will cover everything you need to know to get started. 

But before we begin, assumptions are made about your knowledge of **Kotlin** or **Java**, **Android development**, and **zkLogin**.

## Prerequisites

This guide assumes you have a basic understanding of Android development and Kotlin or Java.

It also assumes you have a conceptual understanding of zkLogin, but a deeper understanding is not required. However, if
you are not familiar with zkLogin, you can read more about it in the [zkLogin](/zklogin/) section.

If you understand the above, you are ready to get started, but before we do, you must have the following:

- **OAuth client credentials** from an OAuth provider.  This is required to authenticate your application with the OAuth
  provider. You can obtain these credentials by registering your application with the OAuth provider. For this guide, we will use Google, but the process is similar
  for other providers.
- **A remote salting service**. ZeroAuth zkLogin Android SDK is designed to work with remote salting services. It
  defaults to the [Mysten Labs](https://mystenlabs.com/) salting service if not specified, but it can be configured to work with other
  salting services. For this, your **client credentials** must be whitelisted by Mysten Labs.
- **A remote proving service**.  Just like the salting service, ZeroAuth zkLogin Android SDK is designed to work with remote proving services. 
It defaults to the [Mysten Labs](https://mystenlabs.com/) proving service if not specified, but it can be configured to work with other proving services.
For this, your **client credentials** must be whitelisted by Mysten Labs.

### Google

<br />
<details>
  <summary>Register (d)app</summary>

In your browser, navigate to the [Google Cloud Console](https://console.cloud.google.com/). If you are not already signed in, sign in with your Google account:

1. Click on the project drop-down and select or create the project you want to use for your application. 
2. Under the API & Services section, click on the Credentials tab. If you have not already created any credentials, click on the Create credentials button 
and select OAuth client ID of type Android.
3. Fill in the form with the required information. You will need to provide the package name of your application and the SHA-1 fingerprint of the signing certificate.

::: tip
To get the SHA-1 fingerprint of your signing certificate, you can use the following command:
```bash
keytool -list -v -keystore zero.keystore
```
:::

<img src="/android-app-details.png">

4. Enable Custom URI scheme and add the scheme to the OAuth client ID. The scheme is used to redirect the user back to your application after they 
have authenticated with Google.

<img src="/android-custom-scheme.png">

The created OAuth client ID should look like this:

**PREFIX.apps.googleusercontent.com,** where PREFIX is an alphanumeric string unique to your client ID.

</details>

## Installation

::: warning
ZeroAuth zkLogin Android SDK is currently in `beta` stage, and the API is subject to rapid changes until the first
stable release.
Please report any issues you encounter. Currently only snapshot versions are available. Stable versions will be
available soon.
:::

To add ZeroAuth SDK to an Android project, you must add a dependency to your `build.gradle` as follows:

```kotlin
dependencies {
    implementation("xyz.mcxross.zero:zero-android:<version>")
}
```

::: tip
The ZeroAuth Android SDK offers two library flavors to cater for different development needs:

- Release Flavor: This flavor is optimized for production environments.
- Debug Flavor: This version is tailored for development and testing phases. It includes additional logging to aid
  developers
  in troubleshooting and refining the ZeroAuth integration within their projects.
  :::

## Configuration

To use the ZeroAuth Android SDK, you must add the following configuration to your `AndroidManifest.xml` file:

```xml
<application>
    <activity android:name="xyz.mcxross.zero.oauth.RedirectUriReceiverActivity">
        <intent-filter>
            <action android:name="android.intent.action.VIEW" />
            <category android:name="android.intent.category.DEFAULT" />
            <category android:name="android.intent.category.BROWSABLE" />
            <data android:scheme="<place-your-scheme>"
                  android:path="/oauth2redirect"
            />
        </intent-filter>
    </activity>
</application>
```

This configuration is required to handle the result of the zkLogin process.

In your app's `build.gradle` file, you must also add the following configuration to enable the use of the `zklogin` scheme:

```kotlin
android {
    defaultConfig {
        manifestPlaceholders = [
            zeroAuthRedirectScheme: "<place-your-scheme>/<path>"
        ]
    }
}
```

## Usage

### Result launcher

In order to receive the result of the zkLogin process, you must register a result launcher in your activity. 
This is where the result of the zkLogin process will be delivered, typically a `ZKLoginResponse` object.

:::: code-group
::: code-group-item Kotlin
@[code kt](../../main/kotlin/launcher.kt)
:::
::: code-group-item Java
@[code java](../../main/java/launcher.java)
:::
::::

### Initialize the SDK

The next step is to initialize the SDK. This is done by setting the required configuration parameters with
a `ZKLoginRequest` object
and passing it to the `zkLogin` callable.

:::: code-group
::: code-group-item Kotlin
@[code kt](../../main/kotlin/zkLoginRequest.kt)
:::
::: code-group-item Java
@[code java](../../main/java/zkLoginRequest.java)
:::
::::

::: tip
The `redirectUri` must match the one placed in the `AndroidManifest.xml` file.
:::

This object encapsulates a zkLogin request. It facilitates the configuration of an **OAuth provider** and
the definition of a **salting** or **proving** service. It at least requires an `OpenIDServiceConfiguration` object for
the OAuth provider.
This is finally passed to the `zkLogin` callable to generate an intent which should be used to launch
the `ZKLoginManagementActivity` which
orchestrates the OAuth flow, managing the outcomes adeptly and relaying the result—usually a `ZKLoginResponse` upon
success or `null` if failed—back to your calling activity.

There are a few ways to access the `zkLogin` callable depending on your preferred style of coding and the language you
are using:

You can then either use the `zkLogin` infix extension function available on the `Context` class of your activity:

@[code kt](../../main/kotlin/intent.kt)

**OR**:

Pass it to a top level function:

@[code kt](../../main/kotlin/intent.kt)

**OR**:

Create a `DefaultZKLoginService` instance and call the `zkLogin` method:
:::: code-group
::: code-group-item Kotlin
@[code kt](../../main/kotlin/zkLoginService.kt)
:::
::: code-group-item Java
@[code java](../../main/java/zkLoginService.java)
:::
::::

### Trigger zkLogin flow

The next step is to trigger the zkLogin flow. This is done by launching the `ZKLoginManagementActivity` with the intent
generated above:

:::: code-group
::: code-group-item Kotlin
@[code kt](../../main/kotlin/launch.kt)
:::
::: code-group-item Java
@[code java](../../main/java/launch.java)
:::
::::

The complete code is as follows using a button click listener:
:::: code-group
::: code-group-item Kotlin
@[code kt{2-8,14,16,19}](../../main/kotlin/mainActivity.kt)
:::
::: code-group-item Java
@[code java{3-15,22-23,25-26,32}](../../main/java/mainActivity.java)
:::
::::

Or using Jetpack Compose:

:::: code-group
::: code-group-item Kotlin
@[code kt{2-8,13,15,18}](../../main/kotlin/mainActivityCompose.kt)
:::
::::

## Next Steps

Congratulations! You have successfully integrated ZeroAuth zkLogin Android SDK into your Android application. You can now use zkLogin to authenticate 
users and do transactions.

You can find a demo application in the [ZeroAuth repo](https://github.com/mcxross/zeroauth/tree/master/sample/zero-kmp/androidApp)

