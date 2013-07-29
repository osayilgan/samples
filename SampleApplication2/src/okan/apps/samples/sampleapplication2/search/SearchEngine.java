package okan.apps.samples.sampleapplication2.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import okan.apps.samples.sampleapplication2.collections.SearchEngineCollection;
import okan.apps.samples.sampleapplication2.database.DBSQLiteHelper;
import okan.apps.samples.sampleapplication2.models.Company;
import okan.apps.samples.sampleapplication2.models.Student;

/**
 * Manager Class to Search in Database.
 * 
 * @author Okan SAYILGAN
 */
public class SearchEngine {
	
	private Context context;
	
	private SQLiteDatabase database;
	private DBSQLiteHelper dbHelper;
	
	public static interface SearchResultListener {
		
		/**
		 * Passes the Search Result When the Search is finished
		 * 
		 * @param searchResult		List of SearchResult Object which has Category Name and the Result Object
		 */
		void onSearchResult(ArrayList<SearchResult> searchResult);
		
		/**
		 * This method is called before starting to Search Process.
		 * This can be used to indicate the User about on going background process.
		 */
		void onPreExecute();
	}
	
	public SearchEngine(Context context) {
		this.context = context;
		
		dbHelper = DBSQLiteHelper.getDBHelperInstance(context);
		open();
	}
	
	/**
	 * Proceeds Search Query in All of the Related Tables Asynchronously in a background Thread.
	 * 
	 * @param searchWord	Word to Search in Database
	 * @param listener		Result Listener to listen Search Results. It can either be implemented to the class itself or used as Inner Type
	 */
	public void queryGeneralSearchAsync(final String searchWord, final SearchResultListener listener) {
		
		new AsyncTask<Void, Void, Void>() {
    		
    		ArrayList<SearchResult> resultMap = new ArrayList<SearchResult>();
    		
    		/** Hash Map to hold mapping between Table Name and the List of Columns linked to this Table Name */
    		HashMap<String, String[]> tableNameToColumnNameMapping = new HashMap<String, String[]>();
    		
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
				/* indicates that the Search is Just Started */
				// it can be used to show Loading Bar
				listener.onPreExecute();
			}
			
			@Override
			protected Void doInBackground(Void... params) {
				
				ArrayList<String> tableNames = getAllTableNames();
				
				/* Fill Up the Hash Map with Table Name and List of Columns */
				for (int i = 0; i < tableNames.size(); i++) {
        			Cursor cursor = database.query(tableNames.get(i), null, null, null, null, null, null);
        			tableNameToColumnNameMapping.put(tableNames.get(i), cursor.getColumnNames());
        			cursor.close();
        		}
				
				/*
        		 * Search Process ...
        		 * 
        		 * 1- Loop over every single Table in Hash Map
        		 * 2- Query to that table to get all the columns
        		 * 3- if the number of columns is greater than 0 then Generate Search Query
        		 * 4- After getting the Query Result, if there is at least one result in the Query
        		 * 	  	Then put it into HashMap to return in the end.
        		 * 
        		 * 5- Finally returnMap is going to include Table Name as KEY (if there is a search result from this table), 
        		 * 		and the List of Objects as VALUE regarding to that KEY.
        		 */
        		for (String key : tableNameToColumnNameMapping.keySet()) {
					
        			String tableName = key;
        			String[] columns = tableNameToColumnNameMapping.get(key);
        			
        			if(columns.length > 0) {
        				
        				Cursor cursor = database.query(tableName, null, generateSearchQueryForColumnNames(columns, searchWord), null, null, null, null, null);
						
						if (cursor.moveToFirst()) {
							
							ArrayList<SearchEngineCollection> resultList = new ArrayList<SearchEngineCollection>();
							
							while (!cursor.isAfterLast()) {
								
								SearchEngineCollection interfaceObject = getInterfaceObjectWithTableName(tableName);
								
								if (interfaceObject != null) {
									
									interfaceObject = interfaceObject.cursorToSearchEngineInterfaceObject(context, cursor);
									resultList.add(interfaceObject);
								}
								
								cursor.moveToNext();
							}
							
							/* If there is at least one result */
							if (resultList.size() > 0) {
								
								// Put Search Result Object into List with Table Name as Category Name
								// And the Search Engine Interface Object itself
								
								for (SearchEngineCollection searchEngineInterface : resultList) {
									
									resultMap.add(new SearchResult(searchEngineInterface, tableName));
								}
							}
						}
						
						// Make sure to close the cursor
						cursor.close();
        			}
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				
				/* Correct Table Names by splitting them by underscore */
				beautifyTableNames(resultMap);
				
				/* send result back to listener */
				listener.onSearchResult(resultMap);
			}
			
		}.execute();
	}
	
