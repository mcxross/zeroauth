# Conceptual Overview

The following steps are involved:

1. **Injection** - This is the process of injecting a `ZKLoginRequest` object into the `zkLogin` callable.
2. **OAuth flow** - This is the process of redirecting the user to the OAuth provider's login page. Once the user has
   successfully logged in, the OAuth provider redirects the user back to the `redirect_uri` you specified in the zkLogin
   request object.

    > **Note:** The `redirect_uri` is a special URI that is used to redirect the user back to your application. ZeroAuth
    > spawns a local web server to listen for the redirect. This web server is automatically shut down once the redirect
    > is handled.

3. **Salting** - This is the process of generating a salt request. This is done by calling the salting service with the
   `token` received from the OAuth provider.
4. **Proving** - This is the process of generating a proof request. This is done by calling the proving service with the
   `token` and `salt` received from the salting service.
5. **Relaying results** - The results of the salting and proving services are relayed back to the caller via a
   `ZKLoginListener` object.