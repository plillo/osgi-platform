package it.hash.osgi.business.persistence.mock;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.utils.StringUtils;

     
public class BusinessServicePersistenceImpl implements BusinessServicePersistence, ManagedService {

	List<Business> businesses = new ArrayList<Business>();
	@SuppressWarnings("rawtypes")
	Dictionary properties;
    private volatile UUIDService _uuid;
	
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
			case "__description":
				business.set__Description(value);
				break;	
			case "__longDescription":
				business.set__Description(value);
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
			case "others":
				business.setOthers((Map<String, Object>) entry.getValue());
				break;

			}
		//	System.out.println(entry.getKey() + "/" + entry.getValue());
		}

		return business;

	}

	@Override
	public Map<String, Object> addBusiness(Business business) {
      String uuid=_uuid.createUUID(business.get_id());
		Map<String, Object> map = new TreeMap<String, Object>();
	
		business.setUUID(uuid);
		map.put("Result", businesses.add(business));
		map.put("created", "true");
		map.put("businessId", business.get_id());
		return map;
	}

	@Override
	public Map<String, Object> addBusiness(Map<String, Object> mapbusiness) {
		System.out.println("Add Business for cfg: ");
		return addBusiness(createBusiness(mapbusiness));
	}

	
	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {


		Business businessObj = new Business();

		if (StringUtils.isNotEmptyOrNull((String) business.get("businessId")))
			businessObj.set_id((String) business.get("businessId"));
		if (StringUtils.isNotEmptyOrNull((String) business.get("email")))
			businessObj.setEmail((String) business.get("email"));
		if (StringUtils.isNotEmptyOrNull((String) business.get("mobile")))
			businessObj.setMobile((String) business.get("mobile"));
	    
		return getBusiness(businessObj);
	}
@Override
	public Map<String,Object>  getBusiness(Business business){
		Map<Business, TreeSet<String>> matchs = new TreeMap<Business, TreeSet<String>>();
		Map<String,Object> response = new HashMap<String,Object>();
		Business found_business = null;
		
			if(business.get_id()!=null) {
			found_business = getBusinessById(business.get_id());

			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("businessId");
				matchs.put(found_business, list);
			}
		}
		
		if(business.getBusinessName()!=null) {
			found_business = getBusinessByBusinessName(business.getBusinessName());

			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("busienssname");
				matchs.put(found_business, list);
			}
		}
		
		if(business.getEmail()!=null) {
			found_business= getBusinessByEmail(business.getEmail());
			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("email");
				matchs.put(found_business, list);
			}
		}
		if(business.getMobile()!=null) {
			found_business= getBusinessByMobile(business.getMobile());
			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("mobile");
				matchs.put(found_business, list);
			}
		}
		// Set response: number of matched users
					response.put("matched", matchs.size());

					// Set response details
					switch(matchs.size()){
					case 0:
						response.put("found", false);
						response.put("returnCode",400);
						break;
					case 1:
						Business key = (Business) matchs.keySet().toArray()[0];
						response.put("business", key);
						response.put("keys", matchs.get(key));
						response.put("found", true);
						response.put("returnCode",200);
						break;
					default:
						response.put("businsess", matchs);
						response.put("returnCode", 110);
					}
				
						
				return response;
			}
	@Override
	public List<Business> getBusinesses() {
System.out.println(this.getImplementation());
		return businesses;
	}

	@Override
	public Business getBusinessByEmail(String email) {
		if (!StringUtils.isEmptyOrNull(email)) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				if (email.equals(business.getEmail()))
					return business;
			}
		}
		return null;
	}

	@Override
	public Business getBusinessByMobile(String mobile) {
		if (!StringUtils.isEmptyOrNull(mobile)) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				if (mobile.equals(business.getMobile()))
					return business;
			}
		}

		return null;
	}

	@Override
	public Business getBusinessByBusinessName(String businessName) {
		System.out.println();
		if (!StringUtils.isEmptyOrNull(businessName)) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				if (businessName.equals(business.getBusinessName()))
					return business;
			}
		}

		return null;
	}

	@Override
	public Business getBusinessById(String businessId) {
		if (!StringUtils.isEmptyOrNull(businessId)) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				if (businessId.equals(business.get_id()))
					return business;
			}
		}

		return null;
	}

	@Override
	public List<Business> getBusinessDetails(Business business) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map<String, Object> updateBusiness(Business business) {

		Map<String, Object> result = new TreeMap<String, Object>();
		if (businesses.contains(business)) {
			businesses.remove(business);
			businesses.add(business);
			result.put("result", "true");
			result.put("Update", business.get_id());
		} else
			result.put("result", "false");

		return result;
	}

	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> par) {

		System.out.println("Update Business for cfg: ");
		
		Business business = createBusiness(par);

		return updateBusiness(business);

	}

	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> pars) {

		Business foundBusiness = createBusiness(pars);
		Map<String, Object> result = new TreeMap<String, Object>();
		result.put("returnCode", 620);
		for (Business business : businesses) {
			if (foundBusiness.compareTo(business) == 0)  {
				businesses.remove(business);
				result.remove("returnCode");
				result.put("returnCode", 200);
				result.put("delete", foundBusiness);
				return result;
			}
		}

		return result;
	}



	@Override
	public String getImplementation() {
		return "mocked";
	}

	private Business createBusiness(String b) {
		Business business = new Business();
		String[] company = b.split("&");
		for (String s : company) {
			String attribute[] = s.split("=");
			if (attribute[0].equals("_id"))
				business.set_id(attribute[1]);
			if (attribute[0].equals("username"))
				business.setBusinessName(attribute[1]);
			if (attribute[0].equals("email"))
				business.setEmail(attribute[1]);
			if (attribute[0].equals("mobile"))
				business.setMobile(attribute[1]);
		}

		return business;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties != null) {

			businesses.add(createBusiness(((String) properties.get("Business_01"))));
			businesses.add(createBusiness(((String) properties.get("Business_02"))));
			this.properties = properties;
		}
	}

	



}
