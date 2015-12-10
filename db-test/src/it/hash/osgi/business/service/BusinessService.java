package it.hash.osgi.business.service;

import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;

public interface BusinessService {
	List<Business> listBusinesss();
	Map<String, Object> getBusiness(Map<String, Object> pars);
	Map<String, Object> create(Business business);
	Map<String, Object> deleteBusiness(Map<String, Object> pars);
	Map<String, Object> updateBusiness(Map<String, Object> pars);
	List<Business> retrieveBusinesses(String parameter);
	List<Business> getBusinesses();
}
