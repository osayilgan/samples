package okan.apps.samples.sampleapplication2.database;

import java.util.ArrayList;
import okan.apps.samples.sampleapplication2.models.CompanyMedia;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Data Source Class for database operations on Company Media Table.
 * 
 * @author Okan SAYILGAN
 */
public class CompanyMediaDataSource extends DataSourceAbstract {
	
	private static final String tableName = DBSQLiteHelper.TABLE_COMPANY_MEDIA;
	
	public static final String[] columns = { 
		DBSQLiteHelper.COLUMN_ID,
		DBSQLiteHelper.COLUMN_COMPANY_MEDIA_TYPE,
		DBSQLiteHelper.COLUMN_COMPANY_MEDIA_URL,
		DBSQLiteHelper.COLUMN_COMPANY_MEDIA_PARENT_ID };
	
	public CompanyMediaDataSource(Context context) {
		super(context, tableName, columns);
	}
	
	@Override
	public long createOrUpdateObject(Object object) {
		
		CompanyMedia companyMedia = (CompanyMedia) object;
		
		long id = companyMedia.getId();
		String type = companyMedia.getType();
		String url = companyMedia.getUrl();
		long parentId = companyMedia.getParentId();
		
		ContentValues values = new ContentValues();
		values.put(DBSQLiteHelper.COLUMN_COMPANY_MEDIA_TYPE, type);
		values.put(DBSQLiteHelper.COLUMN_COMPANY_MEDIA_URL, url);
		values.put(DBSQLiteHelper.COLUMN_COMPANY_MEDIA_PARENT_ID, parentId);
		
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
	 * Deletes the Row with the Given Row ID
	 * @param rowId	ID of the ROW that will be deleted
	 */
	public void deleteSubMediasWithParentId(long parentId) {
		super.getDatabaseInstance().delete(tableName, 
				DBSQLiteHelper.COLUMN_COMPANY_MEDIA_PARENT_ID + " = ? ", new String[] {"" + parentId});
	}
	
	/**
	 * Retrieves All Company Medias Where given parent ID equals to CompanyMedia's parent ID.
	 * 
	 * @param parentId		Parent ID, Company Object ID
	 * @return				List of Company Medias.
	 */
	public ArrayList<CompanyMedia> getCompanyMediasWithParentId(long parentId) {
		
		ArrayList<CompanyMedia> companyMedias = new ArrayList<CompanyMedia>();
		
		Cursor cursor = super.getDatabaseInstance().query(tableName, columns, 
				DBSQLiteHelper.COLUMN_COMPANY_MEDIA_PARENT_ID + " = ? ", 
				new String[] {"" + parentId}, null, null, null);
		
		cursor.moveToFirst();
		
		while (!cursor.isAfterLast()) {
			CompanyMedia companyMedia = cursorToObject(cursor);
			companyMedias.add(companyMedia);
			cursor.moveToNext();
		}
		
		// Make sure to close the cursor
		cursor.close();
		
		return companyMedias;
	}
	
	@Override
	public CompanyMedia cursorToObject(Cursor cursor) {
		
		long id = cursor.getLong(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_ID));
		String type = cursor.getString(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_COMPANY_MEDIA_TYPE));
		String url = cursor.getString(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_COMPANY_MEDIA_URL));
		long parentId = cursor.getLong(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_COMPANY_MEDIA_PARENT_ID));
		
		return new CompanyMedia(id, type, url, parentId);
	}
}
