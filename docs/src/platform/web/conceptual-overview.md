# Conceptual Overview

ZeroAuth for Web ships and encourages two ways to initiate a zkLogin flow on Web supporting both 
Single Page Applications (SPAs) and Multi Page Applications (MPAs):

* **Functional style**: This approach offers a top-level function requiring at least a `ZKLoginRequest` instance 
  as input.
* **Object-oriented style**: ZeroAuth provides a default implementation of `ZKLoginService`. This encapsulates 
  the zkLogin initiation process.

In any of the aforementioned approaches, there exists a **callable**, `zkLogin`, which requires at minimum 
a `ZKLoginRequest` object. The `ZKLoginRequest` object, as its name implies, encapsulates a zkLogin request. It 
facilitates the configuration of an **OAuth provider**.
Once the `ZKLoginRequest` object is configured, it is passed to the `zkLogin` callable which builds 
the `AuthorizationRequest` object and triggers the OAuth flow. At this point, the user is redirected to the OAuth 
provider's login page. Once the user has successfully logged in, the OAuth provider redirects the user back to 
the `redirect_uri`. On the redirect page, a special `continueWithZKLogin` function is called which extracts the `token` 
from the URL and then makes subsequent calls to salting and proving services. From here, the results are relayed back
via a `ZKLoginListener` object.

