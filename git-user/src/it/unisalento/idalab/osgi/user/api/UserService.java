package it.unisalento.idalab.osgi.user.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
	Map<String, Object> login(String username, String password);
	Map<String, Object> login(Map<String, Object> pars);
	Map<String, Object> loginByOAuth2(Map<String, Object> pars);
	List<User> listUsers();
	Map<String,Object> testToken(String token);
	Map<String, Object> validateUsername(String userId, String username);
	Map<String, Object> validateEMail(String userId, String email);
	Map<String, Object> validateMobile(String userId, String mobile);
	Map<String, Object> validateIdentificator(String identificator);
	String generatePassword();
	Map<String, Object> getUser(Map<String, Object> pars);
	Map<String, Object> create(User user);
	void createUsersByCSV(BufferedReader reader, boolean simulation, boolean activation) throws IOException;
	Map<String, Object> deleteUser(Map<String, Object> pars);
	Map<String, Object> updateUser(Map<String, Object> pars);
	List<User> getUserDetails(Map<String, Object> pars);
	List<User> searchUsers(String parameter);
	Map<String, Object> update(HttpServletRequest request);
}