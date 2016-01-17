package it.hash.osgi.business.category.persistence.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

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
	public Map<String, Object> getCategory(Category category) {
		Map<String, Object> response = new HashMap<String, Object>();
		JacksonDBCollection<Category, Object> categoryMap = JacksonDBCollection.wrap(categoriesCollection, Category.class);

		Category found_category = null;

		Map<Category, TreeSet<String>> matchs = new TreeMap<Category, TreeSet<String>>();
		
		if (category.getUUID() != null) {
			found_category = categoryMap.findOne(new BasicDBObject("uuid", category.getUUID()));
			if (found_category != null) {
				TreeSet<String> list = matchs.get(found_category);
				if (list == null)
					list = new TreeSet<String>();

				list.add("uuid");
				matchs.put(found_category, list);
			}
		}

		// Set response: number of matched categories
		response.put("matched", matchs.size());

		// Set response details
		switch (matchs.size()) {
		case 0:
			response.put("found", false);
			response.put("returnCode", 650);
			break;
		case 1:
			Category key = (Category) matchs.keySet().toArray()[0];
			response.put("category", key);
			response.put("keys", matchs.get(key));
			response.put("found", true);
			response.put("returnCode", 200);
			break;
		default:
			response.put("categories", matchs);
			response.put("returnCode", 640);
		}

		return response;
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> retrieveCategories() {
		com.mongodb.DBCursor cursor = categoriesCollection.find();
		List<Map<String, Object>> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(cursor.next().toMap());
		}

		return list;
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
	public Map<String, Object> deleteCategory(String uuid) {
		Map<String, Object> response = new HashMap<String, Object>();
		com.mongodb.WriteResult wr;
		DBObject found_uuid = categoriesCollection.findOne(new BasicDBObject("uuid", uuid));
		if (found_uuid != null) {
			wr = categoriesCollection.remove(new BasicDBObject("uuid", uuid));
			if (wr.getN() == 1) {
				response.put("uuid",found_uuid);
				response.put("deleted", true);
				response.put("returnCode", 200);
			} else {
				response.put("deleted", false);
				response.put("returnCode",680);
			}

		}
		else {
			response.put("deleted", false);
			response.put("returnCode",680);
		}
		return response;
	}

	@Override
	public String getImplementation() {
		// TODO Auto-generated method stub
		return null;
	}

}
