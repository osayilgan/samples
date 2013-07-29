package okan.apps.samples.sampleapplication2.models;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import okan.apps.samples.sampleapplication2.collections.FavoriteCollection;
import okan.apps.samples.sampleapplication2.collections.SearchEngineCollection;

/**
 * Data Model for Company Type of Objects
 * 
 * @author Okan SAYILGAN
 */
public class Company implements SearchEngineCollection, FavoriteCollection {
	
	/** ID for DB */
	private long id;
	
	private String name;
	private String description;
	private String eMail;
	
	/** Company Medias */
	private ArrayList<CompanyMedia> companyMedias;
	
	/**
	 * Empty Constructor to be used for Favorite Manager
	 */
	public Company() {}
	
	public Company(long id, String name, String description, String eMail, ArrayList<CompanyMedia> companyMedias) {
		
		this.id = id;
		this.name = name;
		this.description = description;
		this.eMail = eMail;
		
		this.companyMedias = companyMedias;
	}
	
	public ArrayList<CompanyMedia> getCompanyMedias() {
		return companyMedias;
	}
	
	public void setCompanyMedias(ArrayList<CompanyMedia> companyMedias) {
		this.companyMedias = companyMedias;
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String geteMail() {
		return eMail;
	}
	
	public void seteMail(String eMail) {
		this.eMail = eMail;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (o instanceof Company) {
			return (((Company)o).getId() == this.id);
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
		return this.description;
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
