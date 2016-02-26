package it.hash.osgi.business.service.api;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;

public interface BusinessService {

	Map<String, Object> getBusiness(Map<String, Object> pars);
	Map<String, Object> create(Business business);
	Map<String, Object> create(Map<String, Object> pars);
	Map<String, Object> deleteBusiness(Map<String, Object> pars);
	Map<String, Object> updateBusiness(Map<String, Object> pars);
	Map<String, Object> updateFollowersToBusiness(Map<String, Object> pars);
	List<Business> retrieveBusinesses(String criterion, String search);
	List<Business> getBusinesses();
	Map<String, Object> followBusiness(String businessUuid, String actual_user_uuid);
	List<Business> retrieveFollowedBusinesses(String uuid);
	

}
