package it.hash.osgi.user.attribute.persistence.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.DBCollection;

import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;

public class AttributeServicePersistenceImpl implements AttributeServicePersistence{
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	private static final String COLLECTION = "attributes";
	
	private DBCollection attributesCollection;
	
	public void start() {
		attributesCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}
	
	@Override
	public List<Attribute> getAttributesByCategories(String[] categories) {
		com.mongodb.DBCursor cursor = attributesCollection.find();
		List<Attribute> list = new ArrayList<>();
		while (cursor.hasNext()) {
			List<String> o = (List<String>)cursor.next().get("Context");
			
			list.add(mapToAttribute(cursor.next().toMap()));
		}

		return list;
	}

	private Attribute mapToAttribute(Map<String, Object> map) {
		Attribute attribute = new Attribute();
		
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			switch (key.toLowerCase()) {
				case "_id":
					attribute.set_id(entry.getValue().toString());
					break;
				case "uuid":
					attribute.setUuid((String) entry.getValue());
					break;
				case "name":
					attribute.setName((String) entry.getValue());
					break;
				case "label":
					attribute.setLabel((String) entry.getValue());
					break;
				case "values":
					attribute.setValues((List<String>) entry.getValue());
					break;
				case "cauthor":
					attribute.setCauthor((String) entry.getValue());
					break;
				case "cdate":
					attribute.setCdate((String) entry.getValue());
					break;
				case "mauthor":
					attribute.setMauthor((String) entry.getValue());
					break;
				case "mdate":
					attribute.setMdate((String) entry.getValue());
					break;
				case "lauthor":
					attribute.setLauthor((String) entry.getValue());
					break;
				case "ldate":
					attribute.setLdate((String) entry.getValue());
					break;
				case "others":
					attribute.setOthers((TreeMap<String, Object>) entry.getValue());
					break;
				default:
					if (attribute.getOthers() == null)
						attribute.setOthers(new HashMap<String, Object>());
					
					attribute.setOthers(key, entry.getValue());
			}
		}
		
		return attribute;
	}

}
