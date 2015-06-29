package it.unisalento.idalab.osgi.user.persistence.api;

import it.unisalento.idalab.osgi.user.api.User;

import java.util.List;
import java.util.Map;

public interface UserServicePersistence {

	List<User> listUsers();
	User getUserByEmail(String email);
	User getUserByMobile(String mobile);
	User getUserByUsername(String username);
	void updateUser(User user);
	void removeUser(User user);
	Map<String, Object> login(Map<String, Object> user);
	Map<String, Object> saveUser(User user);
	Map<String, Object> validateUsername(String userId, String username);
	Map<String, Object> validateEMail(String userId, String email);
	Map<String, Object> validateMobile(String userId, String mobile);

}
