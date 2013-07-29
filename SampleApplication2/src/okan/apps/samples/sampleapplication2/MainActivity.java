package okan.apps.samples.sampleapplication2;

import okan.apps.samples.sampleapplication2.audio.AudioConstants;
import okan.apps.samples.sampleapplication2.audio.AudioObject;
import okan.apps.samples.sampleapplication2.audio.AudioService;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	/** Intent for Audio Service */
	Intent audioPlayerService;
	
	/**
	 * Broadcast Receiver to Receiver Audio Player Updates Sends current
	 * Process, buffer Percentage and end of the song.
	 */
	private BroadcastReceiver audioPlayerBroadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent serviceIntent) {
			
			long audioObjectId = ((AudioObject) serviceIntent.getSerializableExtra(AudioConstants.AUDIO_ITEM)).getId();
			
			/* Update the UI of Grid View items */
			updateView(audioObjectId, serviceIntent);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		/* Activate Service in the Main Activity */
		audioPlayerService = new Intent(this, AudioService.class);
		startService(audioPlayerService);
		
		/* Register Receiver */
		registerReceiver(audioPlayerBroadcastReceiver, new IntentFilter(AudioConstants.BROADCAST_AUDIO_UPDATE_CHANNEL));
	}
	
	/**
	 * Updates the UI with extras received from intent.
	 * 
	 * @param objectId			ID of the Audio Object.
	 * @param serviceIntent		intent holds extras to update the View.
	 */
	private void updateView(long objectId, Intent serviceIntent) {
		
		// TODO
		// Update View Here based on request.
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		/* Register Receiver */
		registerReceiver(audioPlayerBroadcastReceiver, new IntentFilter(AudioConstants.BROADCAST_AUDIO_UPDATE_CHANNEL));
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		/* Unregister Receiver */
		unregisterReceiver(audioPlayerBroadcastReceiver);
	}
}
