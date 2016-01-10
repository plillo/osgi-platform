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
//import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.utils.StringUtils;

public class BusinessServicePersistenceImpl implements BusinessServicePersistence {
	private static final String COLLECTION = "businesses";
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	@SuppressWarnings("unused")
	private volatile LogService logService;
	// private volatile UUIDService _uuid;
	// Mongo business collection
	private DBCollection businessCollection;

	public void start() {
		// Initialize business collection
		businessCollection = m_mongoDBService.getDB().getCollection(COLLECTION);

	}

	// TODO controllare UUID in tutti i metodi
	@Override
	public Map<String, Object> addBusiness(Map<String, Object> business) {

		Business business_obj = createBusiness(business);

		return addBusiness(business_obj);

	}

	@Override
	public Map<String, Object> addBusiness(Business business) {
		JacksonDBCollection<Business, Object> businessMap = JacksonDBCollection.wrap(businessCollection,
				Business.class);

		Map<String, Object> response = new TreeMap<String, Object>();

		// Match business
		Map<String, Object> result = getBusiness(business);

		// If new business
		if ((int) result.get("matched") == 0) {
			/*
			 * String savedId = business.get_id(); if
			 * (StringUtils.isEmptyOrNull(savedId)) { // String u =
			 * _uuid.createUUID(savedId); business.set_id(savedId);
			 * business.setUUID(u);
			 * 
			 * }
			 */

			WriteResult<Business, Object> writeResult = businessMap.save(business);
			String savedId = (String) writeResult.getSavedId();
			if (!StringUtils.isEmptyOrNull(savedId)) {
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
		if (!StringUtils.isEmptyOrNull(business.get_id()))
			map.put("businessId", business.get_id());
		if (!StringUtils.isEmptyOrNull(business.getBusinessName()))
			map.put("businessName", business.getBusinessName());
		if (!StringUtils.isEmptyOrNull(business.getEmail()))
			map.put("email", business.getEmail());
		if (!StringUtils.isEmptyOrNull(business.getMobile()))
			map.put("mobile", business.getMobile());

		return getBusiness(map);
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {

		JacksonDBCollection<Business, Object> businessMap = JacksonDBCollection.wrap(businessCollection,
				Business.class);

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

		if (business.containsKey("businessName") && business.get("businessName") != null) {
			found_business = businessMap.findOne(DBQuery.is("businessName", business.get("businessName")));

			if (found_business != null) {
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("businessName");
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
	public Business getBusinessByEmail(String email) {
		return getBusinessByKey("email", email);
	}

	@Override
	public Business getBusinessByMobile(String mobile) {
		return getBusinessByKey("mobile", mobile);
	}

	@Override
	public Business getBusinessByBusinessName(String businessName) {
		return getBusinessByKey("businessName", businessName);
	}

	@Override
	public Business getBusinessById(String businessId) {
		return getBusinessByKey("businessId", businessId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Business> getBusinesses() {
		// dobbiamo leggerli come oggetti json perchè non sappiamo lo schema!!!
		com.mongodb.DBCursor cursor = businessCollection.find();
		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(createBusiness(cursor.next().toMap()));
		}

		return list;
	}

	@Override
	public List<Business> getBusinessDetails(Business business) {
		JacksonDBCollection<Business, Object> businessMap = JacksonDBCollection.wrap(businessCollection,
				Business.class);

		DBCursor<Business> cursor = businessMap.find(business);

		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}

		return list;
	}

	@Override
	public Map<String, Object> updateBusiness(Business business) {
		JacksonDBCollection<Business, Object> businessMap = JacksonDBCollection.wrap(businessCollection,
				Business.class);

		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseUpdate = new TreeMap<String, Object>();
		response = getBusiness(business);
		if ((int) response.get("matched") == 1) {
			Business found = (Business) response.get("business");
			String _id = found.get_id();
			business.set_id(_id);

			// DBObject oldObject = new BasicDBObject("_id", _id);
			// DBObject newObject = new BasicDBObject(pars);

			Business updateBusiness = businessMap.findAndModify(new BasicDBObject("_id", _id),
					new BasicDBObject(createPars(business)));

			if (updateBusiness != null) {
				responseUpdate.put("business", updateBusiness);
				responseUpdate.put("update", "OK");
				responseUpdate.put("returnCode", 200);

			} else {
				responseUpdate.put("update", "ERROR");
				responseUpdate.put("returnCode", 610);
			}
		} else {
			responseUpdate.put("update", "ERROR");
			responseUpdate.put("returnCode", 610);
		}

		return responseUpdate;
	}

	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> pars) {

		return updateBusiness(createBusiness(pars));

	}

	private Map<String, Object> createPars(Business business) {

		Map<String, Object> pars = new HashMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(business.get_id()))
			pars.put("_id", business.get_id());
		if (!StringUtils.isEmptyOrNull(business.getBusinessName()))
			pars.put("_id", business.getBusinessName());
		if (!StringUtils.isEmptyOrNull(business.getPIva()))
			pars.put("_id", business.getPIva());
		if (!StringUtils.isEmptyOrNull(business.getCodiceFiscale()))
			pars.put("_id", business.getCodiceFiscale());
		if (!StringUtils.isEmptyOrNull(business.getAddress()))
			pars.put("_id", business.getAddress());
		if (!StringUtils.isEmptyOrNull(business.getCity()))
			pars.put("_id", business.getCity());
		if (!StringUtils.isEmptyOrNull(business.getCap()))
			pars.put("_id", business.getCap());
		if (!StringUtils.isEmptyOrNull(business.getNation()))
			pars.put("_id", business.getNation());
		if (!StringUtils.isEmptyOrNull(business.get__Description()))
			pars.put("_id", business.get__Description());
		if (!StringUtils.isEmptyOrNull(business.get__longDescription()))
			pars.put("_id", business.get__longDescription());
		if (!StringUtils.isEmptyOrNull(business.getEmail()))
			pars.put("email", business.getEmail());
		if (!StringUtils.isEmptyOrNull(business.getMobile()))
			pars.put("mobile", business.getMobile());
		if (!StringUtils.isEmptyOrNull(business.getPublished()))
			pars.put("published", business.getPublished());
		if (!StringUtils.isEmptyOrNull(business.getTrusted_email()))
			pars.put("trusted_email", business.getTrusted_email());
		if (!StringUtils.isEmptyOrNull(business.getTrusted_mobile()))
			pars.put("trusted_mobile", business.getTrusted_mobile());
		if (!StringUtils.isEmptyOrNull(business.getCauthor()))
			pars.put("cauthor", business.getCauthor());
		if (!StringUtils.isEmptyOrNull(business.getCdate()))
			pars.put("cdate", business.getCdate());
		if (!StringUtils.isEmptyOrNull(business.getMauthor()))
			pars.put("mauthor", business.getMauthor());
		if (!StringUtils.isEmptyOrNull(business.getMdate()))
			pars.put("mdate", business.getMdate());
		if (!StringUtils.isEmptyOrNull(business.getLdate()))
			pars.put("ldate", business.getLdate());
		if (business.getOthers() != null)
			pars.put("others", business.getOthers());

		return pars;
	}

	private Map<String, Object> deleteBusiness(Business business) {
		JacksonDBCollection<Business, Object> businessMap = JacksonDBCollection.wrap(businessCollection,
				Business.class);

		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseDelete = new TreeMap<String, Object>();
		response = getBusiness(business);
		if ((int) response.get("matched") == 1) {
			String _id = ((Business) response.get("business")).get_id();
			WriteResult<Business, Object> wr = businessMap.remove(new BasicDBObject("username", _id));
			if (wr.getN() == 1) {
				responseDelete.put("business", business);
				responseDelete.put("delete", "OK");
				responseDelete.put("returnCode", 200);
			} else {
				responseDelete.put("delete", "ERROR");
				responseDelete.put("returnCode", 620);
			}
		} else {
			responseDelete.put("delete", "ERROR");
		}
		return responseDelete;
	}

	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> pars) {

		return deleteBusiness(createBusiness(pars));
	}

	@Override
	public String getImplementation() {

		return "Mongo";

	}

	@SuppressWarnings("unchecked")
	private Business createBusiness(Map<String, Object> mapBusiness) {
		// .. abbiamo detto che in un database Nosql non si ha uno schema fisso
		// per cui
		// dobbiamo controllare quali attributi andranno settati!!!
		// TODO : abbiamo previsto una variabile di istanza come Map in modo da
		// poter
		/*
		 * inserire attributi dell'entità business non previsti al momento della
		 * progettazione. Bisogna stare attenti a cosa mettiamo nella
		 * mapBusiness
		 */
		Business business = new Business();
		String attribute = null;
		Map<String, Object> others = new TreeMap<String, Object>();
		for (Map.Entry<String, Object> entry : mapBusiness.entrySet()) {
			attribute = entry.getKey();

			if (attribute.equals("_id")) {
				business.set_id(entry.getValue().toString());
			} else {
				if (entry.getValue() instanceof String) {

					String value = (String) entry.getValue();

					switch (attribute.toLowerCase()) {
					case "businessname":
						business.setBusinessName(value);
						break;
					case "piva":
						business.setPIva(value);
						break;
					case "codicefiscale":
						business.setCodiceFiscale(value);
						break;
					case "address":
						business.setAddress(value);
						break;
					case "city":
						business.setCity(value);
						break;
					case "cap":
						business.setCap(value);
						break;
					case "nation":
						business.setNation(value);
						break;
					case "__Description":
						business.set__Description(value);
						break;
					case "__longdescription":
						business.set__longDescription(value);
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
					default:
						others.put(attribute, value);

					}

				} else {
					if (entry.getValue() instanceof Map<?, ?>) {
						business.setOthers((Map<String, Object>) entry.getValue());
					} else
						others.put(attribute, entry.getValue());
				}
			}
		}

		if (!others.isEmpty())
			business.setOthers(others);
		return business;

	}

}
