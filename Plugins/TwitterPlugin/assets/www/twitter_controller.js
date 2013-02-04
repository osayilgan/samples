// execute sharing on twitter
function share(callbackSuccess, callbackFail) {
    function onFunctionSuccess(result) {
        if (typeof callbackSuccess === "function") {
            callbackSuccess.apply({}, [ result ]);
        }
    }
    function onFunctionFail(error) {
        var err = "ERROR: " + error;
        console.log(err);
        if (typeof callbackFail === "function") {
            callbackFail.apply({}, [ err ]);
        }
    }
    
    // test data
	var data = {
		"message" : "Test message for twitter !"
	};
    
    // trigger function
    window.plugins.TwitterPlugin.shareOnTwitter(data, onFunctionSuccess, onFunctionFail);
}