package okan.apps.shareontwitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TwitterManager {
	
	private static final String TAG = TwitterManager.class.getName();
	
	private static final String IEXTRA_AUTH_URL = "auth_url";
	private static final String IEXTRA_OAUTH_VERIFIER = "oauth_verifier";
	private static final String IEXTRA_OAUTH_TOKEN = "oauth_token";
	
	private static final String PREFERENCE_NAME = "twitter_oauth";
	private static final String PREF_KEY_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TOKEN = "oauth_token";
	
	private Activity activity;
	private final String consumerKey;
	private final String consumerSecret;
	private final String callbackUrl;
	
	private Twitter twitter;
	private RequestToken requestToken;
	
	private SharedPreferences sharedPreferences;
	
	private String tweetMessage;
	
	TwitterInterface twitterInterfaceListener;
	
	public TwitterManager(String consumerKey, String consumerSecret, String callbackUrl, Activity activity) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.callbackUrl = callbackUrl;
		this.activity = activity;

		sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Activity.MODE_PRIVATE);

		twitter = new TwitterFactory().getInstance();

		twitter.setOAuthConsumer(consumerKey, consumerSecret);
	}

	private void handleCallback(Intent intent) {
		
		final Uri uri = intent.getData();
		
		if (uri != null && uri.toString().startsWith(callbackUrl)) {
			AsyncTask<Void, Void, AccessToken> task = new AsyncTask<Void, Void, AccessToken>() {
				private String verifier;
				
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					verifier = uri.getQueryParameter(IEXTRA_OAUTH_VERIFIER);
				}
				
				@Override
				protected AccessToken doInBackground(Void... params) {
					try {
						
						AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
						return accessToken;
						
					} catch (Exception e) {
						e.printStackTrace();
						return null;
					}
				}
				
				@Override
				protected void onPostExecute(AccessToken result) {
					super.onPostExecute(result);

					if (result != null) {
						Editor e = sharedPreferences.edit();
						e.putString(PREF_KEY_TOKEN, result.getToken());
						e.putString(PREF_KEY_SECRET, result.getTokenSecret());
						e.commit();
						Log.d(TAG, "handled uri");
						
						// User gave Authorization, send message
						postTweet(tweetMessage);
						
					} else {
						twitterInterfaceListener.twitterPostResult(false);
					}
				}
			};
			task.execute();
		}
	}

	private boolean isAuthorized() {
		return sharedPreferences.getString(PREF_KEY_TOKEN, null) != null;
	}

	private void authorizeUser(final TwitterManagerTokenHadlerListener tokenHandler) {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(consumerKey);
		configurationBuilder.setOAuthConsumerSecret(consumerSecret);
		Configuration configuration = configurationBuilder.build();
		twitter = new TwitterFactory(configuration).getInstance();

		AsyncTask<Void, Void, RequestToken> tokenTask = new AsyncTask<Void, Void, RequestToken>() {

			private ProgressDialog dialog;

			private TwitterManagerTokenHadlerListener thListener;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				thListener = tokenHandler;

				dialog = new ProgressDialog(activity);
				dialog.setMessage("Please Wait");
				dialog.show();
			}

			@Override
			protected RequestToken doInBackground(Void... params) {
				RequestToken r = null;
				try {
					r = twitter.getOAuthRequestToken(callbackUrl);
				} catch (TwitterException e) {
					e.printStackTrace();
				}

				return r;
			}

			@Override
			protected void onPostExecute(RequestToken result) {
				super.onPostExecute(result);

				dialog.dismiss();
				
				if (result != null) {
					
					requestToken = result;
					// Sending succesfull message back
					thListener.tokenHandleSuccessful();
				} else {
					thListener.tokenHandleFailed();
					twitterInterfaceListener.twitterPostResult(false);
				}
			}
		};
		tokenTask.execute();
	}
	
	private void redirectToBrowser() {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()))
				.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY
						| Intent.FLAG_FROM_BACKGROUND);
		activity.startActivity(intent);
	}
	
	public void disconnectTwitter() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.remove(PREF_KEY_TOKEN);
		editor.remove(PREF_KEY_SECRET);

		editor.commit();
	}
	
	public void sendTweet(final String message, TwitterInterface feedbackListener) {
		
		this.twitterInterfaceListener = feedbackListener;
		
		this.tweetMessage = message;
				
		if (NetworkUtils.hasConnection(activity)) {
			
			if (!isAuthorized()) {
				
				authorizeUser(new TwitterManagerTokenHadlerListener() {
					@Override
					public void tokenHandleSuccessful() {
						// Redirect to the Browser.
						redirectToBrowser();
					}
					
					@Override
					public void tokenHandleFailed() {
						// Call javaScript function here to give feedback
					}
				});
			} else {
				postTweet(message);
			}
		} else {
			showCheckConnectionMessage();
		}
	}
	
	private void postTweet(final String message) {
				
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(message).setTitle("Sharing Message");
		builder.setPositiveButton("Share", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				final String oauthAccessToken = sharedPreferences.getString(PREF_KEY_TOKEN, "");
				final String oAuthAccessTokenSecret = sharedPreferences.getString(PREF_KEY_SECRET, "");

				AsyncTask<Void, Void, Boolean> sendTweetTask = new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected Boolean doInBackground(Void... params) {
						boolean success = true;
						try {
							ConfigurationBuilder confbuilder = new ConfigurationBuilder();
							Configuration conf = confbuilder.setOAuthConsumerKey(consumerKey)
									.setOAuthConsumerSecret(consumerSecret).build();

							AccessToken token = new AccessToken(oauthAccessToken, oAuthAccessTokenSecret);

							Twitter twitter = new TwitterFactory(conf).getInstance(token);
							twitter.updateStatus(message);
						} catch (TwitterException e) {
							e.printStackTrace();
							success = false;
						}

						return success;
					}

					@Override
					protected void onPostExecute(Boolean result) {
						super.onPostExecute(result);

						if (result) {
							// Call JS to say successful
							twitterInterfaceListener.twitterPostResult(true);
							Toast.makeText(activity, "Tweet has been sent !", Toast.LENGTH_SHORT).show();
						} else {
							// Call JS to say fail
							twitterInterfaceListener.twitterPostResult(false);
							Toast.makeText(activity, "Tweet Could not sent !", Toast.LENGTH_SHORT).show();
						}
					}
				};
				sendTweetTask.execute();
			}
		});
		builder.setNegativeButton("Cancel", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void showCheckConnectionMessage() {
		Toast.makeText(activity, "Check your internet connection !", Toast.LENGTH_SHORT).show();
	}
	
	public static interface TwitterManagerUriHandlerListener {
		void uriHandled();
		void uriHandleFailed();
	}
	
	public static interface TwitterManagerTokenHadlerListener {
		void tokenHandleSuccessful();
		void tokenHandleFailed();
	}
	
	/*
	 * 
	 * TWITTER INTERFACE IMPLEMENTATION
	 * 
	 */
	
	public void handleTwitterCallback(Intent intent) {
		handleCallback(intent);
	}
	
	public void saveInstanceState(Bundle outState) {
		outState.putSerializable("twitterToken", requestToken);
	}
	
	public void restoreInstanceState(Bundle bundle) {
		requestToken = (RequestToken) bundle.getSerializable("twitterToken");
	}
}
