<p align="center">
    <img src="artwork/logo.png" alt="ZeroAuth logo" width="50" height="50">
</p>

<h2 align="center">ZeroAuth</h2>

<br>

<p align="center">Multi-platform zkLogin SDK Suite</p>

ZeroAuth is a multi-platform SDK suite for zkLogin. It is designed to be easy to use, and easy to integrate into your
existing projects. It offers a consistent native API across different platforms: use zkLogin with Objective-C on
iOS/macOS,
Kotlin/Java on Android, or C++/C on Windows and Linux.

The libraries meticulously adheres to the best practices delineated
in [RFC 8252 - OAuth 2.0 for Native Apps](https://tools.ietf.org/html/rfc8252), employing Custom Tabs for authorization
requests on Android and utilizing `SFAuthenticationSession` and `SFSafariViewController` for auth requests on iOS. To
maintain stringent usability and security standards, `WebView` on Android, along with `UIWebView` and `WKWebView` on
iOS, are explicitly unsupported, as elucidated
in [Section 8.12 of RFC 8252](https://tools.ietf.org/html/rfc8252#section-8.12).

<br>

[![Build Status](https://travis-ci.org/zeroauth/zeroauth.svg?branch=master)](https://travis-ci.org/zeroauth/zeroauth)
![vcpkg](https://img.shields.io/badge/vcpkg-zeroauth-blue.svg)
[![Maven Central](https://img.shields.io/maven-central/v/xyz.mcxross.zero/zero)](https://search.maven.org/artifact/xyz.mcxross.zero/zero)
[![npm](https://img.shields.io/npm/v/mcxross/zero)](https://www.npmjs.com/package/@mcxross/zero)
![cocoapods](https://img.shields.io/badge/cocoapods-zeroauth-blue.svg)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)

![badge-android](http://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android)
![badge-ios](http://img.shields.io/badge/Platform-iOS-orange.svg?logo=apple)
![badge-js](http://img.shields.io/badge/Platform-NodeJS-yellow.svg?logo=javascript)
![badge-jvm](http://img.shields.io/badge/Platform-JVM-red.svg?logo=openjdk)
![badge-linux](http://img.shields.io/badge/Platform-Linux-lightgrey.svg?logo=linux)
![badge-macos](http://img.shields.io/badge/Platform-macOS-orange.svg?logo=apple)
![badge-windows](http://img.shields.io/badge/Platform-Windows-blue.svg?logo=windows)

# Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

# Introduction

Each SDK is fundamentally designed with three primary components to enhance functionality and security: an OAuth and
OpenID Connect client, a Zero Knowledge Proof (ZKP) client, and a Salt client. These essential elements are readily
available across all platforms, offering you flexibility in managing your zkLogin flow. You can choose to delegate the
authentication process or navigate through the steps independently, with the assurance that the intricate backend
mechanisms are seamlessly handled for you. This structure not only ensures a robust security posture but also
streamlines the developer experience, allowing for efficient integration and interaction within various client
environments.

The zkLogin process is a concise, three-step sequence ensuring user privacy and security:

* **OAuth/OpenID Connect**: Handles user authorization and token exchange, verifying identity securely.

* **Salt Client**: Generates a unique 'salt,' anonymizing users to separate their identity from on-chain actions.

* **ZKP Client**: Receives the 'salt' and creates a cryptographic proof, authenticating users without exposing personal
  data.

# Features

* Multi-platform support
* Consistent API across platforms
* Modular design
* Easy to use and Integrates with existing projects

# Usage

For how to use the respective SDKs, please see the [documentation](https://zeroauth.dev) which provides a detailed guide on
how-tos and best practices.

# Contributing

All contributions to ZeroAuth are welcome. Before opening a PR, please submit an issue detailing the bug or feature. When
opening a PR, please ensure that your contribution builds on the KMM toolchain, has been linted
with `ktfmt <GOOGLE (INTERNAL)>`, and contains tests when applicable. For more information, please see
the [contribution guidelines](CONTRIBUTING.md).

# License

```text
    Copyright 2022 McXross

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```
