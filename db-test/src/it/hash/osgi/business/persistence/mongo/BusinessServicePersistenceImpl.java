package it.hash.osgi.business.persistence.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.amdatu.mongo.MongoDBService;
import org.osgi.service.log.LogService;
import com.mongodb.DBCollection;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.WriteResult;
import com.mongodb.BasicDBObject;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.uuid.api.UuidService;


public class BusinessServicePersistenceImpl implements BusinessServicePersistence {
	private static final String COLLECTION = "businesses";
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	@SuppressWarnings("unused")
	private volatile LogService logService;
	private volatile UuidService _uuid;
	// Mongo business collection
	private DBCollection businessCollection;
	JacksonDBCollection<Business, Object> businessMap ;

	public void start() {
		// Initialize business collection
		businessCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
		businessMap= JacksonDBCollection.wrap(businessCollection,
				Business.class);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> addBusiness(Map<String, Object> business) {

		Business business_obj = new Business();

		business_obj.setUsername((String) business.get("username"));
		business_obj.setPassword((String) business.get("password"));
		business_obj.setBusinessname((String) business.get("businessname"));
		business_obj.setPassword_mdate((String) business.get("password_mdate"));
		business_obj.setEmail((String) business.get("email"));
		business_obj.setMobile((String) business.get("mobile"));
		business_obj.setPublished((String) business.get("published"));
		business_obj.setLast_login_date((String) business.get("last_login_date"));
		business_obj.setLast_login_ip((String) business.get("last_login_ip"));
		business_obj.setTrusted_email((String) business.get("trusted_email"));
		business_obj.setTrusted_mobile((String) business.get("trusted_mobile"));
		business_obj.setCauthor((String) business.get("cauthor"));
		business_obj.setCdate((String) business.get("cdate"));
		business_obj.setMauthor((String) business.get("mauthor"));
		business_obj.setMdate((String) business.get("mdate"));
		business_obj.setLauthor((String) business.get("lauthor"));
		business_obj.setLdate((String) business.get("ldate"));
		business_obj.setBusiness_data((String) business.get("business_data"));
		business_obj.setOthers((Map<String, Object>) business.get("others"));

		return addBusiness(business_obj);

	}

	@Override
	public Map<String, Object> addBusiness(Business business) {

		Map<String, Object> response = new TreeMap<String, Object>();
	
		// Match business
		Map<String, Object> result = getBusiness(business);

		// If new business
		if ((int) result.get("matched") == 0) {
			String savedId = business.get_id();
			if (savedId == null || savedId.equals("") || savedId.isEmpty()) {
				Map<String, Object> u = _uuid.createUuid();
				savedId = (String) u.get("insertUuid");
				//business.set_id(savedId);
				System.out.println("prova");
			}
			WriteResult<Business, Object> writeResult = businessMap.save(business);
			savedId = (String) writeResult.getSavedId();
			if (savedId != null) {
				Business created_business = businessMap.findOneById(savedId);
				if (created_business != null) {
					response.put("business", created_business);
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
	public Map<String, Object> getBusiness(Business business) {
		Map<String, Object> map = new TreeMap<String, Object>();
		if (business.get_id() != null && !"".equals(business))
			map.put("businessId", business.get_id());
		if (business.getUsername() != null && !"".equals(business))
			map.put("username", business.getUsername());
		if (business.getEmail() != null && !"".equals(business))
			map.put("email", business.getEmail());
		if (business.getMobile() != null && !"".equals(business))
			map.put("mobile", business.getMobile());

		return getBusiness(map);
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {

		
		Map<String, Object> response = new HashMap<String, Object>();
		Business found_business = null;

		Map<Business, TreeSet<String>> matchs = new TreeMap<Business, TreeSet<String>>();

		if (business.containsKey("businessId") && business.get("businessId") != null) {
			found_business = businessMap.findOne(DBQuery.is("_id", business.get("businessId")));

			if (found_business != null) {
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("businessId");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("username") && business.get("username") != null) {
			found_business = businessMap.findOne(DBQuery.is("username", business.get("username")));

			if (found_business != null) {
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("username");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("email") && business.get("email") != null) {
			found_business = businessMap.findOne(new BasicDBObject("email", business.get("email")));
			if (found_business != null) {
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("email");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("mobile") && business.get("mobile") != null) {
			found_business = businessMap.findOne(new BasicDBObject("mobile", business.get("mobile")));
			if (found_business != null) {
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("mobile");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("firstName") && business.get("firstName") != null) {
			found_business = businessMap.findOne(new BasicDBObject("firstName", business.get("firstName")));
			if (found_business != null) {
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("firstName");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("lastName") && business.get("lastName") != null) {
			found_business = businessMap.findOne(new BasicDBObject("lastName", business.get("lastName")));
			if (found_business != null) {
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("lastName");
				matchs.put(found_business, list);
			}
		}

		// Set response: number of matched businesses
		response.put("matched", matchs.size());

		// Set response details
		switch (matchs.size()) {
		case 0:
			response.put("found", false);
			response.put("returnCode", 650);
			break;
		case 1:
			Business key = (Business) matchs.keySet().toArray()[0];
			response.put("business", key);
			response.put("keys", matchs.get(key));
			response.put("found", true);
			response.put("returnCode", 200);
			break;
		default:
			response.put("businesses", matchs);
			response.put("returnCode", 640);
		}

		return response;
	}

	private Business getBusinessByKey(String key, String value) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(key, value);
		Map<String, Object> response = getBusiness(map);
		if (response.containsKey("business"))
			return (Business) response.get("business");

		return null;
	}

	@Override
	public Business getBusinessByUsername(String username) {
		return getBusinessByKey("username", username);
	}

	@Override
	public Business getBusinessByEmail(String email) {
		return getBusinessByKey("email", email);
	}

	@Override
	public Business getBusinessByMobile(String mobile) {
		return getBusinessByKey("mobile", mobile);
	}

	@Override
	public Business getBusinessByBusinessname(String businessname) {
		return getBusinessByKey("businessname", businessname);
	}

	@Override
	public Business getBusinessById(String businessId) {
		return getBusinessByKey("businessId", businessId);
	}

	@Override
	public List<Business> getBusinesses() {

	
		DBCursor<Business> cursor = businessMap.find();

		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}
		return list;
	}

	@Override
	public List<Business> getBusinessDetails(Business business) {
		DBCursor<Business> cursor = businessMap.find(business);

		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}

		return list;
	}

	@Override
	public Map<String, Object> updateBusiness(Business business) {
		
		return createPars(business);
	}

	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> pars) {

		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseUpdate = new TreeMap<String, Object>();
	
		Business business = createBusiness(pars);
		response = getBusiness(business);
		if ((int) response.get("matched") == 1) {
			Business found = (Business) response.get("business");
			String _id = found.get_id();
			business.set_id(_id);

	//		DBObject oldObject = new BasicDBObject("_id", _id);
	//		DBObject newObject = new BasicDBObject(pars);

			Business updateBusiness = businessMap.findAndModify(new BasicDBObject("_id", _id),new BasicDBObject(pars));

			if (updateBusiness != null) {
				responseUpdate.put("business", updateBusiness);
				responseUpdate.put("update", "OK");
				responseUpdate.put("returnCode", 200);
				
			}
			else{
				responseUpdate.put("update", "ERROR");
				responseUpdate.put("returnCode",610 );
			}
		}
		else{
			responseUpdate.put("update", "ERROR");
			responseUpdate.put("returnCode",610 );
		}

		return responseUpdate;
	
	}

	private Map<String, Object> createPars(Business business) {
		Map<String, Object> pars = new HashMap<String, Object>();
		if (business.get_id() != null)
			pars.put("_id", business.get_id());
		if (business.getUsername() != null)
			pars.put("_id", business.getUsername());
		if (business.getPassword()!=null)
			pars.put("password", business.getPassword());
		if (business.getBusinessname()!=null)
			pars.put("businessname", business.getBusinessname());
		if (business.getPassword_mdate()!=null)
			pars.put("password_mdate", business.getPassword_mdate());
		if (business.getPassword_mdate()!=null)
			pars.put("password_mdate", business.getPassword_mdate());
		if (business.getEmail()!=null)
			pars.put("email", business.getEmail());
		if (business.getMobile()!=null)
			pars.put("mobile", business.getMobile());
		if (business.getPublished()!=null)
			pars.put("published", business.getPublished());
		if (business.getLast_login_date()!=null)
			pars.put("last_login_date", business.getLast_login_date());
		if (business.getLast_login_ip()!=null)
			pars.put("last_login_ip", business.getLast_login_ip());
		if (business.getTrusted_email()!=null)
			pars.put("trusted_email", business.getTrusted_email());
		if (business.getTrusted_mobile()!=null)
			pars.put("trusted_mobile", business.getTrusted_mobile());
		if (business.getCauthor()!=null)
			pars.put("cauthor", business.getCauthor());
		if (business.getCdate()!=null)
			pars.put("cdate", business.getCdate());
		if (business.getMauthor()!=null)
			pars.put("mauthor", business.getMauthor());
		if (business.getMdate()!=null)
			pars.put("mdate", business.getMdate());
		if (business.getLdate()!=null)
			pars.put("ldate", business.getLdate());
		if (business.getBusiness_data()!=null)
			pars.put("business_data", business.getBusiness_data());
		if (business.getOthers()!=null)
			pars.put("others", business.getOthers());
	
		return pars;
	}

	private Map<String, Object> deleteBusiness(Business business) {
	
		Map<String, Object> response = new TreeMap<String, Object>();
		
		response = getBusiness(business);
		if ((int) response.get("matched") == 1) {
			String username = ((Business) response.get("business")).getUsername();
			WriteResult<Business, Object> wr = businessMap.remove(new BasicDBObject("username", username));
			if (wr.getN() == 1) {
				response.put("delete", "OK");
				response.put("returnCode", 200);
			} else {
				response.put("delete", "ERROR");
				response.put("returnCode", 620);
			}
		} else {
			response.put("delete", "ERROR");
		}
		return response;
	}

	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> pars) {

		Business business = createBusiness(pars);

		return deleteBusiness(business);
	}

	@Override
	public Map<String, Object> login(Map<String, Object> business) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		String password = (String) business.get("password");

		// Return ERROR if missing password
		if (password == null || "".equals(password)) {
			response.put("returnCode", 601); // 601: missing password
			return response;
		}

		// Search and get user
		Map<String, Object> result = getBusiness(business);

		// Get reference to user (if found)
		Business businessFound = (Business) result.get("business");
		if (businessFound != null) {
			try {
				if (businessFound.getPassword().equals(password)) {
					response.put("user", businessFound);
					response.put("returnCode", 200);
					response.put("logged", true);
				} else
					response.put("returnCode", 602); // 602: mismatched password

			} catch (Exception e) {
				response.put("returnCode", 603); // 603: exception
				response.put("logged", false);
			}
		} else if (result.containsKey("businesses")) {
			response.put("returnCode", 640);
			response.put("logged", false);
			response.put("users", result.get("users"));
		} else if ((int) result.get("matched") == 0) {
			response.put("logged", false);
			response.put("returnCode", 650);
		}

		return response;
	}

	@Override
	public String getImplementation() {

		return "Mongo";

	}

	@SuppressWarnings("unchecked")
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
