package okan.apps.shareontwitter;

import android.os.Bundle;
import android.app.Activity;

public class TwitterActivity extends Activity {

	public static TwitterManager twitterManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);
		
		twitterManager = new TwitterManager(Constants.TWITTER_CONSUMER_KEY, Constants.TWITTER_CONSUMER_SECRET,
				Constants.TWITTER_CALLBACK_URL, this);
		
		if (savedInstanceState != null) {
			// Twitter
			twitterManager.restoreInstanceState(savedInstanceState);
		}
	}
}