	/**
	 * Generates Query to search in the DB based on Columns and Word to search.
	 * 
	 * @param columnNames	Names of Columns to Search on.
	 * @param searchWord	Word to search.
	 * @return				Query to insert for searching.
	 */
	private String generateSearchQueryForColumnNames(String[] columnNames, String searchWord) {
		
		StringBuilder queryBuilder = new StringBuilder();
		
		String prefix = "( ";
		String suffix = " )";
		queryBuilder.append(prefix);
		
		for (int i = 1; i < columnNames.length; i++) {
			
			queryBuilder.append("'" + columnNames[i] + "' LIKE " + "'%" + searchWord + "%'");
			
			if(i != (columnNames.length-1)) {
				queryBuilder.append(" OR ");
			}
		}
		
		/* Add Suffix Here */
		queryBuilder.append(suffix);
		
		return queryBuilder.toString();
	}
	
	/**
	 * Retrieves the List Tables in our Database except "android_metadata".
	 * 
	 * @return		List of Table Names exist in our DB.
	 */
	private ArrayList<String> getAllTableNames() {
		
		ArrayList<String> tableNames = new ArrayList<String>();
		
		Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
		
		if (cursor.moveToFirst()) {
		    while ( !cursor.isAfterLast() ) {
		    	
		    	String tableName = cursor.getString(0);
		    	
		    	if (!tableName.equals("android_metadata")) {
		    		tableNames.add(tableName);
				}
		    	
		        cursor.moveToNext();
		    }
		}
		
		// Close cursor
		cursor.close();
		
		return tableNames;
	}
	
	/**
	 * Gets category names from search Result and splits them with "_".
	 * After split, Upper cases the first char of the Table Name.
	 * 
	 * @param resultMap		ArrayList with Search Results.
	 * @return				New SearchResult Array with Correction
	 */
	private ArrayList<SearchResult> beautifyTableNames(ArrayList<SearchResult> resultMap) {
		
		for (SearchResult searchResult : resultMap) {
			
			String tableName = searchResult.getCategoryName();
			searchResult.setCategoryName(correctTableName(tableName));
		}
		
		return null;
	}
	
	/**
	 * Splits table name by UnderScore then upper cases the first chars in every single word.
	 * 
	 * @param tableName		Table Name to Correct.
	 * @return				New formatted Table Name.
	 */
	private String correctTableName(String tableName) {
		
		String[] tableNameArray = tableName.split("_");
		
		String result = "";
		
		for (int i = 0; i < tableNameArray.length; i++) {
			
			if (i == 0) {
				
				String part = tableNameArray[i];
				result = result + part.substring(0, 1).toUpperCase(new Locale("en", "US")) + part.substring(1);
				
			} else {
				
				String part = tableNameArray[i];
				result = result + " " + part.substring(0, 1).toUpperCase(new Locale("en", "US")) + part.substring(1);
			}
		}
		
		return result;
	}
	
	/**
	 * Retrieves the Instance of the Object which implements the Interface
	 * 
	 * @param tableName 	Name of the External Table that was stored in Time Line Table as Reference
	 * @return 				Instance of SearchEngineInterface according to Table Name
	 */
	private SearchEngineCollection getInterfaceObjectWithTableName(String tableName) {
		
		if (tableName.equals(DBSQLiteHelper.TABLE_STUDENT)) {
			
			return new Student();
			
		} else if (tableName.equals(DBSQLiteHelper.TABLE_COMPANY)) {
			
			return new Company();
			
		} else {
			
			/* Unknown Database Table Name */
			return null;
		}
	}
	
	/**
	 * Opens DataBase to proceed
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
