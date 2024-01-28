zkLogin(req, new ZKLoginFeedback() {
    @Override
    public void onToken(Option<String> option) {
        System.out.println("onToken: " + option);
    }

    @Override
    public void onProof(Option<String> option) {
        System.out.println("onProof: " + option);
    }

    @Override
    public void onSalt(Option<String> option) {
        System.out.println("onSalt: " + option);
    }
});
