package it.hash.osgi.user.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.jwt.service.JWTService;
import it.hash.osgi.user.User;
import it.hash.osgi.user.password.Password;
import it.hash.osgi.user.persistence.api.UserServicePersistence;
import it.hash.osgi.utils.StringUtils;

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
				// CHECK password
				matched = _passwordService.check(password, user.getPassword());
				
				// GET ROLES
				// TODO: get user's roles from system
				String roles = "reguser, admin, root";

				// PUT ID "Id"
				response.put("id", user.get_id());
				
				// Create a JWT (Jason Web Token)
				Map<String,Object> map = new TreeMap<String, Object>();
				map.put("subject", "userId");
				map.put("uid", user.get_id());
				map.put("roles", roles);
				map.put("body", "this is a access token");
				String token = _jwtService.getToken(map);
				
				// PUT JWT "token"
				response.put("token", token);
				
				// TRIG system event "user/login"
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
	public Map<String, Object> createUser(User user) {
		String password = user.getPassword();
		if(StringUtils.isEmptyOrNull(user.getPassword())){
			password = _passwordService.getRandom();
			user.setPassword(password);
		}
				
		try {
			user.setSalted_hash_password(_passwordService.getSaltedHash(password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _userPersistenceService.addUser(user);
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
	public String getUUID() {
		return _jwtService.getUID();
	}

	// getRoles METHODS
	// ................
	@Override
	public List<String> getRoles(String UUID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRoles(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRoles() {
		return _jwtService.getRoles();
	}

	// isUserInRole METHODS
	// ....................
	@Override
	public boolean isUserInRole(String UUID, String... roles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(User user, String... roles) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isUserInRole(String UUID, List<String> roles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(User user, List<String> roles) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUserInRole(String... roles) {
		// TODO Auto-generated method stub
		return false;
	}

}
