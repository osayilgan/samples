package okan.apps.samples.sampleapplication2.database;

import java.util.ArrayList;

import okan.apps.samples.sampleapplication2.models.Company;
import okan.apps.samples.sampleapplication2.models.CompanyMedia;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Data Source Class for database operations on Company and Related tables.
 * 
 * @author Okan SAYILGAN
 */
public class CompanyDataSouce extends DataSourceAbstract {
	
	private static final String tableName = DBSQLiteHelper.TABLE_COMPANY;
	
	public static final String[] columns = { 
		DBSQLiteHelper.COLUMN_ID,
		DBSQLiteHelper.COLUMN_COMPANY_NAME,
		DBSQLiteHelper.COLUMN_COMPANY_DESCRIPTION,
		DBSQLiteHelper.COLUMN_COMPANY_EMAIL };
	
	/** Data Sources */
	CompanyMediaDataSource companyMediaDataSource;
	
	public CompanyDataSouce(Context context) {
		super(context, tableName, columns);
		
		companyMediaDataSource = new CompanyMediaDataSource(context);
	}
	
	@Override
	public long createOrUpdateObject(Object object) {
		
		Company company = (Company) object;
		
		long id = company.getId();
		String name = company.getName();
		String description = company.getDescription();
		String eMail = company.geteMail();
		
		ArrayList<CompanyMedia> companyMedias = company.getCompanyMedias();
		
		ContentValues values = new ContentValues();
		values.put(DBSQLiteHelper.COLUMN_COMPANY_NAME, name);
		values.put(DBSQLiteHelper.COLUMN_COMPANY_DESCRIPTION, description);
		values.put(DBSQLiteHelper.COLUMN_COMPANY_EMAIL, eMail);
		
		if (super.isRowExists(id)) {
			
			/* Update the Row if it is exists */
			super.updateRow(id, values);
			
			/* Delete Previous Company Medias */
			deleteCompanyMedias(id);
			
		} else {
			
			values.put(DBSQLiteHelper.COLUMN_ID, id);
			super.insertRow(values);
		}
		
		/* insert Company Medias */
		insertCompanyMedias(companyMedias, id);
		
		return id;
	}
	
	/**
	 * inserts the Company Medias to the Company Media Table
	 * 
	 * @param companyMediaList 	List of Company Medias
	 * @param companyId 		ID of the company that will be kept as reference in the Company Medias Table
	 */
	private void insertCompanyMedias(ArrayList<CompanyMedia> companyMediaList, long companyId) {
		/* Company Medias, Update OR Create */
		if ((companyMediaList != null) && (companyMediaList.size() > 0)) {
			for (int i = 0; i < companyMediaList.size(); i++) {
				companyMediaDataSource.createOrUpdateObject(companyMediaList.get(i));
			}
		}
	}

	/**
	 * Deletes all the Company Medias in Company Media Table where the Given
	 * company ID is equal to Company ID column in the ROWs
	 * 
	 * @param companyId
	 *            ID of the Company That will be removed
	 */
	private void deleteCompanyMedias(long companyId) {
		companyMediaDataSource.deleteSubMediasWithParentId(companyId);
	}
	
	@Override
	public Company cursorToObject(Cursor cursor) {
		
		long id = cursor.getLong(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_ID));
		String name = cursor.getString(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_COMPANY_NAME));
		String description = cursor.getString(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_COMPANY_DESCRIPTION));
		String eMail = cursor.getString(cursor.getColumnIndex(DBSQLiteHelper.COLUMN_COMPANY_EMAIL));
		
		ArrayList<CompanyMedia> companyMedias = companyMediaDataSource.getCompanyMediasWithParentId(id);
		
		return new Company(id, name, description, eMail, companyMedias);
	}
}
