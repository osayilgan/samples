package okan.apps.samples.sampleapplication2.models;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import okan.apps.samples.sampleapplication2.collections.FavoriteCollection;
import okan.apps.samples.sampleapplication2.collections.SearchEngineCollection;

/**
 * Data Model for Student Type of Objects
 * 
 * @author Okan SAYILGAN
 */
public class Student implements SearchEngineCollection, FavoriteCollection {
	
	/** ID for DB */
	private long id;
	
	private String name;
	private String department;
	
	/**
	 * Empty Constructor to be used for Favorite Manager
	 */
	public Student() {}
	
	public Student(long id, String name, String department) {
		
		this.id = id;
		this.name = name;
		this.department = department;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDepartment() {
		return department;
	}
	
	public void setDepartment(String department) {
		this.department = department;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o instanceof Student) {
			return (((Student)o).getId() == this.id);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (int) this.id;
	}
	
	@Override
	public FavoriteCollection cursorToFavoriteInterfaceObject(Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public SearchEngineCollection cursorToSearchEngineInterfaceObject(Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getSearchResultTitle() {
		return this.name;
	}
	
	@Override
	public String getSearchResultSubtitle() {
		return this.department;
	}
	
	@Override
	public long getObjectId() {
		return this.id;
	}
	
	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onClickSearchResult(SearchEngineCollection interfaceObject, Activity activity) {
		// TODO Auto-generated method stub
		
	}
}
