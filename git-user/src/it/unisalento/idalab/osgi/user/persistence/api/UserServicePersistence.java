package it.unisalento.idalab.osgi.user.persistence.api;

import it.unisalento.idalab.osgi.user.api.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface UserServicePersistence {
	
	List<User> listUsers();
	User getUserByEmail(String email);
	User getUserByMobile(String mobile);
	User getUserByUsername(String username);
	void removeUser(User user);
	UserPersistenceResponse saveUser(User user);
	void updateUser(User user);
	User login(User user);
	User login(HashMap<String, Object> user);
	Map<String, Object> validateUsername(String userId, String username);
	Map<String, Object> validateEMail(String userId, String email);
	Map<String, Object> validateMobile(String userId, String mobile);
}
