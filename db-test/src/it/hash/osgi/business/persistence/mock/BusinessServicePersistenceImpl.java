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
	// METODO CHE INSTANZIA UN BUSINESS....ANCHE SE QUESTA RESPONSABILITA' IO LA
	// DAREI A BusinessServiceImpl

	private Business createBusiness(Map<String, Object> mapbusiness) {
	
		// ..  abbiamo detto che in un database Nosql non si ha uno schema fisso per cui
		//  dobbiamo controllare quali attributi andranno settati!!!
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
		map.put("Id",business.get_id());
		return map;
	}

	@Override
	public Map<String, Object> addBusiness(Map<String, Object> mapbusiness) {
		System.out.println("Add Business for cfg: ");
		return addBusiness(createBusiness(mapbusiness));
	}

	@Override
	public Map<String, Object> getBusiness(Business business) {
		Map<String, Object> result = new TreeMap<String, Object>();
	    result.put("Result", "False");
		if (businesses.contains(business)) {
			for (Business element : businesses) {
				if (element.compareTo(business)==0) {
					result.remove("Result");
					result.put("Result ", "True");
					result.put("Business", element);
					break;
				}
			}
		}
		return result;
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new TreeMap<String, Object>();
		Business businessObj=null;
		 result.put("Result", "False");
		 
	
		if (StringUtils.isNotEmptyOrNull((String) business.get("Id"))){
					businessObj= getBusinessById((String) business.get("Id"));}
		
		result.remove("Result");
		result.put("Result ", "True");
		result.put("Business", businessObj);
		
		
		return result;
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
		for(Business element: businesses){
			if (b.compareTo(element)==0){
			businesses.remove(element);
			result.remove("result");
			result.put("result", "true");
			result.put("delete", b.get_id());
			break;}
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

private Business prova(String b){
	Business business=new Business();
	String[]  company=b.split("&");
	for (String s: company){
		String attribute[]= s.split("=");
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
           if (properties!=null){
        	   
         	
        	   businesses.add(prova(((String) properties.get("Business_01"))));
        	   businesses.add(prova(((String) properties.get("Business_02"))));
        	  this.properties=properties;
             }
	}

	@SuppressWarnings("unused")
	private Business convert(String s) {

		return new Business();
	}

}
