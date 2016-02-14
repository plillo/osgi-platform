package it.hash.osgi.business.service;

import java.util.ArrayList;
import java.util.Dictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.osgi.service.event.EventAdmin;

import com.amazonaws.util.StringUtils;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.business.service.api.BusinessService;
import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.user.User;
import it.hash.osgi.user.service.UserService;

public class BusinessServiceImpl implements BusinessService {
	
	@SuppressWarnings({ "unused", "rawtypes" })

	private Dictionary properties;
	private volatile BusinessServicePersistence _businessPersistenceService;
	private volatile UUIDService _uuid;
	private volatile UserService _userSrv;

	@SuppressWarnings("unused")
	private volatile EventAdmin _eventAdminService;

	
	@Override
	public Map<String, Object> getBusiness(Map<String, Object> pars) {
		return _businessPersistenceService.getBusiness(pars);
	}

	@Override
	public Map<String, Object> create(Business business) {
		// TODO IMPLEMENTARE MEGLIO L 'INTEGRITà REFERENZIALE TRA LE DUE
		// TABELLE!!!!
		Map<String, Object> response = new HashMap<String, Object>();
		String u = _uuid.createUUID("app/business");
		if (!StringUtils.isNullOrEmpty(u)) {
			business.setUuid(u);

			response = _businessPersistenceService.addBusiness(business);
			if ((Boolean) response.get("created") == false) 
				_uuid.removeUUID(u);

		} else {
			// associo il business al user!!!!! 
	//		 String userUUID = _userSrv.getUUID();
	//		 User user = (User) _userSrv.getUserByUuid(userUUID);
	//		 _userSrv.updateUser(addBusinessToUser(business,user));

			response.put("created", false);
			response.put("returnCode", 630);

		}
		return response;
	}

	@Override
	public Map<String, Object> create(Map<String, Object> pars) {
		String u = _uuid.createUUID("app/business");
		Map<String, Object> response = new HashMap<String, Object>();

		if (!StringUtils.isNullOrEmpty(u)) {
			pars.put("uuid", u);
			response = _businessPersistenceService.addBusiness(pars);

			if ((Boolean) response.get("created") == false) 
				_uuid.removeUUID(u);

				// TODO INTEGRITA' REFERENZIALE

			
		} else {

			response.put("created", false);
			response.put("returnCode", 630);

		}
		return response;
	}

	@Override
	public Map<String, Object> deleteBusiness(Map<String, Object> pars) {
		Map<String, Object> response = new HashMap<String, Object>();

		String u = (String) pars.get("uuid");
		if (!StringUtils.isNullOrEmpty(u)) {

			response = _uuid.removeUUID(u);

			return _businessPersistenceService.deleteBusiness(pars);
		} else {
			response.put("created", false);
			response.put("errorUUIDService", true);
			response.put("returnCode", 630);
			return response;
		}

	}

	@Override
	public Map<String, Object> updateBusiness(Map<String, Object> pars) {

		return _businessPersistenceService.updateBusiness(pars);
	}

	@Override
	public List<Business> retrieveBusinesses(String criterion, String search) {
		if(_uuid.isUUID(search)){
			List<Business> list = new ArrayList<Business>();
			list.add(_businessPersistenceService.getBusinessByUuid(search));
			return list;
		}
			else
				return _businessPersistenceService.retrieveBusinesses(criterion, search);
	}

	@Override
	public List<Business> getBusinesses() {

		return _businessPersistenceService.getBusinesses();
	}

	private Map <String,Object> addBusinessToUser(Business business, User user) {
	    Map <String,Object> update = new HashMap<String,Object>();
	    

			if (!user.getExtra().containsKey("business")) {
				List<String> bs = new ArrayList<String>();
				bs.add(business.getUuid());
				user.setExtra("business", bs);
		
			} else {
				List<String> bs = (List<String>) user.getExtra("business");
				bs.add(business.getUuid());
				user.setExtra("business", bs);
		
			}
			
			update.put("user", user);
		return update;
		
		}

}
