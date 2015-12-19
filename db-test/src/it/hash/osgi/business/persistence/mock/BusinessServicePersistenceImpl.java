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
import it.hash.osgi.utils.StringUtils;

public class BusinessServicePersistenceImpl implements BusinessServicePersistence, ManagedService {

	List<Business> businesses = new ArrayList<Business>();
	@SuppressWarnings("rawtypes")
	Dictionary properties;

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

	@Override
	public Map<String, Object> addBusiness(Business business) {

		Map<String, Object> map = new TreeMap<String, Object>();
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
		// TODO Auto-generated method stub

		Business businessObj = new Business();

		if (StringUtils.isNotEmptyOrNull((String) business.get("businessId")))
			businessObj.set_id((String) business.get("businessId"));
		if (StringUtils.isNotEmptyOrNull((String) business.get("username")))
			businessObj.setUsername((String) business.get("username"));
		if (StringUtils.isNotEmptyOrNull((String) business.get("email")))
			businessObj.setEmail((String) business.get("email"));
		if (StringUtils.isNotEmptyOrNull((String) business.get("mobile")))
			businessObj.setMobile((String) business.get("mobile"));

		Map<String, Object> businessFound = getBusiness(businessObj);
         
		return businessFound;
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
		
		if(business.getBusinessname()!=null) {
			found_business = getBusinessByBusinessname(business.getBusinessname());

			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("busienssname");
				matchs.put(found_business, list);
			}
		}
		if(business.getUsername()!=null) {
			found_business = getBusinessByBusinessname(business.getUsername());

			if(found_business!=null){
				TreeSet<String> list = matchs.get(found_business);
				if(list==null)
					list = new TreeSet<String>();
				    
				list.add("username");
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
	public Business getBusinessByBusinessname(String businessname) {
		System.out.println();
		if (!StringUtils.isEmptyOrNull(businessname)) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				if (businessname.equals(business.getBusinessname()))
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

	private Business machBusiness(Business old, Business New) {
		// in teoria dovrei confrontare ogni attributo e sostituire solo quelli
		// modificati

		return New;
	}

	@Override
	public Map<String, Object> updateBusiness(Business business) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		System.out.println("Update Business for cfg: ");
		Business oldBusiness = businesses.get(Integer.parseInt((String) par.get("id")));
		Business newBusiness = (Business) par.get("newBusiness");

		return updateBusiness(machBusiness(oldBusiness, newBusiness));

	}

	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		Business b = createBusiness(business);
		Map<String, Object> result = new TreeMap<String, Object>();
		result.put("result", "false");
		for (Business element : businesses) {
			if (b.compareTo(element) == 0) {
				businesses.remove(element);
				result.remove("result");
				result.put("result", "true");
				result.put("delete", b.get_id());
				break;
			}
		}

		return result;
	}

	@Override
	public Map<String, Object> login(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
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
				business.setBusinessname(attribute[1]);
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

	@SuppressWarnings("unused")
	private Business convert(String s) {

		return new Business();
	}

}
