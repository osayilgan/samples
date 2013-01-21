package okan.apps.samples;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_EVENTS = "events";
	
	public static final String COLUMN_ID = "_id";
	
	public static final String COLUMN_ALARM_DATE = "alarm_date";
	public static final String COLUMN_NAME = "name";
	
	private static final String EVENTS_DATABASE_NAME = "events.db";
	
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	private static final String EVENTS_DATABASE_CREATE = "create table " + TABLE_EVENTS + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " +
			COLUMN_NAME + " text not null, " +
			COLUMN_ALARM_DATE + " text not null);";
	
	public SQLiteHelper(Context context) {
		super(context, EVENTS_DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(EVENTS_DATABASE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		onCreate(db);
	}
}