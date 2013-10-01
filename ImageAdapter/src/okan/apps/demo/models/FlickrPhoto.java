package okan.apps.demo.models;

import org.json.JSONObject;

/**
 * Data Model Class to represent Photo Object from Flickr Web Service API
 * 
 * @author Okan SAYILGAN
 */
public class FlickrPhoto {
	
	/* Constant Strings for KEY Values, used for parsing JSON */
	public static final String key_id = "id";
	public static final String key_owner = "owner";
	public static final String key_secret = "secret";
	public static final String key_server = "server";
	public static final String key_farm = "farm";
	public static final String key_title = "title";
	public static final String key_isPublic = "ispublic";
	public static final String key_isFriend = "isfriend";
	public static final String key_isFamily = "isfamily";
	
	/* Photo ID */
	private long id;
	
	/* Photo Owner */
	private String owner;
	
	/* Secret code for Photo */
	private String secretCode;
	
	/* Photo Server ID */
	private String server;
	
	/* Photo Farm */
	private String farm;
	
	/* Photo Title String */
	private String title;
	
	/* Public indicator of Photo */
	private boolean isPuclic;
	
	/* Friend(Network) indicator of Photo */
	private boolean isFriend;
	
	/* Family indicator of Photo */
	private boolean isFamily;
	
	public FlickrPhoto(long id, String owner, String secretCode, String server, String farm,
			String title, boolean isPuclic, boolean isFriend, boolean isFamily) {
		
		this.id = id;
		this.owner = owner;
		this.secretCode = secretCode;
		this.server = server;
		this.farm = farm;
		this.title = title;
		this.isPuclic = isPuclic;
		this.isFriend = isFriend;
		this.isFamily = isFamily;
	}
	
	/**
	 * Builds a new URL to download the image with the information in Photo Object.
	 * 
	 * @return	Image URL.
	 */
	public String getImageUrl() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("http://farm");
		builder.append(this.farm);
		builder.append(".static.flickr.com/");
		builder.append(this.server);
		builder.append("/");
		builder.append(this.id);
		builder.append("_");
		builder.append(this.secretCode);
		builder.append("_m.jpg");
		
		return builder.toString();
	}
	
	/**
	 * Parses Flickr Photo Object.
	 * 
	 * @param jsonObject	JSON Object carrying information.
	 * @return				new Flickr Object.
	 */
	public static FlickrPhoto parseFlickrPhotoObject(JSONObject jsonObject) {
		
		long id = jsonObject.optLong(key_id);
		String owner = jsonObject.optString(key_owner);
		String secretCode = jsonObject.optString(key_secret);
		String server = jsonObject.optString(key_server);
		String farm = jsonObject.optString(key_farm);
		String title = jsonObject.optString(key_title);
		boolean isPuclic = jsonObject.optBoolean(key_isPublic);
		boolean isFriend = jsonObject.optBoolean(key_isFriend);
		boolean isFamily = jsonObject.optBoolean(key_isFamily);
		
		return new FlickrPhoto(id, owner, secretCode, server, farm, title, isPuclic, isFriend, isFamily);
	}
	
	/********************* Rest of Getters and Setters ************************/
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getSecretCode() {
		return secretCode;
	}
	
	public void setSecretCode(String secretCode) {
		this.secretCode = secretCode;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	public String getFarm() {
		return farm;
	}
	
	public void setFarm(String farm) {
		this.farm = farm;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isPuclic() {
		return isPuclic;
	}
	
	public void setPuclic(boolean isPuclic) {
		this.isPuclic = isPuclic;
	}
	
	public boolean isFriend() {
		return isFriend;
	}
	
	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	
	public boolean isFamily() {
		return isFamily;
	}
	
	public void setFamily(boolean isFamily) {
		this.isFamily = isFamily;
	}
}
