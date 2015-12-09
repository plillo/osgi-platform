package it.hash.osgi.business.persistence.api;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;
public interface BusinessServicePersistence {
	// CREATE
	Map<String, Object> addBusiness(Business business);
	Map<String, Object> addBusiness(Map<String, Object> business);
	
	// READ
	Map<String, Object> getConstrainedBusiness(Map<String, Object> business);
	Map<String, Object> getBusiness(Business business);
	Map<String, Object> getConstrainedBusiness(Business business);
	Map<String, Object> getBusiness(Map<String, Object> business);
	
	
	Business getBusinessByEmail(String email);
	Business getBusinessByMobile(String mobile);
	Business getBusinessByBusinessname(String businessname);
	Business getBusinessById(String businessId);
	
	List<Business> getBusinesss();
	List<Business> getBusinessDetails(Business business);
	
	// UPDATE
	Map<String, Object> updateBusiness(Business business);
	Map<String, Object> updateBusiness(Map<String, Object> business);
	
	// DELETE
	Map<String, Object> deleteBusiness(Business business);
	
	// LOGIN
	Map<String, Object> login(Map<String, Object> business);
	Map<String, Object> loginByOAuth2(Map<String, Object> business);
	
	// VALIDATE
	Map<String, Object> validateBusinessname(String businessId, String businessname);
	Map<String, Object> validateEMail(String businessId, String email);
	Map<String, Object> validateMobile(String businessId, String mobile);
	
	// IMPLEMENTATION
	String getImplementation();

}
