package it.unisalento.idalab.osgi.user.api;

import java.util.List;
import java.util.Map;

public interface UserService {
	Map<String,Object> login(String username, String password);
	Map<String,Object> createUser(User user);
	List<User> listUsers();
	Map<String,Object> testToken(String token);
}
