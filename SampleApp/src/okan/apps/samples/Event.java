package okan.apps.samples;

public class Event {
	
	private long id;
	private String eventDate;
	private String name;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAlarmDate() {
		return eventDate;
	}
	public void setAlarmDate(String alarmDate) {
		this.eventDate = alarmDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}