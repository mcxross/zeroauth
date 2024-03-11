**Table of Contents**

[[toc]]

# Quick Start

In this guide, we'll walk you through integrating zkLogin into your iOS application using the ZeroAuth SDK. 
Everything you need to kickstart the process is covered here.

## Prerequisites

This guide assumes you have a basic understanding of iOS development. It also assumes you have a [conceptual](ihttps://zeroauth.dev/platform/ios/conceptual-overview.html) 
understanding of zkLogin. If you are new to zkLogin, please refer to the [zkLogin documentation](https://docs.sui.io/build/zk_login).

If you understand the above, you are ready to get started, but before we do, you must have the following:
- **OAuth client credentials (`clientId`)** from an OAuth provider. For this guide, we will use Google, but the process is similar for other providers. 
You typically get this from the OAuth provider's developer console.
- **A remote salting service**. For this guide, we will use the default remote salting service that implements using Mysten's salting service,
but the process is similar for other **remote** salting services including _standard_ self-hosted salting services.
For this, your **client credentials** must be whitelisted by [Mysten Labs](https://mystenlabs.com/).
- **A remote proving service**. For this guide, we will use the default remote proving service provided by [Mysten Labs](https://mystenlabs.com/),
but the process is similar for other **remote** proving services including _standard_ self-hosted proving services. White listing for this is not needed
since the devnet proving service is open to all.

## Installation

::: warning
ZeroAuth `ZeroAuth` iOS SDK is currently in `beta` stage, and the API is subject to rapid unannounced changes until the first stable release.
Please report any issues you encounter. Stable versions will be available soon.
:::


`ZeroAuth` iOS SDK current only supports Swift Package Manager. To add ZeroAuth SDK to an iOS project, add the following to your `Package.swift` file:

### Swift Package Manager

```swift
dependencies: [
    .package(url: "https://github.com/mcxross/swift-zeroauth.git", .upToNextMajor(from: "0.1.0-beta"))
]
```

## Usage

Once you have the ZeroAuth SDK installed, you can start using it in your iOS application.

The main entry for the SDK is the `ZKLoginModel`.  This  utilizes the `ObservableObject` protocol to allow you to observe the zkLogin response.
It also has methods for accessing models, `UnauthenticatedViewModel`,  and `AuthenticatedViewModel`  for both **unauthenticated** and **authenticated** views respectively. 
The `UnauthenticatedViewModel` is used to initiate the zkLogin request and the `AuthenticatedViewModel` is used to log out the user.

In your main `struct`, add an observer to listen for the zkLogin response:

```swift
@ObservedObject private var zklModel: ZKLoginModel
```

On the `zklModel`, you can check the published `response` property to get the current state of the zkLogin flow.
You can then use this to determine the view to display to the user.

```swift
if zkLModel.response != nil {
      // User is logged in
} else {
      // User is not logged in
}
```
In your **Unauthenticated** view, you can use the `UnauthenticatedViewModel` to initiate the zkLogin request:

```swift
let google = ZKLoginRequest(openIDServiceConfiguration: OpenIDServiceConfiguration(provider: Google(), 
                                                  clientId: "YOUR_CLIENT_ID",
                                                  redirectUri: "YOUR_REDIRECT_URI"))
                                                   
model.zkLogin(zkLoginRequest: google)
```

The `UnauthenticatedViewModel` will handle the zkLogin flow and return the result, a `ZKLoginResponse`, to the observer.  This has all the necessary data to make transactions.

For a complete example, see the [ZeroAuth iOS SDK Example](https://github.com/mcxross/swift-zeroauth/tree/main/Sample/ZeroAuth%20iOS%20Demo).

That's it! You have successfully integrated zkLogin into your iOS application using the ZeroAuth SDK.
