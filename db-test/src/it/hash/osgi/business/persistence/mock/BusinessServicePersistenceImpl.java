package it.hash.osgi.business.persistence.mock;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.utils.StringUtils;

public class BusinessServicePersistenceImpl implements BusinessServicePersistence, ManagedService {

	List<Business> businesses = new ArrayList<Business>();

	@SuppressWarnings("rawtypes")
	Dictionary properties;

	private Business createBusiness(Map<String, Object> mapbusiness) {

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
		map.put("Add", business.get_id());
		return map;
	}


	@Override
	public Map<String, Object> addBusiness(Map<String, Object> mapbusiness) {
		System.out.println("Add Business for cfg: ");
		return addBusiness(createBusiness(mapbusiness));
	}

	@Override
	public Map<String, Object> getBusiness(Business business) {
		return new TreeMap<String, Object>();
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return new TreeMap<String, Object>();
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

	@Override
	public Map<String, Object> updateBusiness(Business business) {
		// TODO Auto-generated method stub
		Map<String,Object> result = new TreeMap<String,Object>();
		if (businesses.contains(business)){
			businesses.remove(business);
			businesses.add(business);
			result.put("result", "true");
			result.put("Update",business.get_id() );
		}
		else 
			result.put("result", "false");
		
		return result;
	}

	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		Business b = createBusiness(business);
		Map<String,Object> result = new TreeMap<String,Object>();
		if (businesses.contains(b)){
			String id=b.get_id();
			businesses.remove(b);
			result.put("result", "true");
			result.put("delete", id);
		}
		else 
			result.put("result", "false");
		
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

	public void sendParameters(@SuppressWarnings("rawtypes") Dictionary properties, String b) {
		System.out.println(" PARAMETRI BUSINESS_MOCK");
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("company_1", (String) properties.get("company_1"));

		/*
		 * 
		 * b.setPassword((String)parameters.get("password"));
		 * b.setEmail((String)parameters.get("email"));
		 * b.setMobile((String)parameters.get("mobile"));
		 */

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {

	}

	@SuppressWarnings("unused")
	private Business convert(String s) {

		return new Business();
	}

}
