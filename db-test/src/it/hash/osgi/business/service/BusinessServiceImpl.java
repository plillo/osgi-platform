package it.hash.osgi.business.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.jwt.service.JWTService;
import it.hash.osgi.user.password.Password;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.business.service.Validator;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

public class BusinessServiceImpl implements BusinessService, ManagedService {

	@SuppressWarnings({ "unused", "rawtypes" })
	
	private Dictionary properties;
	private volatile BusinessServicePersistence _businessPersistenceService;
	private volatile Password _passwordService;
	private volatile EventAdmin _eventAdminService;
	private volatile JWTService _jwtService;
	
	@SuppressWarnings("unused")
	private Validator validator = new Validator();
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> login(String businessname, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> login(Map<String, Object> pars) {
		Map<String, Object> response = new TreeMap<String, Object>();
		// GET business
		Map<String, Object> businessSearch = _businessPersistenceService.getBusiness(pars);
		boolean matched = false;

		// Build the response
		if((int)businessSearch.get("matched")>0){
			Business business = (Business)businessSearch.get("business");
			// MATCHED business
			String password = (String)pars.get("password");
			try {

				matched = _passwordService.check(password, business.getPassword());

				response.put("id", business.get_id());
				Map<String,Object> map = new TreeMap<String, Object>();
				map.put("subject", "businessId");
				map.put("body", business.get_id());
				String token = _jwtService.getToken(map);
				response.put("token", token);
				
				// Trigging system event: 'business/login'
				Map<String,Object> event_props = new HashMap<>();
				event_props.put("token", token);
				event_props.put("id", business.get_id());
				event_props.put("verified", matched);
				_eventAdminService.sendEvent(new Event("business/login", event_props));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			response.put("message", "Unknown identificator");
		}
		
		response.put("verified", matched);
		
		return response;
	}

	@Override
	public Map<String, Object> loginByOAuth2(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> listBusinesss() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> testToken(String token) {
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
	public Map<String, Object> validateIdentificator(String identificator) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String generatePassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getBusiness(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> create(Business business) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createBusinesssByCSV(BufferedReader reader, boolean simulation,
			boolean activation) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> getBusinessDetails(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> searchBusinesss(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Business> getBusinesss() {
		// TODO Auto-generated method stub
		System.out.println("getBusiness");
		return null;
	}

	@Override
	public void addBusinesssByCSV(BufferedReader reader, boolean simulation,
			boolean activation) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> addBusiness(Business business) {
		// TODO Auto-generated method stub
		return null;
	}

}
