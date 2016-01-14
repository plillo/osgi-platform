package it.hash.osgi.business.service;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;

public class BusinessServiceImpl implements BusinessService, ManagedService {
	private volatile UUIDService _uuid;

	@SuppressWarnings({ "unused", "rawtypes" })
	       
	private Dictionary properties;
	private volatile BusinessServicePersistence _businessPersistenceService;
	@SuppressWarnings("unused")
	private volatile EventAdmin _eventAdminService;
	
	
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> pars) {
		return _businessPersistenceService.getBusiness(pars);
	}


	@Override
	public Map<String, Object> create(Business business) {
        business.setUUID(_uuid.createUUID("app/business"));
		return _businessPersistenceService.addBusiness(business);
	}

	@Override
	public Map<String, Object> create(Map<String, Object> pars) {
        pars.put("uuid",(_uuid.createUUID("app/business")));
		return _businessPersistenceService.addBusiness(pars);
	}

	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return _businessPersistenceService.deleteBusiness(pars);
	}

	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> pars) {

		return _businessPersistenceService.updateBusiness((Business)pars.get("business"));
	}


	@Override
	public List<Business> retrieveBusinesses(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Business> getBusinesses() {
	
		return _businessPersistenceService.getBusinesses();
	}


}
