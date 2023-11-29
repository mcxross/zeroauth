# ZeroAuth Vanilla Sample
This is a sample site that demonstrates how to use **ZeroAuth** with vanilla web technologies.

> The concepts introduced here can be applied to any web framework.

## Getting Started with Your Project

To kickstart, follow these simple steps:

1. **Clone the Repository**: Begin by cloning the repository to your local machine.

2. **Configure Client ID**:
    - Locate the [`index.html`](https://github.com/mcxross/zeroauth/sample/js/vanilla/src) file.
    - Find the placeholder for the Client ID.
    - Replace it with your **Client ID**, which can be obtained from your OAuth2 provider. This is an essential step for setting up authentication.

3. **Set Up OAuth2 Redirect URI**:
    - Access your OAuth2 provider's settings.
    - Specify the **Redirect URI**. This should be set to `http://localhost:9000/redirect.html`. It's a crucial step to ensure that the OAuth2 flow works correctly after authentication.

4. **Start the Server**:
    - Open your terminal or command prompt.
    - Navigate to the project's directory where the repository was cloned.
    - Run the command `npm run serve`. This will start the local development server, typically accessible via `http://localhost:9000`.

> :warning: **Important**: ZeroAuth ships with Mysten Labs managed salting and proving services. So, unless your Client ID is whitelisted, you'll need to run your own salting and proving services. For more information, please refer to the [documentation](https://blog.sui.io/proving-service-zklogin/).

By following these steps, your project setup will be completed, and you'll be ready to proceed with development. Happy coding!
