package it.unisalento.idalab.osgi.user.api;

import java.util.HashMap;
import java.util.List;


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

}
