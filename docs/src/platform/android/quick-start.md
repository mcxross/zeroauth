**Table of Contents**

[[toc]]

# Quick Start

In this guide, we are going to use the ZeroAuth zkLogin Android SDK to integrate zkLogin into an Android application.
From
installation to usage, this guide will cover everything you need to know to get started.

But before we begin, assumptions are made about your knowledge of **Kotlin** or **Java**, **Android development**, and **zkLogin**.

## Prerequisites

This guide assumes you have a basic understanding of Android development and Kotlin or Java.

It also assumes you have a conceptual understanding of zkLogin. If you are new to zkLogin, please refer to the
[zkLogin documentation](https://docs.sui.io/build/zk_login).

If you understand the above, you are ready to get started, but before we do, you must have the following:

- **OAuth client credentials** from an OAuth provider. For this guide, we will use Google, but the process is similar for
  other providers.
- **A remote salting service**. For this guide, we will use the default remote salting service provided by [Mysten Labs](https://mystenlabs.com/), but the process is
  similar for other **remote** salting services including _standard_ self-hosted salting services. 
  For this, your **client credentials** must be whitelisted by [Mysten Labs](https://mystenlabs.com/).
- **A remote proving service**. For this guide, we will use the default remote proving service provided by [Mysten Labs](https://mystenlabs.com/), but the process is
  similar for other **remote** proving services including _standard_ self-hosted proving services. 
  For this, your **client credentials** must be whitelisted by [Mysten Labs](https://mystenlabs.com/).

## Installation

::: warning
ZeroAuth zkLogin Android SDK is currently in `beta` stage, and the API is subject to rapid changes until the first stable release. 
Please report any issues you encounter. Currently only snapshot versions are available. Stable versions will be available soon.
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
- Debug Flavor: This version is tailored for development and testing phases. It includes additional logging to aid developers 
  in troubleshooting and refining the ZeroAuth integration within their projects. 
:::

## Usage

### Result launcher

In order to receive the result of the zkLogin process, you must register a result launcher in your activity.

:::: code-group
::: code-group-item Kotlin
@[code kt](../../main/kotlin/launcher.kt)
:::
::: code-group-item Java
@[code java](../../main/java/launcher.java)
:::
::::

### Initialize the SDK

The next step is to initialize the SDK. This is done by setting the required configuration parameters with a `ZKLoginRequest` object
and passing it to the `zkLogin` callable.

:::: code-group
::: code-group-item Kotlin
@[code kt](../../main/kotlin/zkLoginRequest.kt)
:::
::: code-group-item Java
@[code java](../../main/java/zkLoginRequest.java)
:::
::::

This object encapsulates a zkLogin request. It facilitates the configuration of an **OAuth provider** and 
the definition of a **salting** or **proving** service. It at least requires an `OpenIDServiceConfiguration` object for the OAuth provider. 
This is finally passed to the `zkLogin` callable to generate an intent which should be used to launch the `ZKLoginManagementActivity` which 
orchestrates the OAuth flow, managing the outcomes adeptly and relaying the result—usually a `ZKLoginResponse` upon success or `null` if failed—back to your calling activity.

There are a few ways to access the `zkLogin` callable depending on your preferred style of coding and the language you are using:

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

The next step is to trigger the zkLogin flow. This is done by launching the `ZKLoginManagementActivity` with the intent generated above:

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