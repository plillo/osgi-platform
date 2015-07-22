package it.unisalento.idalab.osgi.content.service;

import java.util.Map;
import java.util.TreeMap;

import org.amdatu.mongo.MongoDBService;

import com.mongodb.DBCollection;

import net.vz.mongodb.jackson.JacksonDBCollection;
import it.unisalento.idalab.osgi.content.api.Content;
import it.unisalento.idalab.osgi.content.api.ContentService;

public class ContentServiceImpl implements ContentService {
	private static final String COLLECTION = "contents";
	private volatile MongoDBService m_mongoDBService;

	// Mongo User collection
	private DBCollection userCollection;
	
	public void start() {
		// Initialize user collection
		userCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	@Override
	public Map<String, ?> getContent(String name, String lang, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, ?> setContent(String name, String lang, String type, String content) {
		
		Content cnt = new Content();
		cnt.setName(name);
		cnt.setLang(lang);
		cnt.setType(type);
		cnt.setContent(content);
		
		return setContent(cnt);
	}
	
	
	// CREATE
	// ======
	public Map<String, Object> setContent(Content content) {
		Map<String, Object> response = new TreeMap<String, Object>();
		JacksonDBCollection<Content, String> contents = JacksonDBCollection.wrap(userCollection, Content.class, String.class);
		
		// Match content
		Map<String, Object> result = getContent(content);

		// If new content
		if((int)result.get("matched")==0) {
			
			String savedId = contents.save(content).getSavedId();
			if(savedId!=null) {
				Content created_content = contents.findOneById(savedId);
				if(created_content!=null) {
					response.put("content", created_content);
					response.put("created", true);
					response.put("returnCode", 100);
				}
			}
		}
		// If existing content
		else if((int)result.get("matched")==1){
			Content existing_content = (Content) result.get("content");
			if(existing_content!=null) {
				content.set_previousId(existing_content.get_id());
				String savedId = contents.save(content).getSavedId();
				if(savedId!=null) {
					Content created_content = contents.findOneById(savedId);
					if(created_content!=null) {
						response.put("content", created_content);
						response.put("created", false);
						response.put("returnCode", 105);
					}
				}
			}
		}
		// If existing many contents
		else{
			response.put("created", false);
			response.put("returnCode", 110);
			response.put("contents", result.get("contents"));
		}

		return response;
	}

	private Map<String, Object> getContent(Content content) {
		Map<String, Object> response= new TreeMap<String, Object>();
		response.put("matched", 0);
		
		return response;
	}

}
