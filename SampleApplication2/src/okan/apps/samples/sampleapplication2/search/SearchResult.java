package okan.apps.samples.sampleapplication2.search;

import okan.apps.samples.sampleapplication2.collections.SearchEngineCollection;

/**
 * Data Model to combine Search Result Object with only one Category
 * 
 * @author Okan SAYILGAN
 */
public class SearchResult {
	
	private SearchEngineCollection interfaceObject;
	private String categoryName;
	
	public SearchResult(SearchEngineCollection interfaceObject, String categoryName) {
		this.interfaceObject = interfaceObject;
		this.categoryName = categoryName;
	}
	
	public SearchEngineCollection getInterfaceObject() {
		return interfaceObject;
	}
	
	public void setInterfaceObject(SearchEngineCollection interfaceObject) {
		this.interfaceObject = interfaceObject;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
