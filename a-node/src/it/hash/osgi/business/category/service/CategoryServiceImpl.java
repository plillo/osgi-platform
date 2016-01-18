package it.hash.osgi.business.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.persistence.api.CategoryPersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

public class CategoryServiceImpl implements CategoryService{
	// Injected services
	private volatile CategoryPersistence _persistenceSrv;
	private volatile UUIDService _uuidSrv;
	private volatile AttributeService _attributeSrv;
	
	@Override
	public Category getCategory(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> createCategory(Category category) {
		category.setUUID(_uuidSrv.createUUID("app/profiler/business-category"));

		return _persistenceSrv.createCategory(category);
	}

	@Override
	public Map<String, Object> updateCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteCategory(String uuid) {
		return _persistenceSrv.deleteCategory(uuid);
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
		return _persistenceSrv.retrieveCategories();
	}
	
	// ATTRIBUTES
	// ==========
	@Override
	public List<Attribute> getAttributes(String ctgUuid) {
		return _attributeSrv.getAttributesByCategories(new String[]{ctgUuid});
	}

	@Override
	public Map<String, Object> createAttribute(String ctgUuid, Attribute attribute) {
		if(attribute.getContext()==null)
			attribute.setContext(new ArrayList<String>());
		
		List<String> context = attribute.getContext();
		if(!context.contains("busctg:"+ctgUuid))
			context.add("busctg:"+ctgUuid);
			
		return _attributeSrv.createAttribute(attribute);
	}

	@Override
	public Map<String, Object> updateAttribute(String ctgUuid, Attribute attr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteAttribute(String ctgUuid, String attrUuid) {
		// TODO Auto-generated method stub
		return null;
	}

}
