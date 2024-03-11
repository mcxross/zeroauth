# Conceptual Overview

Adding zkLogin to your application is a straightforward process. The following steps are involved:

1. **ZeroAuth Dependency** - Simply add the ZeroAuth SDK for iOS to your project for example using Swift Package Manager.
2. **Observe for zkLogin Response** - Add an observer to listen for the zkLogin response.
3. **Initiate zkLogin Request** - Use the `ZKLoginRequest` object to initiate the zkLogin request.  If the operation is successful, the `ZKLoginResponse` object will be returned to the observer.
This has all the necessary data to make transactions.