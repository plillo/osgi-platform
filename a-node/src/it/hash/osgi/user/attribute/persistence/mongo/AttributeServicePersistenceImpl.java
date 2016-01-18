package it.hash.osgi.user.attribute.persistence.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import it.hash.osgi.business.Business;
import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;
import it.hash.osgi.utils.StringUtils;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;

public class AttributeServicePersistenceImpl implements AttributeServicePersistence{
	private volatile MongoDBService m_mongoDBService;
	private static final String COLLECTION = "attributes";
	
	private DBCollection attributesCollection;
	private JacksonDBCollection<Attribute, Object> attributeMap;
	
	public void start() {
		attributesCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
		attributeMap = JacksonDBCollection.wrap(attributesCollection, Attribute.class);
	}
	
	@Override
	public List<Attribute> getAttributesByCategories(String[] categories) {
		Vector<String> ctgs = new Vector<String>();
		for(String ctg: categories) ctgs.add(ctg);
		com.mongodb.DBCursor cursor = attributesCollection.find();
		List<Attribute> list = new ArrayList<>();
		while (cursor.hasNext()) {
			DBObject dbo = cursor.next();
			List<String> o = (List<String>)dbo.get("context");
			if(o!=null)
				for(Iterator<String> i = o.iterator(); i.hasNext();){
					String context = i.next();
					if(context.startsWith("busctg:")){
						String ctg = context.split(":")[1];
						if(ctgs.contains(ctg)){
							list.add(mapToAttribute(dbo.toMap()));
							break;
						}
					}
				}
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

	@Override
	public Map<String, Object> createAttribute(Attribute attribute) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		WriteResult<Attribute, Object> writeResult = attributeMap.save(attribute);
		String savedId = (String) writeResult.getSavedId();
		if (!StringUtils.isEmptyOrNull(savedId)) {
			Attribute created_attribute = attributeMap.findOneById(savedId);
			if (created_attribute != null) {
				response.put("attribute", created_attribute);
				response.put("created", true);
				response.put("returnCode", 200);
			} else {
				response.put("created", false);
				response.put("returnCode", 630);
			}
		}
		
		return response;
	}

}
