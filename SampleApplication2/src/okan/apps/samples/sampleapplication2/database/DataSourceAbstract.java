package okan.apps.samples.sampleapplication2.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Abstract Class to handle Common database operations.
 * 
 * @author Okan SAYILGAN
 */
public abstract class DataSourceAbstract {
	
	private SQLiteDatabase database;
	private DBSQLiteHelper dbHelper;
	
	private String tableName;
	private String[] columns;
	
	private Context context;
	
	public DataSourceAbstract(Context context, String tableName, String[] columns) {
		
		this.tableName = tableName;
		this.columns = columns;
		this.context = context;
		
		dbHelper = DBSQLiteHelper.getDBHelperInstance(context);
		open();
	}
	
	/**
	 * Retrieves the Context object from parent class
	 * 
	 * @return Activity Context
	 */
	public Context getDataSourceContext() {
		return this.context;
	}
	
	/**
	 * Retrieves the database from parent class
	 * 
	 * @return database instance
	 */
	public SQLiteDatabase getDatabaseInstance() {
		return this.database;
	}
	
	/**
	 * Checks if the Given Object is already in the Related table Updates the
	 * Object if Exists, Creates new Row if it doesn't
	 * 
	 * @param object
	 *            Object to insert to the Database
	 */
	public abstract long createOrUpdateObject(Object object);
	
	/**
	 * Method to retrieve related Object from given Cursor
	 * 
	 * @param cursor
	 *            Cursor to parse
	 * @return Child Object which extends this Class
	 */
	public abstract Object cursorToObject(Cursor cursor);
	
	/**
	 * Inserts the Rows given in the Content Values.
	 * 
	 * @param values
	 *            Values contains Column Name-Value pair.
	 */
	public long insertRow(ContentValues values) {
		return database.insert(this.tableName, null, values);
	}
	
	/**
	 * inserts the Rows given in the Content Values.
	 * Uses transaction.
	 * 
	 * @param values  Values contains Column Name-Value pair.
	 */
	public void insertRows(ContentValues[] values) {
		database.beginTransaction();
		try {
			for (ContentValues value : values) {
				database.insert(this.tableName, null, value);
			}
			database.setTransactionSuccessful();
		} finally {
			database.endTransaction();
		}
	}
	
	/**
	 * Retrieves the Row as Object with the Given Row ID
	 * 
	 * @param rowId ROW ID of the Object
	 * @return new Object Instance if the Row Exists, Null otherwise.
	 */
	public Object getObjectWithId(long rowId) {
		
		Cursor cursor = database.query(this.tableName, this.columns, DBSQLiteHelper.COLUMN_ID + " = ? ", new String[] { "" + rowId }, null, null, null);
		
		Object object = null;
		
		if (cursor.moveToFirst()) {
			object = cursorToObject(cursor);
			cursor.moveToNext();
		}
		
		// Make sure to close the cursor
		cursor.close();
		
		return object;
	}
	
	/**
	 * Retrieves all of the rows as List of Object instances from Related TABLE
	 * 
	 * @return List of Objects
	 */
	public ArrayList<Object> getAllObjects() {
		
		ArrayList<Object> objects = new ArrayList<Object>();
		
		Cursor cursor = database.query(this.tableName, this.columns, null, null, null, null, null);
		
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			Object object = cursorToObject(cursor);
			objects.add(object);
			cursor.moveToNext();
		}
		
		// Make sure to close the cursor
		cursor.close();
		
		return objects;
	}
	
	/**
	 * Deletes the Row with given insert ID from Table
	 * 
	 * @param rowId
	 *            ID of the Object That will be removed from Related Table
	 */
	public void deleteObject(long rowId) {
		database.delete(this.tableName, DBSQLiteHelper.COLUMN_ID + " = ? ", new String[] { "" + rowId });
	}
	
	/**
	 * Deletes All of the Rows in the Table.
	 */
	public void deleteAllObjects() {
		database.delete(this.tableName, null, null);
	}
	
	/**
	 * Updates the Row with the given ID
	 * 
	 * @param rowId
	 *            insert ID of the Row
	 * @param values
	 *            Content Values to update in the given Table
	 * @return true if the row exists, false otherwise
	 */
	public boolean updateRow(long rowId, ContentValues values) {
		
		int updatedRowNumber = database.update(this.tableName, values, DBSQLiteHelper.COLUMN_ID + " ='" + rowId + "'", null);
		
		/*
		 * If the number of rows affected is greater than 0, it means the row
		 * with the given ID exists
		 */
		return (updatedRowNumber > 0) ? true : false;
	}
	
	/**
	 * To check if the row with the given id exists
	 * 
	 * @param rowId
	 *            ID of the Row that will be checked
	 * @return True if the row exists, false otherwise
	 */
	public boolean isRowExists(long rowId) {
		
		Cursor cursor = database.query(this.tableName, this.columns, DBSQLiteHelper.COLUMN_ID + " = ? ", new String[] { "" + rowId }, null, null, null);
		
		int numOfRows = cursor.getCount();
		cursor.close();
		
		return (numOfRows > 0) ? true : false;
	}
	
	/**
	 * Inserts Place holders to Create SQLite Statement for IN Clause.
	 * 
	 * @param len
	 *            Length of the Array
	 * @return String with ? after each element in Array
	 */
	public String makePlaceholders(int len) {
		if (len < 1) {
			// It will lead to an invalid query anyway ..
			throw new RuntimeException("No placeholders");
		} else {
			StringBuilder sb = new StringBuilder(len * 2 - 1);
			sb.append("?");
			for (int i = 1; i < len; i++) {
				sb.append(",?");
			}
			return sb.toString();
		}
	}
	
	/**
	 * Opens DataBase to proceed
	 * 
	 * @throws SQLException
	 */
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	/**
	 * Closes Recently opened DataBase
	 */
	public void close() {
		dbHelper.close();
	}
}