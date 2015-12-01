package it.hash.osgi.user.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.user.User;
import it.hash.osgi.user.persistence.api.UserServicePersistence;

public class UserServiceImpl implements UserService, ManagedService{
	@SuppressWarnings({ "unused", "rawtypes" })
	private Dictionary properties;
	private volatile UserServicePersistence _persistence;
	
	private Validator validator = new Validator();

	@Override
	public Map<String, Object> login(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> login(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> loginByOAuth2(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUsers() {
		return _persistence.getUsers();
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
		return _persistence.addUser(user);
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
		return _persistence.getUser(pars);
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
