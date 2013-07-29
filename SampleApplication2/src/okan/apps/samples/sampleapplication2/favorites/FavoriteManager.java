package okan.apps.samples.sampleapplication2.favorites;

import java.util.ArrayList;

import android.content.Context;
import okan.apps.samples.sampleapplication2.collections.FavoriteCollection;
import okan.apps.samples.sampleapplication2.database.DBSQLiteHelper;
import okan.apps.samples.sampleapplication2.database.FavoriteDataSource;
import okan.apps.samples.sampleapplication2.models.Company;
import okan.apps.samples.sampleapplication2.models.Favorite;
import okan.apps.samples.sampleapplication2.models.Student;
import okan.apps.samples.sampleapplication2.utils.Calculator;

/**
 * Manager Class for Favorites Objects
 * 
 * @author Okan SAYILGAN
 */
public class FavoriteManager {
	/**
	 * Adds Object to the Favorites
	 * 
	 * @param context 				Activity Context to Create data source for database operations
	 * @param externalObject		FavoriteCollection Object instance
	 */
	public static void addToFavorites(Context context, FavoriteCollection externalObject) {
		
		FavoriteDataSource dataSource = new FavoriteDataSource(context);		
		dataSource.createOrUpdateObject(new Favorite(Calculator.getTodaysDate(), externalObject));
	}
	
	/**
	 * Deletes Favorite Object based on Reference Object Table Name and ID
	 * 
	 * @param context		Activity Context
	 * @param favorite		Favorite Object
	 */
	public static void deleteObjectFromFavorites(Context context, Favorite favorite) {
		FavoriteDataSource favoriteDataSource = new FavoriteDataSource(context);
		favoriteDataSource.deleteFavoriteObject(favorite);
	}

	/**
	 * Deletes Favorite Object based on Reference Object Table Name and ID
	 * 
	 * @param context
	 *            Activity Context
	 * @param favoriteCollection
	 *            External Object which implements Favorite Interface
	 */
	public static void deleteObjectFromFavorites(Context context, FavoriteCollection favoriteCollection) {
		FavoriteDataSource favoriteDataSource = new FavoriteDataSource(context);
		favoriteDataSource.deleteFavoriteObject(favoriteCollection);
	}

	/**
	 * Retrieves the entire table in ascending order of Date
	 * 
	 * @param context
	 *            Activity Context
	 * @return List of Favorite Objects
	 */
	public static ArrayList<Favorite> getAllFavorites(Context context) {

		FavoriteDataSource dataSource = new FavoriteDataSource(context);
		return dataSource.getAllFavorites();
	}

	/**
	 * Retrieves the Favorite Object According to given insert ID
	 * 
	 * @param context
	 *            Activity Context
	 * @param id
	 *            insert id in the Table
	 * @return Favorite Object
	 */
	public static Favorite getFavoriteWithId(Context context, long id) {

		FavoriteDataSource dataSource = new FavoriteDataSource(context);
		return (Favorite) dataSource.getObjectWithId(id);
	}

	/**
	 * Retrieves the External object attached to the Favorites View
	 * 
	 * @param context
	 *            Activity Context
	 * @param favorite
	 *            Favorite Object Added to Favorites View
	 * @return Favorite Interface Instance, External Object which implements
	 *         FavoriteInterface.
	 */
	public static FavoriteCollection getFavoriteDetails(Context context, Favorite favorite) {
		
		FavoriteDataSource dataSource = new FavoriteDataSource(context);
		return dataSource.getReferenceObject(favorite);
	}
	
	/**
	 * Checks whether the Object is Added to the Favorites Database.
	 * 
	 * @param externalObject  	Object implements Favorite Interface.
	 * @return					True, If the External Object is added to the Favorites. False, otherwise.
	 */
	public static boolean isFavorite(Context context, FavoriteCollection externalObject) {
		
		long externalObjectId = externalObject.getId();
		String externalTableName = externalObject.getTableName();
		
		FavoriteDataSource favoriteDataSource = new FavoriteDataSource(context);
		
		return favoriteDataSource.isObjectExist(externalTableName, externalObjectId);
	}

	/**
	 * Retrieves the Instance of the Object which implements the Interface
	 * 
	 * @param tableName
	 *            Name of the External Table that was stored in Time Line Table
	 *            as Reference
	 * @return Instance of TimeLineInterface according to Table Name
	 */
	public static FavoriteCollection getInterfaceObjectWithTableName(String tableName) {
		
		if (tableName.equals(DBSQLiteHelper.TABLE_COMPANY)) {
			
			return new Company();
			
		} else if (tableName.equals(DBSQLiteHelper.TABLE_STUDENT)) {
			
			return new Student();
			
		} else {
			
			/* Unknown Database Table Name */
			return null;
		}
	}
}
