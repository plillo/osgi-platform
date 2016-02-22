package it.hash.osgi.business.persistence.mongo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.amdatu.mongo.MongoDBService;
import org.osgi.service.log.LogService;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

import com.mongodb.BasicDBObject;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.utils.StringUtils;

/**
 * Implements interface BusinessServicePersistence with MongoDB:
 * DBMS,open-source, document-oriented
 * 
 * @author Montinari Antonella
 */

public class BusinessServicePersistenceImpl implements BusinessServicePersistence {
	/** Name of the collection */
	private static final String COLLECTION = "businesses";
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	@SuppressWarnings("unused")
	private volatile LogService logService;
	// Mongo business collection
	private DBCollection businessCollection;

	public void start() {
		// Initialize business collection
		businessCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	@Override
	public Map<String, Object> addBusiness(Map<String, Object> business) {

		Business business_obj = createBusiness(business);

		return addBusiness(business_obj);

	}

	private DBObject dbObjectBusiness(Business business) {
		Map<String, Object> map = createPars(business);
		DBObject db = new BasicDBObject(map);

		return db;
	}

	@Override
	public Map<String, Object> addBusiness(Business business) {

		Map<String, Object> response = new TreeMap<String, Object>();

		// Match business
		Map<String, Object> result = getBusiness(business);
		// If new business
		if ((int) result.get("matched") == 0) {

			businessCollection.save(dbObjectBusiness(business));
			DBObject created = businessCollection.findOne(dbObjectBusiness(business));

			if (created != null) {
				Business created_business = createBusiness(created.toMap());
				response.put("business", created_business);
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
		return response;
	}

	@Override
	public Map<String, Object> getBusiness(Business business) {
		Map<String, Object> map = new TreeMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(business.get_id()))
			map.put("_id", business.get_id());
		if (!StringUtils.isEmptyOrNull(business.getUuid()))
			map.put("uuid", business.getUuid());
		if (!StringUtils.isEmptyOrNull(business.getName()))
			map.put("name", business.getName());
		if (!StringUtils.isEmptyOrNull(business.getPIva()))
			map.put("pIva", business.getPIva());
		if (!StringUtils.isEmptyOrNull(business.getFiscalCode()))
			map.put("fiscalCode", business.getFiscalCode());

		return getBusiness(map);
	}

	@Override
	public List<Business> retrieveBusinesses(String criterion, String search) {

		List<DBObject> list = new ArrayList<DBObject>();
		List<Business> listB = new ArrayList<Business>();
		BasicDBObject regexQuery = null;

		regexQuery = new BasicDBObject();
		List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
		obj.add(new BasicDBObject("name", new BasicDBObject("$regex", search).append("$options", "$i")));
		obj.add(new BasicDBObject("_description", new BasicDBObject("$regex", search).append("$options", "$i")));
		regexQuery.put("$or", obj);

		// regexQuery.put("name", new BasicDBObject("$regex",
		// search).append("$options", "$i"));
		// db.categories.find({name:{$regex:"computer",$options:"$i"}})
		System.out.println(regexQuery.toString());
		DBCursor cursor = businessCollection.find(regexQuery);
		list = cursor.toArray();
		Business b;
		for (DBObject elem : list) {
			b = createBusiness(elem.toMap());
			listB.add(b);

		}

		return listB;

	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {

		Map<String, Object> response = new HashMap<String, Object>();
		DBObject found = null;
		Business found_business = null;
		Map<Business, TreeSet<String>> matchs = new TreeMap<Business, TreeSet<String>>();

		if (business.containsKey("uuid") && business.get("uuid") != null) {
			found = businessCollection.findOne(new BasicDBObject("uuid", business.get("uuid")));

			if (found != null) {
				found_business = createBusiness(found.toMap());

				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("uuid");
				matchs.put(found_business, list);
			}
		}
		
		if (business.containsKey("_id") && business.get("_id") != null) {
			found = businessCollection.findOne(new BasicDBObject("_id", business.get("_id")));

			if (found != null) {
				found_business = createBusiness(found.toMap());
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("_id");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("fiscalCode") && business.get("fiscalCode") != null) {
			found = businessCollection.findOne(new BasicDBObject("fiscalCode", business.get("fiscalCode")));
			if (found != null) {
				found_business = createBusiness(found.toMap());
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("fiscalCode");
				matchs.put(found_business, list);
			}
		}

		if (business.containsKey("name") && business.get("name") != null) {

			found = businessCollection.findOne(new BasicDBObject("name", business.get("name")));

			if (found != null) {
				found_business = createBusiness(found.toMap());

				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("name");
				matchs.put(found_business, list);
			}
		}
		if (business.containsKey("pIva") && business.get("pIva") != null) {
			found = businessCollection.findOne(new BasicDBObject("pIva", business.get("pIva")));

			if (found != null) {
				found_business = createBusiness(found.toMap());
				TreeSet<String> list = matchs.get(found_business);
				if (list == null)
					list = new TreeSet<String>();

				list.add("partitaIva");
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
	public Business getBusinessByFiscalCode(String fiscalCode) {
		return getBusinessByKey("fiscalCode", fiscalCode);
	}

	@Override
	public Business getBusinessByPartitaIva(String partitaIva) {
		return getBusinessByKey("partitaIva", partitaIva);
	}

	@Override
	public Business getBusinessByName(String name) {

		return getBusinessByKey("name", name);
	}

	@Override
	public Business getBusinessById(String businessId) {
		return getBusinessByKey("businessId", businessId);
	}

	@Override
	public Business getBusinessByUuid(String uuid) {
		return getBusinessByKey("uuid", uuid);
	}

	@Override
	public List<Business> getBusinesses() {
		// dobbiamo leggerli come oggetti json perchè non sappiamo lo schema!!!
		DBCursor cursor = businessCollection.find();
		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(createBusiness(cursor.next().toMap()));
		}

		return list;
	}

	@Override
	public List<Business> getBusinessDetails(Business business) {

		DBCursor cursor = businessCollection.find(dbObjectBusiness(business));

		List<Business> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(createBusiness(cursor.next().toMap()));
		}

		return list;
	}

	@Override
	public synchronized Map<String, Object> updateBusiness(Business business) {

		// JacksonDBCollection<Business, Object> businessMap =
		// JacksonDBCollection.wrap(businessCollection,
		// Business.class);

		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseUpdate = new TreeMap<String, Object>();
		response = getBusiness(business);
		if ((int) response.get("matched") == 1) {
			Business found = (Business) response.get("business");

			BasicDBObject oldObject = new BasicDBObject("uuid", found.getUuid());
			Map<String, Object> map = createPars(business);

			BasicDBObject newObject = new BasicDBObject(map);

			DBObject update = businessCollection.findAndModify(oldObject, newObject);
			Business updateBusiness = createBusiness(update.toMap());
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
		Map<String, Object> response = new HashMap<String, Object>();
		String uuid = ((Business) pars.get("business")).getUuid();
		Business business = getBusinessByUuid(uuid);
		if (business != null) {

			response = updateBusiness((Business) pars.get("business"));
		} else {
			response.put("update", "ERROR");
			response.put("returnCode", 610);
		}

		return response;
	}

	private Map<String, Object> createPars(Business business) {

		Map<String, Object> pars = new HashMap<String, Object>();

		if (!StringUtils.isEmptyOrNull(business.get_id()))
			pars.put("_id", business.get_id());
		if (!StringUtils.isEmptyOrNull(business.getUuid()))
			pars.put("uuid", business.getUuid());
		if (!StringUtils.isEmptyOrNull(business.getName()))
			pars.put("name", business.getName());
		if (!StringUtils.isEmptyOrNull(business.getPIva()))
			pars.put("pIva", business.getPIva());
		if (!StringUtils.isEmptyOrNull(business.getFiscalCode()))
			pars.put("fiscalCode", business.getFiscalCode());
		if (!StringUtils.isEmptyOrNull(business.getAddress()))
			pars.put("address", business.getAddress());
		if (!StringUtils.isEmptyOrNull(business.getCity()))
			pars.put("city", business.getCity());
		if (!StringUtils.isEmptyOrNull(business.getCap()))
			pars.put("cap", business.getCap());
		if (!StringUtils.isEmptyOrNull(business.getNation()))
			pars.put("nation", business.getNation());
		if (!StringUtils.isEmptyOrNull(business.get__Description()))
			pars.put("_description", business.get__Description());
		if (!StringUtils.isEmptyOrNull(business.get__longDescription()))
			pars.put("_longDescription", business.get__longDescription());
		if (business.getCategories() != null)
			pars.put("categories", business.getCategories());
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

	private synchronized Map<String, Object> deleteBusiness(Business business) {

		Map<String, Object> response = new TreeMap<String, Object>();
		Map<String, Object> responseDelete = new TreeMap<String, Object>();
		response = getBusiness(business);
		if ((int) response.get("matched") == 1) {
			String uuid = ((Business) response.get("business")).getUuid();
			BasicDBObject Dbo = new BasicDBObject("uuid", uuid);
			WriteResult wr = businessCollection.remove(Dbo);
			if (wr.getN() == 1) {
				responseDelete.put("business", business);
				responseDelete.put("delete", true);
				responseDelete.put("returnCode", 200);
			} else {
				responseDelete.put("delete", false);
				responseDelete.put("returnCode", 620);
			}
		} else {
			responseDelete.put("delete", false);
			responseDelete.put("returnCode", 680);

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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Business createBusiness(Map mapBusiness) {
		// .. abbiamo detto che in un database Nosql non si ha uno schema fisso
		// per cui
		// dobbiamo controllare quali attributi andranno settati!!!
		// TODO : abbiamo previsto una variabile di istanza come Map in modo da
		/*
		 * poter inserire attributi dell'entità business non previsti al momento
		 * della progettazione. Se vogliamo aggiungere un nuovo attributo
		 * nomeAttributo-valore ( verra aggiunto nella Map) Se vogliamo
		 * sostituire la Map others-Map Se vogliamo aggiungere una categoria di
		 * merce alla lista già esistente category-idCategory se vogliamo
		 * sostituire la lista delle Categorie categories- List
		 */
		Business business = new Business();
		String attribute = null;
		Map<String, Object> others = new TreeMap<String, Object>();
		Set entry = mapBusiness.keySet();
		for (Object elem : entry) {
			attribute = (String) elem;
			if (attribute.equals("_id")) {
				Object o = mapBusiness.get(attribute).toString();
				business.set_id(o.toString());
			} else {

				switch (attribute) {
				case "uuid":
					business.setUuid((String) mapBusiness.get(attribute));
					break;
				case "name":
					business.setName((String) mapBusiness.get(attribute));
					break;
				case "pIva":
					business.setPIva((String) mapBusiness.get(attribute));
					break;
				case "fiscalCode":
					business.setFiscalCode((String) mapBusiness.get(attribute));
					break;
				case "address":
					business.setAddress((String) mapBusiness.get(attribute));
					break;
				case "city":
					business.setCity((String) mapBusiness.get(attribute));
					break;
				case "cap":
					business.setCap((String) mapBusiness.get(attribute));
					break;
				case "nation":
					business.setNation((String) mapBusiness.get(attribute));
					break;
				case "_description":
					business.set__Description((String) mapBusiness.get(elem));
					break;
				case "_longDescription":
					business.set__longDescription((String) mapBusiness.get(elem));
					break;
				case "category":
					if (business.getCategories() == null)
						business.setCategories(new ArrayList<String>());
					business.addCategory((String) mapBusiness.get(elem));
					break;
				case "email":
					business.setEmail((String) mapBusiness.get(elem));
					break;

				case "mobile":
					business.setMobile((String) mapBusiness.get(elem));
					break;

				case "published":
					business.setPublished((String) mapBusiness.get(elem));
					break;

				case "trusted_email":
					business.setTrusted_email((String) mapBusiness.get(elem));
					break;

				case "trusted_mobile":
					business.setTrusted_mobile((String) mapBusiness.get(elem));
					break;

				case "cauthor":
					business.setCauthor((String) mapBusiness.get(elem));
					break;
				case "cdate":
					business.setCdate((String) mapBusiness.get(elem));
					break;
				case "mauthor":
					business.setMauthor((String) mapBusiness.get(elem));
					break;
				case "mdate":
					business.setMdate((String) mapBusiness.get(elem));
					break;
				case "lauthor":
					business.setLauthor((String) mapBusiness.get(elem));
					break;
				case "ldate":
					business.setLdate((String) mapBusiness.get(elem));
					break;
				case "categories":
					business.setCategories((List<String>) mapBusiness.get(elem));
					break;
				case "others":
					if (mapBusiness.get(elem) instanceof Map)
						business.setOthers((Map<String, Object>) mapBusiness.get(elem));
					break;
				default:
					if (business.getOthers() == null)
						business.setOthers(new HashMap<String, Object>());
					if (!business.getOthers().containsKey(attribute))
						business.getOthers().put(attribute, mapBusiness.get(elem));

				}
			}
		}
		return business;

	}

}
