package okan.apps.samples.sampleapplication2.models;

import okan.apps.samples.sampleapplication2.collections.FavoriteCollection;

/**
 * Favorite Model Class
 * 
 * @author Okan SAYILGAN
 */
public class Favorite {
	
	/** Unique ID */
	private long id;
	
	/** Time Stamp of the Favorite Element added to Favorite Table */
	private String date;
	
	/** table name of the referenced Object */
	private String referenceObjectTableName;
	
	/** ID Of the Object from its own Table */
	private long referenceObjectId;
	
	/**
	 * Constructor with ID Field returned from Database
	 */
	public Favorite(long id, String date, String referenceObjectTableName, long referenceObjectId) {
		this.id = id;
		this.date = date;
		this.referenceObjectTableName = referenceObjectTableName;
		this.referenceObjectId = referenceObjectId;
	}
	
	/**
	 * Constructor with ID Field returned from Database
	 */
	public Favorite(long id, String date, FavoriteCollection externalObject) {
		this.id = id;
		this.date = date;
		this.referenceObjectTableName = externalObject.getTableName();
		this.referenceObjectId = externalObject.getId();
	}
	
	/**
	 * Constructor without ID Field.
	 * 
	 * This Constructor can be Used when its created Manually to save into database
	 * Since the insert ID is not passed as parameter 
	 */
	public Favorite(String date, FavoriteCollection externalObject) {
		this.date = date;
		this.referenceObjectTableName = externalObject.getTableName();
		this.referenceObjectId = externalObject.getId();
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getReferenceObjectTableName() {
		return referenceObjectTableName;
	}
	
	public void setReferenceObjectTableName(String referenceObjectTableName) {
		this.referenceObjectTableName = referenceObjectTableName;
	}
	
	public long getReferenceObjectId() {
		return referenceObjectId;
	}
	
	public void setReferenceObjectId(long referenceObjectId) {
		this.referenceObjectId = referenceObjectId;
	}
}