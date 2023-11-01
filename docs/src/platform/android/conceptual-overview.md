# Conceptual Overview

There are three _standard_ ways to initiate a zkLogin flow on Android accommodating either Kotlin or/and Java Projects:
* **Context class Infix extension function style**: ZeroAuth injects an infix extension function into the `Context` class
accessible in your activity
* **Functional style**: This approach offers a top-level function requiring at least a `Context` and a `ZKLoginRequest`
instances as inputs.
* **Object-oriented style**: ZeroAuth provides a default implementation of `ZKLoginService`. This encapsulates the
zkLogin initiation process.

In any of the aforementioned approaches, there exists a **callable**, `zkLogin`, which requires at minimum
a `ZKLoginRequest` object. The `ZKLoginRequest`
object, as its name implies, encapsulates a zkLogin request. It facilitates the configuration of an **OAuth provider**
and the definition of a *salting* or
*proving* service. This component is pivotal for extending core features of the framework, establishing a foundation for
customization and scalability.

The zkLogin request yields an `intent` which should be utilized to launch the `ZKLoginManagementActivity`. This activity
orchestrates the OAuth flow, managing
the outcomes adeptly and relaying the result—usually a `ZKLoginResponse` upon success or `null` if failed—back to your
calling activity. Through this mechanism, ZeroAuth ensures a smooth transition between the initiation of the 
login process and the handling of its result, providing a streamlined workflow for authentication within your Android application.