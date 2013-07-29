package okan.apps.samples.sampleapplication2.models;

/**
 * Data Model for Media Type of Objects
 * 
 * @author Okan SAYILGAN
 */
public class CompanyMedia {
	
	/** ID for DB */
	private long id;
	
	/** Media TYPE. e.g. image, video etc... */
	private String type;
	
	private String url;
	private long parentId;
	
	public CompanyMedia(long id, String type, String url, long parentId) {
		
		this.id = id;
		this.type = type;
		this.url = url;
		this.parentId = parentId;
	}
	
	public long getParentId() {
		return this.parentId;
	}
	
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o instanceof CompanyMedia) {
			return (((CompanyMedia)o).getId() == this.id);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) this.id;
	}
}
