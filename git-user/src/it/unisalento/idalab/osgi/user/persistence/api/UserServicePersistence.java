package it.unisalento.idalab.osgi.user.persistence.api;

import it.unisalento.idalab.osgi.user.api.User;

import java.util.List;
import java.util.Map;

public interface UserServicePersistence {
	// CREATE
	Map<String, Object> createUser(User user);
	Map<String, Object> createUser(Map<String, Object> user);
	
	// READ
	Map<String, Object> getUser(User user);
	Map<String,Object> getUser(Map<String, Object> user);
	Map<String,Object> getConstrainedUser(Map<String, Object> user);
	List<User> getUsers();
	User getUserByEmail(String email);
	User getUserByMobile(String mobile);
	User getUserByUsername(String username);
	User getUserByFirstName(String firstName);
	User getUserByLastName(String lastName);
	User getUserById(String userId);
	List<User> getUserDetails(User user);
	
	// UPDATE
	Map<String, Object> updateUser(User user);
	Map<String, Object> updateUser(Map<String, Object> user);
	
	// DELETE
	Map<String, Object> deleteUser(User user);
	
	// LOGIN
	Map<String, Object> login(Map<String, Object> user);
	Map<String, Object> loginByOAuth2(Map<String, Object> user);
	
	// VALIDATE
	Map<String, Object> validateUsername(String userId, String username);
	Map<String, Object> validateEMail(String userId, String email);
	Map<String, Object> validateMobile(String userId, String mobile);

}
