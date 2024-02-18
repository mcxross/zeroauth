**Table of Contents**

[[toc]]

# Quick Start

In this guide, we'll walk you through integrating zkLogin into your iOS application using the ZeroAuth SDK. 
Everything you need to kickstart the process is covered here.

## Prerequisites

This guide assumes you have a basic understanding of iOS development. It also assumes you have a conceptual understanding of zkLogin.
If you are new to zkLogin, please refer to the [zkLogin documentation](https://docs.sui.io/build/zk_login).

If you understand the above, you are ready to get started, but before we do, you must have the following:
- **OAuth client credentials (`clientId`)** from an OAuth provider. For this guide, we will use Google, but the process is similar for other providers. 
You typically get this from the OAuth provider's developer console.
- **A remote salting service**. For this guide, we will use the default remote salting service provided by [Mysten Labs](https://mystenlabs.com/),
but the process is similar for other **remote** salting services including _standard_ self-hosted salting services.
For this, your **client credentials** must be whitelisted by [Mysten Labs](https://mystenlabs.com/).
- **A remote proving service**. For this guide, we will use the default remote proving service provided by [Mysten Labs](https://mystenlabs.com/),
but the process is similar for other **remote** proving services including _standard_ self-hosted proving services.
For this, your **client credentials** must be whitelisted by [Mysten Labs](https://mystenlabs.com/).

## Installation

To add ZeroAuth SDK to an iOS project, you can use Swift Package Manager as follows:

```swift
dependencies: [
    .package(url: "https://github.com/mcxross/zeroauth-swift.git", .upToNextMajor(from: "1.0.0-SNAPSHOT"))
]
```

## Usage

Once you have the ZeroAuth SDK installed, you can start using it in your iOS application.

In your `struct`, add an observer to listen for the zkLogin response:

```swift
@ObservedObject private var model: ZKLoginModel
```

The model will be used to initiate the zkLogin request:

```swift
let google = ZKLoginRequest(openIDServiceConfiguration: OpenIDServiceConfiguration(provider: Google(), 
                                                  clientId: "YOUR_CLIENT_ID",
                                                  redirectUri: "YOUR_REDIRECT_URI"))
                                                   
model.zkLogin(zkLoginRequest: google)
```

The `ZKLoginModel` will handle the zkLogin flow and return the result, a `ZKLoginResponse`, to the observer.
