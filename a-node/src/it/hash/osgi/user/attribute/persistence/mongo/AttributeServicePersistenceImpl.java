package it.hash.osgi.user.attribute.persistence.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import it.hash.osgi.business.Business;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.utilsAttribute;
import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;
import it.hash.osgi.utils.StringUtils;

public class AttributeServicePersistenceImpl implements AttributeServicePersistence {
	private volatile MongoDBService m_mongoDBService;
	private static final String COLLECTION = "attributes";

	private DBCollection attributesCollection;

	public void start() {
		attributesCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	@Override
	public List<Attribute> getAttribute() {
		DBCursor cursor = attributesCollection.find();
		List<Attribute> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(utilsAttribute.toMap(cursor.next().toMap()));
		}
		return list;

	}

	@Override
	public List<Attribute> getAttributesByCategories(List<String> categories) {

		DBObject regexQuery = new BasicDBObject();

		List<Attribute> listAtt = new ArrayList<Attribute>();

		regexQuery = new BasicDBObject();

		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		for (String s : categories) {
			obj.add(new BasicDBObject("context", new BasicDBObject("$regex", s).append("$options", "$i")));
		}
		regexQuery.put("$or", obj);

		System.out.println(regexQuery.toString());
		DBCursor cursor = attributesCollection.find(regexQuery);
		List<DBObject> list = cursor.toArray();
		Attribute b;
		for (DBObject elem : list) {
			b = utilsAttribute.toMap(elem.toMap());
			listAtt.add(b);
		}
		return listAtt;
	}

	@Override
	public Map<String, Object> addAttribute(Attribute attribute) {
		Map<String, Object> response = new TreeMap<String, Object>();

		// Match attribute
		Map<String, Object> result = getAttribute(attribute);

		// If new attribute
		if ((int) result.get("matched") == 0) {

			attributesCollection.save(dbObjectAttribute(attribute));
			DBObject created = attributesCollection.findOne(dbObjectAttribute(attribute));

			if (created != null) {
				Attribute created_attribute =utilsAttribute.toMap(created.toMap());
				response.put("attribute", created_attribute);
				response.put("created", true);
				response.put("returnCode", 200);
			} else {
				response.put("created", false);
				response.put("returnCode", 630);
			}
		} else {
			response.put("created", false);
			response.put("returnCode", 630);

		}

		/*
		 * 
		 * WriteResult<Attribute, Object> writeResult =
		 * attributeMap.save(attribute); String savedId =
		 * (String)writeResult.getSavedId(); if
		 * (!StringUtils.isEmptyOrNull(savedId)) { Attribute created_attribute =
		 * attributeMap.findOneById(savedId); if (created_attribute != null) {
		 * response.put("attribute",created_attribute); response.put("created",
		 * true); response.put("returnCode", 200); } else {
		 * response.put("created", false); response.put("returnCode", 630); } }
		 */

		return response;
	}

	private DBObject dbObjectAttribute(Attribute attribute) {
		Map<String, Object> map = attributeToMap(attribute);
		DBObject db = new BasicDBObject(map);

		return db;
	}

	private Map<String, Object> attributeToMap(Attribute attribute) {
		Map<String, Object> pars = new HashMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(attribute.get_id()))
			pars.put("_id", attribute.get_id());

		if (!StringUtils.isEmptyOrNull(attribute.getUuid()))
			pars.put("uuid", attribute.getUuid());

		if (!StringUtils.isEmptyOrNull(attribute.getName()))
			pars.put("name", attribute.getName());
		if (!StringUtils.isEmptyOrNull(attribute.getLabel()))
			pars.put("label", attribute.getLabel());
		if (attribute.getValues() != null)
			pars.put("valuea", attribute.getValues());
		if (attribute.getContext() != null)
			pars.put("context", attribute.getContext());
		if (!StringUtils.isEmptyOrNull(attribute.getValidator()))
			pars.put("validator", attribute.getValidator());

		pars.put("mandatory", attribute.isMandatory());

		if (!StringUtils.isEmptyOrNull(attribute.getCauthor()))
			pars.put("cauthor", attribute.getCauthor());
		if (!StringUtils.isEmptyOrNull(attribute.getCdate()))
			pars.put("cdate", attribute.getCdate());
		if (!StringUtils.isEmptyOrNull(attribute.getMauthor()))
			pars.put("mauthor", attribute.getMauthor());
		if (!StringUtils.isEmptyOrNull(attribute.getMdate()))
			pars.put("mdate", attribute.getMdate());
		if (!StringUtils.isEmptyOrNull(attribute.getLauthor()))
			pars.put("lauthor", attribute.getLauthor());

		if (!StringUtils.isEmptyOrNull(attribute.getLdate()))
			pars.put("Ldate", attribute.getLdate());
		if (attribute.getOthers() != null)
			pars.put("others", attribute.getOthers());

		return pars;
	}

	private Map<String, Object> getAttribute(Attribute attribute) {
		Map<String, Object> map = new TreeMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(attribute.get_id()))
			map.put("_id", attribute.get_id());
		if (!StringUtils.isEmptyOrNull(attribute.getUuid()))
			map.put("uuid", attribute.getUuid());
		if (!StringUtils.isEmptyOrNull(attribute.getName()))
			map.put("name", attribute.getName());

		return getAttribute(map);

	}

	private Map<String, Object> getAttribute(Map<String, Object> map) {
		Map<String, Object> response = new HashMap<String, Object>();
		DBObject found = null;
		Attribute found_attribute = null;
		Map<Attribute, TreeSet<String>> matchs = new TreeMap<Attribute, TreeSet<String>>();

		if (map.containsKey("uuid") && map.get("uuid") != null) {
			found = attributesCollection.findOne(new BasicDBObject("uuid", map.get("uuid")));

			if (found != null) {
				found_attribute = utilsAttribute.toMap(found.toMap());

				TreeSet<String> list = matchs.get(found_attribute);
				if (list == null)
					list = new TreeSet<String>();

				list.add("uuid");
				matchs.put(found_attribute, list);
			}
		}
		if (map.containsKey("_id") && map.get("_id") != null) {
			found = attributesCollection.findOne(new BasicDBObject("_id", map.get("_id")));

			if (found != null) {
				found_attribute = utilsAttribute.toMap(found.toMap());
				TreeSet<String> list = matchs.get(found_attribute);
				if (list == null)
					list = new TreeSet<String>();

				list.add("_id");
				matchs.put(found_attribute, list);
			}
		}

		if (map.containsKey("name") && map.get("name") != null) {

			found = attributesCollection.findOne(new BasicDBObject("name", map.get("name")));

			if (found != null) {
				found_attribute =  utilsAttribute.toMap(found.toMap());

				TreeSet<String> list = matchs.get(found_attribute);
				if (list == null)
					list = new TreeSet<String>();
   
				list.add("name");
				matchs.put(found_attribute, list);
			}
		} // Set response: number of matched attributes
		response.put("matched", matchs.size());

		// Set response details
		switch (matchs.size()) {
		case 0:
			response.put("found", false);
			response.put("returnCode", 650);
			break;
		case 1:
			Attribute key = (Attribute) matchs.keySet().toArray()[0];
			response.put("attribute", key);
			response.put("keys", matchs.get(key));
			response.put("found", true);
			response.put("returnCode", 200);
			break;
		default:
			response.put("attributes", matchs);
			response.put("returnCode", 640);
		}

		return response;
	}
}
