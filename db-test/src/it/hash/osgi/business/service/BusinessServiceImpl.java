package it.hash.osgi.business.service;

import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;

public class BusinessServiceImpl implements BusinessService, ManagedService {

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
		// TODO Auto-generated method stub
		// nei pars controllo su quale attributo recuperare il Business
		return _businessPersistenceService.getBusiness(pars);
	}


	@Override
	public Map<String, Object> create(Business business) {
		// TODO Auto-generated method stub
		
		return 	_businessPersistenceService.addBusiness(business);
	
	}


	@Override
	public Map<String, Object> create(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return _businessPersistenceService.addBusiness(pars);
	}



	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return _businessPersistenceService.deleteBusiness(pars);
	}


	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return _businessPersistenceService.updateBusiness(pars);
	}


	@Override
	public List<Business> retrieveBusinesses(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Business> getBusinesses() {
		// TODO Auto-generated method stub
		return _businessPersistenceService.getBusinesses();
	}


}
