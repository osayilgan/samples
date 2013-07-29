package okan.apps.samples.sampleapplication2.audio;

import okan.apps.samples.sampleapplication2.audio.AudioConstants.AudioStatus;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;

public class AudioPlayerManager {
	
	/* Intent to send data to the Audio Service */
	private Intent audioPlayerDataIntent;
	
	/* Audio Player Status */
	private static AudioStatus audioStatus;
	
	private static AudioPlayerManager player;
	
	/* Aegon Audio Player Interface */
	private AudioPlayerInterface waddenAudioPlayerInterface;
	
	private Context context;
	
	private String deviceName;
	
	private static AudioObject currentlyPlayingAudio;
	
	private static ProgressBar progressBar;
	
	private static boolean isAudioDownloading;
	
	private AudioPlayerManager(Context context) {
		
		this.context = context;
		
		audioStatus = AudioStatus.NONE;
		
		this.waddenAudioPlayerInterface = (AudioPlayerInterface) context;
		
		audioPlayerDataIntent = new Intent(AudioConstants.BROADCAST_COMMAND_CHANNEL);
		
		/* Initialise Manufacturer Name */
		setDeviceManufacturer(Build.MANUFACTURER);
	}
	
	public static AudioPlayerManager getAudioPlayerManagerInstance(Context context) {
		if (player != null) {
			return player;
		} else {
			player = new AudioPlayerManager(context);
			return player;
		}
	}
	
	/**
	 * Retrieves the Current Playing Audio Object.
	 * 
	 * @return	Current Playing Audio Item's Instance.
	 */
	public AudioObject getCurrentPlayingAudio() {
		return this.currentlyPlayingAudio;
	}
	
	/**
	 * Sets the NAME of the Manufacturer e.g SAMSUNG, Acer, Motorola ...
	 * @param deviceName	String, name of the device
	 */
	private void setDeviceManufacturer(String deviceName) {
		this.deviceName = deviceName;
	}
	
	/**
	 * Audio Player's Status
	 * 
	 * @return Playing, Paused, Stopped, None
	 */
	public AudioStatus getAudioStatus() {
		return audioStatus;
	}
	
	/**
	 * Sets the AudioStatus of AudioPlayer Service
	 * @param audioStatus		Status to Set (PLAYING, PAUSED, STOPPED, NONE)
	 */
	public void setAudioStatus(AudioStatus audioStatus) {
		this.audioStatus = audioStatus;
	}
	
	/**
	 * Sets Currently Playing Audio
	 * @param videoPodcastItem	Audio Element (VideoPodcastItem Object)
	 */
	public void setCurrentPlayingAudio(AudioObject audioObject) {
		this.currentlyPlayingAudio = audioObject;
	}
	
	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}
	
	public ProgressBar getProgressBar() {
		return this.progressBar;
	}
	
	public void setDownloadingAudio(boolean isDownloading) {
		this.isAudioDownloading = isDownloading;
	}
	
	public boolean getDownloadingAudioStatus() {
		return this.isAudioDownloading;
	}
	
	/******************************************************************
	 * 
	 * AUDIO PLAYER METHODS
	 *  
	 ******************************************************************/
	
	/**
	 * Sends Play Command to Audio Player Service
	 * 
	 * @param dialect				Dialect Item
	 * @param isDeviceSamsung		The position of clicked item in the list
	 */
	public void play(AudioObject audioObject, boolean isDeviceSamsung) {
		
		clearIntentExtras(audioPlayerDataIntent);
		audioPlayerDataIntent.putExtra(AudioConstants.AUDIO_ITEM, audioObject);
		audioPlayerDataIntent.putExtra(AudioConstants.AUDIO_PLAYER_COMMAND, AudioConstants.AUDIO_PLAYER_COMMAND_PLAY);
		audioPlayerDataIntent.putExtra(AudioConstants.IS_DEVICE_SAMSUNG, isDeviceSamsung);
		
		context.sendBroadcast(audioPlayerDataIntent);
		
		setAudioStatus(AudioStatus.PLAYING);
		
		/* Update current VideoPodcast Item */
		setCurrentPlayingAudio(audioObject);
		
		/* Audio Player Started */
		waddenAudioPlayerInterface.onAudioPlayerStarted(audioObject);
	}
	
	/**
	 * Pauses the currently playing audio
	 */
	public void pause() {
		
		clearIntentExtras(audioPlayerDataIntent);
		audioPlayerDataIntent.putExtra(AudioConstants.AUDIO_PLAYER_COMMAND, AudioConstants.AUDIO_PLAYER_COMMAND_PAUSE);
		context.sendBroadcast(audioPlayerDataIntent);
		
		setAudioStatus(AudioStatus.PAUSED);
		
		/* Audio Player Paused */
		waddenAudioPlayerInterface.onAudioPlayerPaused();
	}
	
	/**
	 * Stops currently playing audio, and stops streaming
	 */
	public void stop() {
		clearIntentExtras(audioPlayerDataIntent);
		audioPlayerDataIntent.putExtra(AudioConstants.AUDIO_PLAYER_COMMAND, AudioConstants.AUDIO_PLAYER_COMMAND_STOP);
		context.sendBroadcast(audioPlayerDataIntent);
		
		audioStatus = AudioStatus.STOPPED;
	}
	
	/**
	 * Clears all of the Extras have put the intent
	 * 
	 * @param intent	Intent Object to Clear it's Extras
	 */
	private void clearIntentExtras(Intent intent) {
		intent.replaceExtras(new Bundle());
	}
}
