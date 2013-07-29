package okan.apps.samples.sampleapplication2.audio;

public interface AudioPlayerInterface {
	
	public void onAudioPlayerStarted(AudioObject audioObject);
	public void onAudioPlayerPaused();
	public void onAudioStopped();
}
