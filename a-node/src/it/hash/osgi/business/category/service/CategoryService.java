package it.hash.osgi.business.category.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.user.attribute.Attribute;

public interface CategoryService {
	Category getCategory(String uuid);
	Map<String, Object> createCategory(Category category);
	Map<String, Object> updateCategory(Category category);
	Map<String, Object> deleteCategory(String uuid);
	
	Map<String, Object> createCategory(Map<String, Object> pars);
	Map<String, Object> updateCategory(Map<String, Object> pars);

	List<Map<String, Object>> retrieveCategories(String criterion);
	
	List<Attribute> getAttributes(String ctgUuid);
	Map<String, Object> createAttribute(String ctgUuid, Attribute attribute);
	Map<String, Object> updateAttribute(String ctgUuid, Attribute attribute);
	Map<String, Object> deleteAttribute(String ctgUuid, String attrUuid);
	boolean createCollection(String url,String nameFile) ;
	
}
