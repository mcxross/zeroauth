**Table of Contents**

[[toc]]

# Quick Start

In this guide, we'll walk you through integrating zkLogin into your web application 
using the ZeroAuth Web SDK. Everything you need to kickstart the process is covered here, and the 
concepts are equally applicable to websites.

## Prerequisites

This guide assumes you have a basic understanding of web development and JavaScript. It also assumes you have a 
conceptual understanding of zkLogin. If you are new to zkLogin, please refer to 
the [zkLogin documentation](https://docs.sui.io/build/zk_login).

If you understand the above, you are ready to get started, but before we do, you must have the following:
- **OAuth client credentials (`clientId`)** from an OAuth provider. For this guide, we will use Google, but the process is similar 
  for other providers. You typically get this from the OAuth provider's developer console.
- **A remote salting service**. For this guide, we will use the default remote salting service provided by
  [Mysten Labs](https://mystenlabs.com/), but the process is similar for other **remote** salting services including _standard_ self-hosted
  salting services.
  For this, your **client credentials** must be whitelisted by [Mysten Labs](https://mystenlabs.com/).
- **A remote proving service**. For this guide, we will use the default remote proving service provided by 
  [Mysten Labs](https://mystenlabs.com/), but the process is similar for other **remote** proving services including _standard_ self-hosted 
  proving services. For this, your **client credentials** must be whitelisted by [Mysten Labs](https://mystenlabs.com/).

## Installation

::: warning
ZeroAuth zkLogin Web SDK is currently in `beta` stage, and the API is subject to rapid changes until the first stable release.
Please report any issues you encounter. Currently only snapshot versions are available. Stable versions will be available soon.
:::

To add ZeroAuth SDK to a web project depends on how you are building your project. For example, if you are using a bundler
like Webpack or Vite, you can install the SDK using npm or yarn or pnpm:

:::: code-group
::: code-group-item pnpm
```bash
pnpm add @mcxross/zero
```
:::
::: code-group-item npm
```bash
npm install @mcxross/zero
```
:::
::: code-group-item yarn
```bash
yarn add @mcxross/zero
```
:::
::::


If you are using a CDN, you can add the following to your HTML file:

```html
<script src="https://unpkg.com/@mcxross/zero@<version>"></script>
```

::: tip
The ZeroAuth Web SDK offers two library flavors to cater for different development needs:
- Release Flavor: This flavor is optimized for production environments.
- A Snapshot Flavor: This version is tailored for development and testing phases. It includes additional 
  logging to aid developers in troubleshooting and refining the ZeroAuth integration within their projects.
The snapshot flavor is only available via npm, yarn, or pnpm. It is not available via CDN.
:::


## Usage

The usage (the code) of the ZeroAuth Web SDK matches the conceptual overview described in 
the [Conceptual Overview](/platform/web/conceptual-overview.md) section.

### ZKLoginRequest

Create an instance of the `ZKLoginRequest` object.

:::: code-group
::: code-group-item JavaScript
@[code js](../../main/js/zkLoginRequest.js)
:::
::: code-group-item TypeScript
@[code ts](../../main/ts/zkLoginRequest.ts)
:::
::: code-group-item Kotlin/Js
@[code kt](../../main/kotlin/zkLoginRequest.kt)
:::
::::

### Trigger zkLogin flow

Pass the zkLogin request object to the `zkLogin` callable:

:::: code-group
::: code-group-item JavaScript
@[code js](../../main/js/zkLogin.js)
:::
::: code-group-item TypeScript
@[code ts](../../main/ts/zkLogin.ts)
:::
::: code-group-item Kotlin/Js
@[code kt](../../main/kotlin/zkLogin.kt)
:::
::::

This will trigger a redirect to the OAuth provider's login page. Once the user has successfully logged in, the OAuth
provider redirects the user back to the `redirect_uri` you specified in the zkLogin request object.

### Handle the redirect

Okay, now we need to handle the redirect. To do this, we need to use a special `continueWithZKLogin` function on the
`redirect_uri` page. This takes in a Salting Service, a Proving Service, and a `ZKLoginNotifier`. If the first two are
not provided, the default remote salting and proving services provided by [Mysten Labs](https://mystenlabs.com/) will 
be attempted.

:::: code-group
::: code-group-item JavaScript
@[code js](../../main/js/continueWithZKLogin.js)
:::
::: code-group-item TypeScript
@[code ts](../../main/ts/continueWithZKLogin.ts)
:::
::: code-group-item Kotlin/Js
@[code kt](../../main/kotlin/continueWithZKLogin.kt)
:::
::::

The notifier will deliver the results of the process to your provided listener, which you can use to handle the results