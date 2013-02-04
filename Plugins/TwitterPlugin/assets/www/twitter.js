/**
 * Constructor
 */
function TwitterPlugin() {
};

TwitterPlugin.prototype.shareOnTwitter = function(data, successCB, errorCB) {
	
	// execute native side
	cordova.exec(successCB, errorCB, "TwitterPlugin", "shareOnTwitter", [data]);
};

TwitterPlugin.prototype.install = function() {
};

/**
 * Load TwitterPlugin
 */
cordova.addConstructor(function() {
    if (!window.plugins) {
        window.plugins = {};
    }
    if (!window.Cordova) {
        window.Cordova = cordova;
    }
	
    window.plugins.TwitterPlugin = new TwitterPlugin();
});