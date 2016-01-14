package it.hash.osgi.business.category.service;

import java.util.List;
import java.util.Map;

import org.osgi.service.log.LogService;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.persistence.api.CategoryPersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;

public class CategoryServiceImpl implements CategoryService{
	// Injected services
	private volatile CategoryPersistence _persistence;
	private volatile UUIDService _uuid;
	@SuppressWarnings("unused")
	private volatile LogService logService;
	
	@Override
	public Category getCategory(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> createCategory(Category category) {
		category.setUUID(_uuid.createUUID("app/profiler/business-category"));
		return _persistence.createCategory(category);
	}

	@Override
	public Map<String, Object> updateCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteCategoryByUUID(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> createCategory(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteCategory(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateCategory(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> searchCategories(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}

}
