/**
 * Persistence Api
 * @author Montinari Antonella
 */
package it.hash.osgi.business.persistence.api;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;

/**
 * Provides interfaces for the management of the persistence of a business
 * @author Montinari Antonella
 */
public interface BusinessServicePersistence {
	
	// CREATE
	
	Map<String, Object> addBusiness(Map<String, Object> business);
	Map<String, Object> addBusiness(Business business);
	             
	// READ
	Map<String, Object> getBusiness(Business business);
	Map<String, Object> getBusiness(Map<String, Object> business);
	
	Business getBusinessByEmail(String email);
	Business getBusinessByMobile(String mobile);
	Business getBusinessByBusinessName(String businessName);
	Business getBusinessById(String businessId);
	Business getBusinessByUuid(String uuid);
	
	List<Business> getBusinesses();
	List<Business> getBusinessDetails(Business business);
	
	// UPDATE
	Map<String, Object> updateBusiness(Business business);
	Map<String, Object> updateBusiness(Map<String, Object> business);
	
	// DELETE
	Map<String, Object> deleteBusiness(Map<String, Object> business);
	
	// IMPLEMENTATION
	String getImplementation();

}
