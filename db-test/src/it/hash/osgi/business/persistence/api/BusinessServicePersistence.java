package it.hash.osgi.business.persistence.api;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;
public interface BusinessServicePersistence {
	// CREATE
	// addBusiness
	Map<String, Object> addBusiness(Map<String, Object> business);
	Map<String, Object> addBusiness(Business business);
	
	// READ
	Map<String, Object> getBusiness(Business business);
	Map<String, Object> getBusiness(Map<String, Object> business);
	
	Business getBusinessByEmail(String email);
	Business getBusinessByMobile(String mobile);
	Business getBusinessByBusinessname(String businessname);
	Business getBusinessById(String businessId);
	
	List<Business> getBusinesses();
	List<Business> getBusinessDetails(Business business);
	
	// UPDATE
	Map<String, Object> updateBusiness(Business business);
	Map<String, Object> updateBusiness(Map<String, Object> business);
	
	// DELETE
	Map<String, Object> deleteBusiness(Map<String, Object> business);
	
	// LOGIN
	Map<String, Object> login(Map<String, Object> business);
	
	// IMPLEMENTATION
	String getImplementation();

}
