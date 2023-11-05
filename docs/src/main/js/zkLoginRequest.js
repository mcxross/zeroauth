const provider = new zero.Google();
const cfg = new zero.OpenIDServiceConfiguration(
    provider,
    "YOUR_CLIENT_ID",
    "YOUR_REDIRECT_URI",
);
const zkLoginRequest = new zero.ZKLoginRequest(
    cfg,
);