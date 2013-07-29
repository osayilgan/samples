package okan.apps.samples.sampleapplication2.collections;

import android.content.Context;
import android.database.Cursor;

/**
 * Collection for Favorites Objects
 * 
 * @author Okan SAYILGAN
 */
public interface FavoriteCollection {
	
	/**
	 * Retrieves an Object from given Cursor Object
	 * 
	 * @param context	Activity Context
	 * @param cursor	Cursor from Database
	 * @return			Object Instance which implements Favorite Interface
	 */
	FavoriteCollection cursorToFavoriteInterfaceObject(Context context, Cursor cursor);
	
	/**
	 * @return		The Name of the Table associated with implemented Object
	 */
	String getTableName();
	
	/**
	 * @return	The insert ID of the implemented Object
	 */
	long getId();
}
