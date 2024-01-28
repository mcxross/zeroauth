# Introduction

ZeroAuth provides full support for zkLogin on desktop platforms, including Windows, Linux, and macOS. This support is
available natively or through the Java Virtual Machine (JVM).

On these desktop platforms, ZeroAuth aims to seamlessly manage the entire zkLogin flow, similar to its approach on other
platforms. The process involves composing a request for an OpenID provider, launching a browser window, and handling the
redirect back to your application. ZeroAuth efficiently handles all these steps, eliminating the need for you to worry
about them. However, you have the flexibility to intervene at any stage and reclaim control if necessary. For detailed
information on the zkLogin flow on desktop platforms, refer to
the [Conceptual Overview](/platform/desktop/conceptual-overview.md) section.

The underlying philosophy of ZeroAuth is to offer a simple, intuitive API that enables developers to easily integrate
zkLogin into their applications. Despite its streamlined operation, ZeroAuth ensures that you retain full control over
the process, courtesy of its highly decoupled architecture.