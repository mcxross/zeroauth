const zkLoginNotifier: zero.ZKLoginNotifier = new zero.ZKLoginNotifier();
const listener: zero.Listener = {
    invoke: function(request: zero.Request, response: zero.Response, error: zero.Error): void {
        // Do something with the response
    }
};
zkLoginNotifier.setListener(listener);
zero.continueWithZKLogin(null, null, zkLoginNotifier);
