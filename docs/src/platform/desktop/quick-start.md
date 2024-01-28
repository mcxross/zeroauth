**Table of Contents**

[[toc]]

# Quick Start

In this guide, we'll walk you through integrating zkLogin into your desktop application
using the ZeroAuth SDK on various Desktop platforms. Everything you need to kickstart the process is covered here.

## Prerequisites

This guide assumes you have a basic understanding of desktop development, both natively and JVM-based.
It also assumes you have a conceptual understanding of zkLogin. If you are new to zkLogin, please refer to
the [zkLogin documentation](https://docs.sui.io/build/zk_login).

If you understand the above, you are ready to get started, but before we do, you must have the following:

- **OAuth client credentials (`clientId`)** from an OAuth provider. For this guide, we will use Google, but the process
  is similar
  for other providers. You typically get this from the OAuth provider's developer console.
- **A remote salting service**. For this guide, we will use the default remote salting service provided by
  [Mysten Labs](https://mystenlabs.com/), but the process is similar for other **remote** salting services including
  _standard_ self-hosted
  salting services.
  For this, your **client credentials** must be whitelisted by [Mysten Labs](https://mystenlabs.com/).
- **A remote proving service**. For this guide, we will use the default remote proving service provided by
  [Mysten Labs](https://mystenlabs.com/), but the process is similar for other **remote** proving services including
  _standard_ self-hosted
  proving services. For this, your **client credentials** must be whitelisted by [Mysten Labs](https://mystenlabs.com/).

## Installation

::: warning
ZeroAuth zkLogin Desktop SDK is currently in `beta` stage, and the API is subject to rapid changes until the first
stable release.
Please report any issues you encounter. Currently only snapshot versions are available. Stable versions will be
available soon.
:::

To add ZeroAuth SDK to a desktop project depends on how you are building your project. For example, if you are building
natively or via the JVM. We will cover both!

### JVM

If you are building your desktop application via the JVM, you can install the SDK using Gradle:

:::: code-group
::: code-group-item Gradle (Kotlin DSL)
```kotlin
implementation("xyz.mcxross.zero:zero-jvm:1.0.0-SNAPSHOT")
```
:::
::: code-group-item Gradle
```groovy
implementation 'xyz.mcxross.zero:zero-jvm:1.0.0-SNAPSHOT'
```
:::
::::

## Usage

Usage of ZeroAuth on both native and JVM-based desktop platforms is similar.

### Initialization

Before you can use the SDK, you must configure it with your OAuth client credentials and the endpoints of the remote
salting and proving services.

:::: code-group
::: code-group-item Kotlin
@[code kt](../../main/kotlin/zkLoginRequest.kt)
:::
::: code-group-item Java
@[code java](../../main/java/zkLoginRequest.java)
:::
::::

### Triggering the login process

Once the SDK is initialized, you can trigger the login process by calling the `zkLogin` callable. This will launch the 
default browser and redirect the user to the OAuth provider's login page.

:::: code-group
::: code-group-item Kotlin
@[code kt](../../main/kotlin/zkLoginJvm.kt)
:::
::: code-group-item Java
@[code java](../../main/java/zkLoginJvm.java)
:::
::::

Happy zkLogin-ing!