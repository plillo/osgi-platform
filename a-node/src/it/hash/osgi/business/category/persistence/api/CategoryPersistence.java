package it.hash.osgi.business.category.persistence.api;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.category.Category;

public interface CategoryPersistence {
	// CREATE
	Map<String, Object> createCategory(Map<String, Object> business);
	Map<String, Object> createCategory(Category business);
	             
	// READ
	Map<String, Object> getCategory(Category business);
	Map<String, Object> getCategory(Map<String, Object> business);
	
	Business getCatgoryByUUID(String categoryUuid);
	
	List<Business> getCategories();
	
	// UPDATE
	Map<String, Object> updateCategory(Category business);
	Map<String, Object> updateCategory(Map<String, Object> business);
	
	// DELETE
	Map<String, Object> deleteCategory(Map<String, Object> business);
	
	// IMPLEMENTATION
	String getImplementation();
}
