const provider: zero.Google = new zero.Google();
const cfg: zero.OpenIDServiceConfiguration = new zero.OpenIDServiceConfiguration(
    provider,
    "YOUR_CLIENT_ID",
    "YOUR_REDIRECT_URI"
);
const zkLoginRequest: zero.ZKLoginRequest = new zero.ZKLoginRequest(
    cfg
);
