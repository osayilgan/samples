package okan.apps.samples.sampleapplication2.audio;

/**
 * Class to keep constant Values.
 * 
 * @author Okan SAYILGAN
 */
public class AudioConstants {
	
	public static final String IS_DEVICE_SAMSUNG = "isDeviceSamsung";
	
	public static final String AUDIO_ITEM = "audioItem";
	
	public static final String AUDIO_PLAYER_COMMAND = "command";
	
	public static final String AUDIO_PLAYER_COMMAND_PLAY = "play";
	
	public static final String AUDIO_PLAYER_COMMAND_PAUSE = "pause";
	
	public static final String AUDIO_PLAYER_COMMAND_STOP = "stop";
		
	public static final String AUDIO_PLAYER_CURRENT_POSITION = "audio_player_current_position";
	
	public static final String AUDIO_PLAYER_TOTAL_DURATION = "audio_player_total_duration";
	
	public static final String AUDIO_PLAYER_BUFFER_PERCENTAGE = "audio_player_buffer_percentage";
	
	public static final String AUDIO_PLAYER_END_OF_FILE = "audio_player_end_of_file";
	
	public static enum AudioStatus {
		PLAYING, PAUSED, NONE, STOPPED
	}
	
	/** Broadcast for sending the Updates from Activity to WaddenAudioPlayer */
	public static final String BROADCAST_COMMAND_CHANNEL = "okan.apps.samples.sampleapplication2.audio.sendcommand";
	
	/** Broadcast for sending the Audio Data Updates from Service to Activity */
	public static final String BROADCAST_AUDIO_UPDATE_CHANNEL = "okan.apps.samples.sampleapplication2.audio.audioprogress";
}
