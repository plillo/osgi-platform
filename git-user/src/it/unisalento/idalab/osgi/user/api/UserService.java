package it.unisalento.idalab.osgi.user.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
	Map<String, Object> login(String username, String password);
	Map<String, Object> login(Map<String, Object> pars);
	Map<String, Object> createUser(User user);
	List<User> listUsers();
	Map<String,Object> testToken(String token);
	Map<String, Object> validateUsername(String userId, String username);
	Map<String, Object> validateEMail(String userId, String email);
	Map<String, Object> validateMobile(String userId, String mobile);
	Map<String, Object> validateIdentificator(String identificator);
	void createUsersByCSV(BufferedReader reader, boolean simulation, boolean activation) throws IOException;
}
