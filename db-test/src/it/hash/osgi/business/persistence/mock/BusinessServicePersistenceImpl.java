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




public class BusinessServicePersistenceImpl implements BusinessServicePersistence,ManagedService{
	
	List<Business> businesses = new ArrayList<Business>();
		
	@SuppressWarnings("rawtypes")
	Dictionary properties;
	
	public BusinessServicePersistenceImpl(){
		Business b = new Business();
		b.set_id("OK");
		businesses.add(new Business());
	}
	
	@Override
	public Map<String, Object> addBusiness(Business business) {
		businesses.add(business);
		
		return new TreeMap<String, Object>();
	}

	@Override
	public Map<String, Object> addBusiness(Map<String, Object> mapbusiness) {
		
		
    	Business business = new Business();
	//	sendParameters(properties,business);
	
	
		System.out.println("Add Business for cfg: ");
		return addBusiness(business);
	}

	@Override
	public Map<String, Object> getBusiness(Business business) {
	
		
		return new TreeMap<String, Object>();
	}

	@Override
	public Map<String, Object> getConstrainedBusiness(Business business) {
		return new TreeMap<String, Object>();
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return new TreeMap<String, Object>();
	}

	@Override
	public Map<String, Object> getConstrainedBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return new TreeMap<String, Object>();
	}

	@Override
	public List<Business> getBusinesses() {
		return businesses;
	}

	@Override
	public Business getBusinessByEmail(String email) {
		if(!StringUtils.isEmptyOrNull(email)){
			for(Iterator<Business> it = businesses.iterator();it.hasNext();){
				Business business = it.next();
				if(email.equals(business.getEmail()))
					return business;
			}
		}

		return null;
	}

	@Override
	public Business getBusinessByMobile(String mobile) {
		if(!StringUtils.isEmptyOrNull(mobile)){
			for(Iterator<Business> it = businesses.iterator();it.hasNext();){
				Business business = it.next();
				if(mobile.equals(business.getMobile()))
					return business;
			}
		}

		return null;
	}

	@Override
	public Business getBusinessByBusinessname(String businessname) {
		if(!StringUtils.isEmptyOrNull(businessname)){
			for(Iterator<Business> it = businesses.iterator();it.hasNext();){
				Business business = it.next();
				if(businessname.equals(business.getCompanyname()))
					return business;
			}
		}

		return null;
	}

	@Override
	public Business getBusinessById(String businessId) {
		if(!StringUtils.isEmptyOrNull(businessId)){
			for(Iterator<Business> it = businesses.iterator();it.hasNext();){
				Business business = it.next();
				if(businessId.equals(business.get_id()))
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
		return null;
	}

	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteBusiness(Business business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> login(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> loginByOAuth2(Map<String, Object> business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateBusinessname(String businessId, String businessname) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateEMail(String businessId, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateMobile(String businessId, String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImplementation() {
		return "mocked";
	}

	
	
	public void sendParameters(@SuppressWarnings("rawtypes") Dictionary properties,String b)
	{
		System.out.println(" PARAMETRI BUSINESS_MOCK");
		Map<String, String> parameters = new HashMap<String, String>();
		
		parameters.put("company_1",(String) properties.get("company_1"));
		
	/*	b.setCompanyname((String)parameters.get("businesscompanyname"));
		b.setPassword((String)parameters.get("password"));
		b.setEmail((String)parameters.get("email"));
		b.setMobile((String)parameters.get("mobile"));*/
		
	
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {

	}

	@SuppressWarnings("unused")
	private Business convert(String s){
		
		return new Business();
	}
	


}
