package okan.apps.shareontwitter;

import org.apache.cordova.api.CallbackContext;
import org.apache.cordova.api.CordovaPlugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterPlugin extends CordovaPlugin {

	private static final String ACTION_SHARE_ON_TWITTER = "shareOnTwitter";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

		if (action.equals(ACTION_SHARE_ON_TWITTER)) {
			
			shareOnTwitter(action, args, callbackContext);
			
		} else {
			PluginResult pluginResult = new PluginResult(PluginResult.Status.INVALID_ACTION);
			callbackContext.sendPluginResult(pluginResult);	
			return false;
		}
		return true;
	}

	private boolean shareOnTwitter(String action, JSONArray jsonArray,final  CallbackContext callbackContext) throws JSONException {
		
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		String message = jsonObject.getString("message");

		TwitterActivity.twitterManager.sendTweet(message, new TwitterInterface() {
			
			@Override
			public void twitterPostResult(boolean isSuccess) {

				// TODO Send feedback to JS here...
				// TODO Remove Toast Message and replace with alert message
				// which is triggered by JS
				PluginResult result;
				if (isSuccess) {
					result = new PluginResult(PluginResult.Status.OK);
					result.setKeepCallback(false);
					callbackContext.sendPluginResult(result);
				} else {
					result = new PluginResult(PluginResult.Status.ERROR);
					result.setKeepCallback(false);
					callbackContext.sendPluginResult(result);
				}

			}
		});

		/*
		 * Set Plugin Result as NO_RESULT to make the plugin waits for the
		 * result of Async Prcess in the background
		 */
		PluginResult.Status status = PluginResult.Status.NO_RESULT;

		PluginResult pluginResult = new PluginResult(status);
		pluginResult.setKeepCallback(true);
		callbackContext.sendPluginResult(pluginResult);

		return true;
	}
}
