package okan.apps.samples;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EventsDataSource {
	
	private SQLiteDatabase database;
	private SQLiteHelper dbHelper;
	private String[] allColumns = { SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_NAME,
			SQLiteHelper.COLUMN_ALARM_DATE };
	
	public EventsDataSource(Context context) {
		dbHelper = new SQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public long createEvent(String eventName, String eventDate) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_NAME, eventName);
		values.put(SQLiteHelper.COLUMN_ALARM_DATE, eventDate);
		
		long insertId = database.insert(SQLiteHelper.TABLE_EVENTS, null, values);
		
		return insertId;
	}
	
	public void deleteEvent(Event event) {
		long id = event.getId();
		System.out.println("Event deleted with id: " + id);
		database.delete(SQLiteHelper.TABLE_EVENTS, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public List<Event> getAllAlarms() {
		
		List<Event> events = new ArrayList<Event>();
		
		Cursor cursor = database.query(SQLiteHelper.TABLE_EVENTS, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Event event = cursorToAlarm(cursor);
			events.add(event);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return events;
	}
	
	public Event cursorToAlarm(Cursor cursor) {
		
		Event event = new Event();
		event.setId(cursor.getLong(0));
		event.setName(cursor.getString(1));
		event.setAlarmDate(cursor.getString(2));
		
		return event;
	}
}
