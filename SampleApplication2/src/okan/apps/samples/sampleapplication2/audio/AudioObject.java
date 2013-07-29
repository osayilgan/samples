package okan.apps.samples.sampleapplication2.audio;

import java.io.Serializable;

import okan.apps.samples.sampleapplication2.models.Company;

/**
 * Object contains Audio information.
 * 
 * @author Okan SAYILGAN
 */
public class AudioObject implements Serializable {
	
	private long id;
	private String title;
	private String subTitle;
	private String audioUrl;
	
	public AudioObject(long id, String title, String subTitle, String audioUrl) {
		
		this.id = id;
		this.title = title;
		this.subTitle = subTitle;
		this.audioUrl = audioUrl;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSubTitle() {
		return subTitle;
	}
	
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	
	public String getAudioUrl() {
		return audioUrl;
	}
	
	public void setAudioUrl(String audioUrl) {
		this.audioUrl = audioUrl;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o instanceof AudioObject) {
			return (((AudioObject)o).getId() == this.id);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) this.id;
	}
}
