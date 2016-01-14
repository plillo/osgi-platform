package it.hash.osgi.business.category.persistence.mongo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.DBCollection;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.persistence.api.CategoryPersistence;
import it.hash.osgi.utils.StringUtils;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;

public class CategoryPersistenceImpl implements CategoryPersistence{
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	private static final String COLLECTION = "categories";
	
	private DBCollection categoriesCollection;
	
	public void start() {
		categoriesCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}
	
	@Override
	public Map<String, Object> createCategory(Category category) {

		JacksonDBCollection<Category, Object> categoryMap = JacksonDBCollection.wrap(categoriesCollection, Category.class);

		Map<String, Object> response = new TreeMap<String, Object>();

		// Look for matching categories
		Map<String, Object> result = getCategory(category);

		// If new category
		if ((int) result.get("matched") == 0) {
			WriteResult<Category, Object> writeResult = categoryMap.save(category);
			String savedId = (String) writeResult.getSavedId();
			if (!StringUtils.isEmptyOrNull(savedId)) {
				Category created_category = categoryMap.findOneById(savedId);
				if (created_category != null) {
					response.put("category", created_category);
					response.put("created", true);
					response.put("returnCode", 200);
				} else {
					response.put("created", false);
					response.put("returnCode", 630);
				}
			}

		} else {
			response.put("created", false);
			response.put("returnCode", 640);
		}

		return response;
	}

	@Override
	public Map<String, Object> createCategory(Map<String, Object> category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getCategory(Category business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getCategory(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getCatgoryByUUID(String categoryUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> getCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateCategory(Category business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateCategory(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteCategory(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

}
