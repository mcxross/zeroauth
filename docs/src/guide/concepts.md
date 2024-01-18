# Core Concepts

Each ZeroAuth SDK consists of mainly three decoupled components:

- **OAuth 2.0 and OpenID Connect (OIDC) client library**: This library is responsible for handling the OAuth 2.0 and
  OIDC flows. It strives to directly map the requests and responses of those specifications and is responsible for
  making the
  necessary HTTP requests to the authorization server and handling the responses.
  It also provides a simple API for developers to use.

- **Salt Service client library**: This library is responsible for handling the communication with the Salt Service.
  It provides a simple API for developers to use.

- **Proving Service client library**: This library is responsible for handling the communication with a ZK Proving
  Service.
  It provides a simple API for developers to use.

While you can use each of these components separately, you can delegate the entire process to the SDK, which is the
recommended approach.

## OAuth 2.0 and OpenID Connect (OIDC) client library

The OAuth 2.0 and OIDC client library is responsible for handling the OAuth 2.0 and OIDC flows. It follows best
practices for native apps as outlined in [RFC 8252](https://tools.ietf.org/html/rfc8252)
and [OAuth 2.0 for Native Apps](https://tools.ietf.org/html/draft-ietf-oauth-native-apps-12).

It provides a generic API that allows developers to use any OAuth 2.0 and OIDC provider. It also provides a set of APIs
for specific providers, which makes it easier to use.

## Salt Service client library

In **zkLogin**, the use of salting is a critical measure for protecting user identity and privacy. This technique
involves appending a random data string to the user's identity (specifically, the OAuth identifier or 'sub'), thereby
obscuring any direct connection between the user's Web2 and Web3 credentials. This process significantly enhances
privacy by preventing cross-referencing of user credentials.

The application can implement salting through several methods, generally categorized into two main approaches: **client-side** and **server-side**.

1. **Client-Side Salting**: In this scenario, the responsibility of generating and maintaining the salt rests either
   with the application itself or the user. The application can directly manage salt creation and storage.
   Alternatively, it might transfer this duty to users, allowing for a more distributed form of security management,
   thus giving users direct control over their privacy.

2. **Server-Side Salting**: Conversely, with server-side salting, the application entrusts the salt management role to
   an external entity known as the Salt Service. This delegation can involve mapping the user's identifier (like 'sub')
   to their respective salt. The architecture here can vary — ranging from using traditional databases (possibly
   integrating with existing user or password tables) to employing specialized services. One such dedicated solution is
   Mystenlabs' Salt Service, accessible via [their interface](https://salt.api.mystenlabs.com/get_salt).

   Utilizing the default Salt Service Client requires the application's backend to support REST operations, specifically
   accepting a JWT token encapsulated as `{"token": "$JWT_TOKEN"}` and responding with the corresponding salt in a JSON
   format, noted as `{salt: "salt"}`. However, if the application's backend does not conform to these specifications or
   if there are other unique requirements, there is a need to develop a custom Salt Service Client. This tailored client
   would then integrate with the SDK, ensuring proper communication and data handling tailored to the application's
   specific needs.

By choosing between client-side and server-side salting based on the application's structure, security needs, and
resource allocation, developers can effectively bolster user privacy and the overall security posture of the system
involved in the zkLogin process.

## Proving Service client library

Generating a cryptographic proof is a cornerstone of the zkLogin process, essential for the zkLogin protocol.
This step, however, is known for being computationally demanding, which can lead to delays that impact the user
experience.

To mitigate this challenge, the ZeroAuth SDKs introduce a Proving Service client library. This library acts as an
intermediary, allowing the application to offload the intensive proof generation process to an external ZK Proving
Service. The beauty of this approach lies in its flexibility — developers have the option to host their own service or
engage a third-party provider.

For those interested in establishing their own ZK Proving Service, detailed guidelines are available in
the [documentation](https://docs.sui.io/build/zk_login#run-the-proving-service-in-your-backend). This resource provides
comprehensive instructions, enabling developers to integrate the service smoothly into their backend systems.

Alternatively, Mystenlabs offers a proprietary ZK Proving Service. However, access is currently restricted and available
only on a whitelist basis. Developers seeking to explore this option or wishing to request access can reach out directly
to
[Mystenlabs](https://mystenlabs.com/news) for more information.

Regardless of the chosen setup, the Proving Service client library within the ZeroAuth SDKs simplifies interaction with
the ZK Proving Service through a straightforward API, making it easier for developers to integrate these capabilities
into their applications. However, suppose developers opt for a customized Proving Service that deviates from standard
specifications. In that case, they will need to create a bespoke Proving Service client library. This custom library
must be compatible with the overarching SDK environment, ensuring seamless communication and operational coherence
within the zkLogin ecosystem.

This balanced combination of computational delegation and streamlined developer interfaces ensures that applications can
perform rigorous zero-knowledge proofs without burdening the client-side resources or compromising the user experience,
maintaining the delicate equilibrium between performance and privacy.