package it.hash.osgi.business.persistence.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.amdatu.mongo.MongoDBService;
import org.osgi.service.log.LogService;

import com.mongodb.DBCollection;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.amdatu.mongo.MongoDBService;
import org.osgi.service.log.LogService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class BusinessServicePersistenceImpl implements BusinessServicePersistence{
	private static final String COLLECTION = "businesses";
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	@SuppressWarnings("unused")
	private volatile LogService logService;

	
	// Mongo User collection
	private DBCollection businessCollection;
	
	public void start() {
		// Initialize user collection
		businessCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}
	
	
	@Override
	public Map<String, Object> addBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		
		Business b= createBusiness(business);
		JacksonDBCollection<Business, Object> businesses= JacksonDBCollection.wrap(businessCollection, Business.class);
		businesses.save(createBusiness(business));
		
		return null;
	}

	@Override
	public Map<String, Object> addBusiness(Business business) {
		// TODO Auto-generated method stub
		
		JacksonDBCollection<Business, Object> businesses= JacksonDBCollection.wrap(businessCollection, Business.class);
		businesses.save(business);
		return null;
	}

	@Override
	public Map<String, Object> getBusiness(Business business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getBusinessByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getBusinessByMobile(String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getBusinessByBusinessname(String businessname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Business getBusinessById(String businessId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> getBusinesses() {

		JacksonDBCollection<Business, Object> businesses = JacksonDBCollection.wrap(businessCollection, Business.class);
		DBCursor<Business> cursor = businesses.find();
		
		List<Business> list = new ArrayList<>();
		while(cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list;
	}

	@Override
	public List<Business> getBusinessDetails(Business business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateBusiness(Business business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> login(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImplementation() {
		// TODO Auto-generated method stub
		return null;
		
	}
	private Business createBusiness(Map<String, Object> mapbusiness) {

			// .. abbiamo detto che in un database Nosql non si ha uno schema fisso
			// per cui
			// dobbiamo controllare quali attributi andranno settati!!!
			Business business = new Business();
			for (Map.Entry<String, Object> entry : mapbusiness.entrySet()) {
				String attribute = entry.getKey();
				String value = (String) entry.getValue();

				switch (attribute.toLowerCase()) {

				case "_id":
					business.set_id(value);
					break;

				case "username":
					business.setUsername(value);
					break;

				case "password":
					business.setPassword(value);
					break;

				case "businessname":
					business.setBusinessname(value);
					break;

				case "password_mdate":
					business.setPassword_mdate(value);
					break;

				case "email":
					business.setEmail(value);
					break;

				case "mobile":
					business.setMobile(value);
					break;

				case "published":
					business.setPublished(value);
					break;

				case "last_login_date":
					business.setLast_login_date(value);
					break;

				case "last_login_ip":
					business.setMobile(value);
					break;

				case "trusted_email":
					business.setTrusted_email(value);
					break;

				case "trusted_mobile":
					business.setTrusted_mobile(value);
					break;

				case "cauthor":
					business.setCauthor(value);
					break;
				case "cdate":
					business.setCdate(value);
					break;
				case "mauthor":
					business.setMauthor(value);
					break;
				case "mdate":
					business.setMdate(value);
					break;
				case "lauthor":
					business.setLauthor(value);
					break;
				case "ldate":
					business.setLdate(value);
					break;
				case "business_data":
					business.setBusiness_data(value);
					break;
				case "others":
					business.setOthers((Map<String, Object>) entry.getValue());
					break;

				}
				System.out.println(entry.getKey() + "/" + entry.getValue());
			}

			return business;

		}
	

}
