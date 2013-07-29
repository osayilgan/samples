package okan.apps.samples.sampleapplication2.audio;

import java.io.File;
import java.io.IOException;

import okan.apps.samples.sampleapplication2.MainActivity;
import okan.apps.samples.sampleapplication2.R;
import okan.apps.samples.sampleapplication2.audio.AudioConstants.AudioStatus;
import okan.apps.samples.sampleapplication2.utils.NetworkUtils;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class AudioService extends Service implements OnCompletionListener, OnPreparedListener, OnErrorListener, OnBufferingUpdateListener {
	
	/** Android Media Player Instance */
	private MediaPlayer mediaPlayer;
	
	/** Dialect Object */
	private AudioObject currentAudioObject;
	
	/* Set up the notification ID for Push Messages */
	private static final int NOTIFICATION_ID = 6526;
	
	/* Declare headsetSwitch variable */
	private int headsetSwitch = 1;
	
	/* Handler Object to send updates to the UI */
	private final Handler handler = new Handler();
	
	/* Variables for SeekBar Progress */
	int audioCurrentPosition;
	int audioTotalDuration;
	
	/* To Inform The Activity for End of The PodCast Media */
	private static int endOfFile;
	
	/* Intent to send Currently Playing Audio Updates from Service To Activity */
	private Intent broadcastDataUpdateIntent;
	
	private int bufferedPercentage;
	
	/* Variables for Phone State Listener */
	private boolean isPausedInCall = false;
	private PhoneStateListener phoneStateListener;
	private TelephonyManager telephonyManager;

	/* If HeadSet gets unplugged, Stop PodCast and Service. */
	private BroadcastReceiver headsetReceiver = new BroadcastReceiver() {
		
		private boolean headsetConnected = false;
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.hasExtra("state")) {
				if (headsetConnected && intent.getIntExtra("state", 0) == 0) {
					headsetConnected = false;
					headsetSwitch = 0;
				} else if (!headsetConnected && intent.getIntExtra("state", 0) == 1) {
					headsetConnected = true;
					headsetSwitch = 1;
				}
			}

			/*
			 * Switch Case According to HeadSet. Value 0 --> Disconnected. Value
			 * 1 --> Connected.
			 */
			switch (headsetSwitch) {
			case (0):
				headsetDisconnected();
				break;
			case (1):
				break;
			}
		}
	};
	
	/**
	 * Receiver for seek bar position changed by User in the Activity
	 */
	private BroadcastReceiver commandReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			setCommand(intent);
		}
	};

	/**
	 * Sets the Command "play", "pause", "stop", "seek"
	 * 
	 * @param intent
	 *            Data Holder for Command String and Extra Data
	 */
	private void setCommand(Intent intent) {

		String command = intent.getStringExtra(AudioConstants.AUDIO_PLAYER_COMMAND);

		if (command.equals(AudioConstants.AUDIO_PLAYER_COMMAND_PLAY)) {

			boolean isDeviceSamsung = intent.getBooleanExtra(AudioConstants.IS_DEVICE_SAMSUNG, false);
			
			if (isDeviceSamsung) {
				playAudioFromCacheFolder((AudioObject) intent.getExtras().get(AudioConstants.AUDIO_ITEM));
			} else {
				playAudio((AudioObject) intent.getExtras().get(AudioConstants.AUDIO_ITEM));
			}
			
		} else if (command.equals(AudioConstants.AUDIO_PLAYER_COMMAND_PAUSE)) {
			
			pauseMedia();
			
		} else if (command.equals(AudioConstants.AUDIO_PLAYER_COMMAND_STOP)) {
			
			stopMediaPlayer();
			
		} else {
			
			// Do Nothing
		}
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		/* Register HeadSet Receiver */
		registerReceiver(headsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
		
		/* Register Command Receiver of the Service */
		registerReceiver(commandReceiver, new IntentFilter(AudioConstants.BROADCAST_COMMAND_CHANNEL));
		
		/* Listen Incoming Call Events */
		setUpPhoneStateListener();
		
		/* Set up intent for broadcasting Audio updates */
		broadcastDataUpdateIntent = new Intent(AudioConstants.BROADCAST_AUDIO_UPDATE_CHANNEL);
		
		// Register the listener with the telephony manager
		telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		
		// We can Show Notification for Playing Audio
		// initNotification();

		return START_STICKY;
	}
	
	/**
	 * Plays audio file in Applications Cache Folder with the hash code of
	 * DialectItem object Audio URL
	 * 
	 * @param audioObject
	 *            Audio Object to play.
	 */
	private void playAudioFromCacheFolder(AudioObject audioObject) {
		
		if ((currentAudioObject != null) && (currentAudioObject.equals(audioObject))) {
			
			// Same VideoPodcastItem Object
			if (!mediaPlayer.isPlaying()) {
				startMediaPlayer();
			}
			
		} else {

			// New Media Received
			// Reset Player
			// Prepare
			// Then play on onPrepared
			
			this.currentAudioObject = audioObject;
			
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			
			String audioFileName = "" + audioObject.getAudioUrl().hashCode();
			
			try {
				
				File file = new File(getCacheDir(), audioFileName);
				String path = file.getAbsolutePath();
				
				mediaPlayer.setDataSource(path);
				mediaPlayer.prepareAsync();
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {				
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Plays the Media Item if the one is Received
	 * 
	 * @param audioObject
	 */
	private void playAudio(AudioObject audioObject) {
		
		if ((currentAudioObject != null) && (currentAudioObject.equals(audioObject))) {
			
			// Same VideoPodcastItem Object
			if (mediaPlayer.isPlaying()) {
				// Do Nothing
			} else {
				startMediaPlayer();
			}
			
		} else {
			
			// New Media Received
			// Reset Player
			// Prepare
			// Then play on onPrepared
			
			this.currentAudioObject = audioObject;
			
			mediaPlayer.reset();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			
			try {

				mediaPlayer.setDataSource(audioObject.getAudioUrl());
				mediaPlayer.prepareAsync();

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Pauses Media Player If It's playing
	 */
	public void pauseMedia() {
		
		if (mediaPlayer.isPlaying()) {
			
			mediaPlayer.pause();
			startMediaProgressBroadcast();
			
			/* Cancel Notification When the Audio is Stopped */
			cancelNotification();
		}
	}
	
	/**
	 * Starts Media Player If It's not playing
	 */
	public void startMediaPlayer() {
		if (!mediaPlayer.isPlaying()) {
			mediaPlayer.start();
			
			/* Set EndOfFile variable to 0 since the Audio is Just Started ! */
			endOfFile = 0;
			
			startMediaProgressBroadcast();
			
			/*
			 * Push Notification to the Notifiaction Center when the Audio
			 * Starts
			 */
			initNotification();
		}
	}
	
	/**
	 * Sets up the Handler to send Info back to Activity
	 */
	private void startMediaProgressBroadcast() {
		handler.removeCallbacks(sendUpdatesToUI);
		handler.postDelayed(sendUpdatesToUI, 1000);
	}
	
	/**
	 * Sends Updates of SeekBar Progress to the Activity's UI
	 */
	private Runnable sendUpdatesToUI = new Runnable() {
		public void run() {
			clearIntentExtras(broadcastDataUpdateIntent);
			broadcastMediaProgress();
			handler.postDelayed(this, 1000);
		}
	};
	
	/**
	 * Clears all of the Extras have put the intent
	 * 
	 * @param intent	Intent Object to Clear it's Extras
	 */
	private void clearIntentExtras(Intent intent) {
		intent.replaceExtras(new Bundle());
	}
	
	/**
	 * Stops Broadcasting to the UI
	 */
	private void stopBroadcasting() {
		handler.removeCallbacks(sendUpdatesToUI);
	}
	
	/**
	 * Broadcasts the Data Within the Intent Activities which wants to receive
	 * updates from this Service have to register the Broadcast Receiver
	 */
	private void broadcastMediaProgress() {
		
		AudioStatus audioStatus = AudioPlayerManager.getAudioPlayerManagerInstance(this).getAudioStatus();
		
		if ((audioStatus == AudioStatus.PLAYING) || (audioStatus == AudioStatus.PAUSED)) {
			/* Current time of Podcast media */
			audioCurrentPosition = mediaPlayer.getCurrentPosition();
			
			/* Total Duration of Podcast media */
			audioTotalDuration = mediaPlayer.getDuration();
			
			/* Put Info to the Intent */
			broadcastDataUpdateIntent.putExtra(AudioConstants.AUDIO_PLAYER_CURRENT_POSITION, String.valueOf(audioCurrentPosition));
			broadcastDataUpdateIntent.putExtra(AudioConstants.AUDIO_PLAYER_TOTAL_DURATION, String.valueOf(audioTotalDuration));
			broadcastDataUpdateIntent.putExtra(AudioConstants.AUDIO_PLAYER_BUFFER_PERCENTAGE, bufferedPercentage);
			broadcastDataUpdateIntent.putExtra(AudioConstants.AUDIO_PLAYER_END_OF_FILE, String.valueOf(endOfFile));
			broadcastDataUpdateIntent.putExtra(AudioConstants.AUDIO_ITEM, this.currentAudioObject);
			
			broadcastDataUpdateIntent.putExtra(AudioConstants.AUDIO_PLAYER_COMMAND_STOP, false);
			
			sendBroadcast(broadcastDataUpdateIntent);
		}
	}
	
	/**
	 * Stops and Releases Media Player When the Head Set is Plugged Out
	 */
	private void headsetDisconnected() {
		stopMediaPlayer();
	}
	
	/**
	 * Stops Media Player If It's playing
	 */
	public void stopMediaPlayer() {
		if (mediaPlayer.isPlaying()) {
			
			/* Set the Variable to 0 since its stopped ! */
			endOfFile = 1;
			
			mediaPlayer.stop();
		}
		
		/* Cancel Notification When the Audio is Stopped */
		cancelNotification();
		
		/* Send Finish Command */
		sendFinishCommand();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		/* New Instance of Player */
		mediaPlayer = new MediaPlayer();
		
		/* Set Media Player Listeners */
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.reset();
		
		/* Register HeadSet Receiver */
		registerReceiver(headsetReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
		
		/* Initialise VideoPodcastItem as NULL */
		currentAudioObject = null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int arg1) {
		bufferedPercentage = arg1;
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		
		if (NetworkUtils.isConnected(this)) {
			
			switch (what) {
			case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
				Toast.makeText(this, "MEDIA ERROR NOT VALID FOR PROGRESSIVE PLAYBACK " + extra, Toast.LENGTH_SHORT).show();
				break;
			case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
				Toast.makeText(this, "MEDIA ERROR SERVER DIED " + extra, Toast.LENGTH_SHORT).show();
				break;
			case MediaPlayer.MEDIA_ERROR_UNKNOWN:
				Toast.makeText(this, "MEDIA ERROR UNKNOWN " + extra, Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(this, "AN ERROR OCCURED, EXTRA:  " + extra + " , WHAT: " + what, Toast.LENGTH_SHORT).show();
				break;
			}
			
		} else {
			
			Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
		}
		
		return true;
	}
	
	@Override
	/**
	 * Called When the Media Player is Ready To Play the PodCast Media
	 */
	public void onPrepared(MediaPlayer mp) {
		startMediaPlayer();
	}
	
	@Override
	/**
	 * Called When the PodCast Media is Finished
	 * Stops Media Player and Service
	 */
	public void onCompletion(MediaPlayer mp) {
		stopMediaPlayer();
		sendFinishCommand();
	}
	
	private void sendFinishCommand() {
		
		broadcastDataUpdateIntent.putExtra(AudioConstants.AUDIO_PLAYER_COMMAND_STOP, true);
		sendBroadcast(broadcastDataUpdateIntent);
		
		/* Set Audio Player STOPPED ! */
		AudioPlayerManager.getAudioPlayerManagerInstance(this).setAudioStatus(AudioStatus.STOPPED);
		
		/* Stop Broadcasting */
		stopBroadcasting();
	}

	@Override
	/**
	 * Cancels The Notification in Notification Center
	 * When the App is killed.
	 */
	public void onTaskRemoved(Intent rootIntent) {
		super.onTaskRemoved(rootIntent);
		
		cancelNotification();
	}

	/**
	 * Sets up Phone State Listener to Manage incoming Phone Calls Pause Media
	 * Player on Incoming call and Resume on Hang up
	 */
	private void setUpPhoneStateListener() {
		
		telephonyManager = (TelephonyManager) getSystemService(MainActivity.TELEPHONY_SERVICE);
		phoneStateListener = new PhoneStateListener() {
			
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				
				switch (state) {
				
				case TelephonyManager.CALL_STATE_OFFHOOK:
					
				case TelephonyManager.CALL_STATE_RINGING:
					
					if (mediaPlayer != null) {
						pauseMedia();
						isPausedInCall = true;
					}
					break;
					
				case TelephonyManager.CALL_STATE_IDLE:

					/* Phone is in IDLE State Start Playing */
					if (mediaPlayer != null) {
						if (isPausedInCall) {
							isPausedInCall = false;
							startMediaPlayer();
						}
					}
					break;
				}
			}
		};
	}

	/*
	 * Stop media player and release Stop phoneStateListener and Receivers
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
		}

		if (phoneStateListener != null) {
			telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		}

		/* Cancel the notification */
		// cancelNotification();

		/* Unregister HeadSet Receiver */
		unregisterReceiver(headsetReceiver);

		/* Unregister Command Receiver */
		unregisterReceiver(commandReceiver);

		// Stop the seekbar handler from sending updates to UI
		handler.removeCallbacks(sendUpdatesToUI);
	}

	/**
	 * Cancels the Notification with Given ID
	 */
	private void cancelNotification() {
		String ns = MainActivity.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		mNotificationManager.cancel(NOTIFICATION_ID);
	}

	/**
	 * Creates Notification in Notification Service
	 */
	private void initNotification() {
		String ns = MainActivity.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "Podcast Service";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, 0);
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		Context context = getApplicationContext();
		
		if (this.currentAudioObject != null) {
			
			String title = currentAudioObject.getTitle();
			String content = currentAudioObject.getSubTitle();
			
			Intent notificationIntent = new Intent(this, MainActivity.class);
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			notification.setLatestEventInfo(context, title, content, contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, notification);
		}
	}
}
