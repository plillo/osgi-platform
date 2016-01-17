package it.hash.osgi.business.category.service;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.persistence.api.CategoryPersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;

public class CategoryServiceImpl implements CategoryService{
	// Injected services
	private volatile CategoryPersistence _persistence;
	private volatile UUIDService _uuid;
	
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
	public Map<String, Object> deleteCategory(String uuid) {
		return _persistence.deleteCategory(uuid);
	}

	@Override
	public Map<String, Object> createCategory(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateCategory(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> retrieveCategories(String criterion) {
		return _persistence.retrieveCategories();
	}
}
