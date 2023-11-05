const zkLoginNotifier = new zero.ZKLoginNotifier();
const listener = {
    invoke: function(request, response, error) {
        // Do something with the response
    }
};
zkLoginNotifier.setListener(listener);
zero.continueWithZKLogin(null, null, zkLoginNotifier);