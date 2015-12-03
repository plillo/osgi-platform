package it.hash.osgi.user.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.jwt.service.JWTService;
import it.hash.osgi.user.User;
import it.hash.osgi.user.password.Password;
import it.hash.osgi.user.persistence.api.UserServicePersistence;

public class UserServiceImpl implements UserService, ManagedService{
	@SuppressWarnings({ "unused", "rawtypes" })
	private Dictionary properties;
	private volatile UserServicePersistence _userPersistenceService;
	private volatile Password _passwordService;
	private volatile EventAdmin _eventAdminService;
	private volatile JWTService _jwtService;
	
	private Validator validator = new Validator();

	@Override
	public Map<String, Object> login(String identificator, String password) {
		return null;
	}

	@Override
	public Map<String, Object> login(Map<String, Object> pars) {
		Map<String, Object> response = new TreeMap<String, Object>();
		// GET user
		Map<String, Object> userSearch = _userPersistenceService.getUser(pars);
		boolean matched = false;

		// Build the response
		if((int)userSearch.get("matched")>0){
			User user = (User)userSearch.get("user");
			// MATCHED user
			String password = (String)pars.get("password");
			try {

				matched = _passwordService.check(password, user.getPassword());

				response.put("id", user.get_id());
				Map<String,Object> map = new TreeMap<String, Object>();
				map.put("subject", "userId");
				map.put("body", user.get_id());
				String token = _jwtService.getToken(map);
				response.put("token", token);
				
				// Trigging system event: 'user/login'
				Map<String,Object> event_props = new HashMap<>();
				event_props.put("token", token);
				event_props.put("id", user.get_id());
				event_props.put("verified", matched);
				_eventAdminService.sendEvent(new Event("user/login", event_props));
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
	public List<User> getUsers() {
		return _userPersistenceService.getUsers();
	}

	@Override
	public Map<String, Object> testToken(String token) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateUsername(String userId, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateEMail(String userId, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateMobile(String userId, String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateIdentificator(String identificator) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("identificatorType", "unmatched");
		map.put("isValid", true);
		
		if(validator.isValidEmail(identificator))
			map.put("identificatorType", "email");
		else if(validator.isValidMobile(identificator))
			map.put("identificatorType", "mobile");
		else if(validator.isValidUsername(identificator))
			map.put("identificatorType", "username");
		else
			map.put("isValid", false);

		return map;
	}

	@Override
	public String generatePassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> addUser(User user) {
		return _userPersistenceService.addUser(user);
	}

	@Override
	public void addUsersByCSV(BufferedReader reader, boolean simulation, boolean activation) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> deleteUser(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateUser(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getUser(Map<String, Object> pars) {
		Map<String, Object> response = _userPersistenceService.getUser(pars);
		
		return response;
	}

	@Override
	public List<User> searchUsers(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		this.properties = properties;
		
		validator.setProperties(properties);
	}

	@Override
	public List<User> listUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> create(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createUsersByCSV(BufferedReader reader, boolean simulation, boolean activation) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<User> getUserDetails(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> update(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}