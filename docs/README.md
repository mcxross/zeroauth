# ZeroAuth Documentation

This is the official documentation for ZeroAuth, a cross-platform, native SDK for zkLogin.

### Structure

The documentation is currently structured as follows:

- [Guide](/guide/) - This covers the overview of ZeroAuth, its features, and how to get started.
- [Platform Guides](/guide/platforms/) - This covers the platform-specific guides for iOS/macOS, Android, and Windows/Linux.
- [zkLogin](/zklogin/) - This covers zkLogin, the underlying protocol that ZeroAuth is built on.
- [Advanced Guides](/guide/advanced/) - This covers advanced topics, such as customizing the UI, handling errors, and more.


### Building

The documentation is built using [VuePress](https://vuepress.vuejs.org/). To build the documentation, run the following commands:

```bash
# Install dependencies
pnpm install
```

```bash
# Build the documentation and serve it
pnpm run docs:dev
```

### Deploying

To deploy the documentation, run the following commands:

```bash
# Build the documentation
pnpm run docs:build
```

The command will generate a `docs/.vuepress/dist` directory. This directory can then be deployed to any static site hosting service.

### Contributing

Contributions are welcome! Please read our [contributing guidelines]() before submitting a pull request.

### License

ZeroAuth is licensed under the [Apache-2.0 License](https://github.com/mcxross/zeroauth/blob/master/LICENSE).