package okan.apps.samples.sampleapplication2.database;

import java.util.ArrayList;

import okan.apps.samples.sampleapplication2.collections.FavoriteCollection;
import okan.apps.samples.sampleapplication2.favorites.FavoriteManager;
import okan.apps.samples.sampleapplication2.models.Favorite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Data Source Class for database operations on Favorite and Related tables.
 * 
 * @author Okan SAYILGAN
 */
public class FavoriteDataSource extends DataSourceAbstract {
	
	private static final String tableName = DBSQLiteHelper.TABLE_FAVORITES;
	
	public static final String[] columns = { 
		DBSQLiteHelper.COLUMN_ID,
		DBSQLiteHelper.COLUMN_FAVORITES_DATE,
		DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_TABLE,
		DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_OBJECT_ID };
	
	public FavoriteDataSource(Context context) {
		super(context, tableName, columns);
	}
	
	@Override
	public long createOrUpdateObject(Object object) {
		
		Favorite favorite = (Favorite) object;
		
		long id = favorite.getId();
		String date = favorite.getDate();
		String referenceTable = favorite.getReferenceObjectTableName();
		long referenceId = favorite.getReferenceObjectId();
		
		ContentValues values = new ContentValues();
		values.put(DBSQLiteHelper.COLUMN_FAVORITES_DATE, date);
		values.put(DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_TABLE, referenceTable);
		values.put(DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_OBJECT_ID, referenceId);
		
		if (super.isRowExists(id)) {
			
			/* Update the Row if it is exists */
			super.updateRow(id, values);
			
		} else {
			
			values.put(DBSQLiteHelper.COLUMN_ID, id);
			super.insertRow(values);
		}
		
		return id;
	}
	
	/**
	 * Retrieves All of the Favorites from Database
	 * 
	 * @return List of Favorite Objects
	 */
	public ArrayList<Favorite> getAllFavorites() {
		
		ArrayList<Favorite> favoriteList = new ArrayList<Favorite>();
		
		for (Object object : super.getAllObjects()) {
			favoriteList.add((Favorite) object);
		}
		
		return favoriteList;
	}
	
	/**
	 * Checks whether the Object is added to Favorite Database.
	 * 
	 * @param externalTableName
	 *            Table Name of the External Object added to the Favorites
	 *            Table.
	 * @param externalObjectId
	 *            Insert ID of the External Object in its own Table.
	 * @return True if the Object added to the Database, False otherwise.
	 */
	public boolean isObjectExist(String externalTableName, long externalObjectId) {
		
		Cursor cursor = super.getDatabaseInstance().query(tableName, columns,
				DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_OBJECT_ID + " = ? AND " + 
				DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_TABLE + " = ? ",
				new String[] { "" + externalObjectId, externalTableName },
				null, null, null);
		
		return (cursor.getCount() > 0);
	}
	
	/**
	 * Retrieves the External Object Added to the Favorites Table.
	 * 
	 * @param favorite		Object added to the Favorites Table
	 * @return				Object implements Favorites Collection.
	 */
	public FavoriteCollection getReferenceObject(Favorite favorite) {
		
		String referenceObjectTableName = favorite.getReferenceObjectTableName();
		long referenceObjectId = favorite.getReferenceObjectId();
		
		Cursor cursor = super.getDatabaseInstance().query(
				referenceObjectTableName, null,
				DBSQLiteHelper.COLUMN_ID + " = ? ",
				new String[] { "" + referenceObjectId }, null, null, null);
		
		FavoriteCollection favoriteInterfaceObject = null;
		
		if (cursor.moveToFirst()) {
			/*
			 * This will get the Empty Instance first then will fill up the
			 * empty object with the values returned with cursor
			 */
			favoriteInterfaceObject = FavoriteManager.getInterfaceObjectWithTableName(referenceObjectTableName)
					.cursorToFavoriteInterfaceObject(super.getDataSourceContext(), cursor);
		}
		
		cursor.close();

		return favoriteInterfaceObject;
	}
	
	/**
	 * Deletes Favorite Object from Favorites Table based on Reference Object ID
	 * and Reference Object Table Name to remove the Object from Different
	 * Sections.
	 * 
	 * @param favorite
	 */
	public void deleteFavoriteObject(Favorite favorite) {
		
		String referenceTableName = favorite.getReferenceObjectTableName();
		long referenceObjectId = favorite.getReferenceObjectId();
		
		String whereClause = DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_OBJECT_ID + " = ? AND " + 
		DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_TABLE + " = ? ";
		String[] whereArgs = new String[] { "" + referenceObjectId, referenceTableName };

		super.getDatabaseInstance().delete(tableName, whereClause, whereArgs);
	}
	
	/**
	 * Deletes Favorite Object from Favorites Table based on Reference Object ID
	 * and Reference Object Table Name to remove the Object from Different
	 * Sections.
	 * 
	 * @param favorite
	 */
	public void deleteFavoriteObject(FavoriteCollection favoriteObject) {

		String referenceTableName = favoriteObject.getTableName();
		long referenceObjectId = favoriteObject.getId();

		String whereClause = DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_OBJECT_ID + " = ? AND " + 
		DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_TABLE + " = ? ";
		String[] whereArgs = new String[] { "" + referenceObjectId, referenceTableName };
		
		super.getDatabaseInstance().delete(tableName, whereClause, whereArgs);
	}

	
	@Override
	public Favorite cursorToObject(Cursor cursor) {
		
		long id = cursor.getLong(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_ID));
		String date = cursor.getString(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_FAVORITES_DATE));
		String referenceTable = cursor.getString(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_TABLE));
		long referenceId = cursor.getLong(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_FAVORITES_REFERENCE_OBJECT_ID));
		
		return new Favorite(id, date, referenceTable, referenceId);
	}
}
