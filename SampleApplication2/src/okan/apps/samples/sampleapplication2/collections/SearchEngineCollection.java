package okan.apps.samples.sampleapplication2.collections;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;

/**
 * Collection for Search Result Objects
 * 
 * @author Okan SAYILGAN
 */
public interface SearchEngineCollection {
	
	/** Retrieves the Object from Cursor. Use DataSource to invoke the method and (DO NOT CLOSE CURSOR) */
	SearchEngineCollection cursorToSearchEngineInterfaceObject(Context context, Cursor cursor);
	
	/**
	 * @return	The title of the Object that will appear in General Search Results
	 */
	String getSearchResultTitle();
	
	/**
	 * @return	The subtitle, description of the Object
	 */
	String getSearchResultSubtitle();
	
	/** Returns the ID of the Referenced Object */
	long getObjectId();
	
	/** Generates TYPE (holds name of the Table) String according to instance of TIME LINE INTERFACE */
	String getTableName();
	
	/** 
	 * Method to invoke When clicked on the Item in the General Search Result View.
	 * Define the Activity needs to be called.
	 */
	void onClickSearchResult(SearchEngineCollection interfaceObject, Activity activity);
}
