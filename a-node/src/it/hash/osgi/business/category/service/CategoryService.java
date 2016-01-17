package it.hash.osgi.business.category.service;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.category.Category;

public interface CategoryService {
	Category getCategory(String uuid);
	Map<String, Object> createCategory(Category category);
	Map<String, Object> updateCategory(Category category);
	Map<String, Object> deleteCategory(String uuid);
	
	Map<String, Object> createCategory(Map<String, Object> pars);
	Map<String, Object> updateCategory(Map<String, Object> pars);

	List<Map<String, Object>> retrieveCategories(String criterion);
}
