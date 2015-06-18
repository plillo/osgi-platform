package it.unisalento.idalab.osgi.user.commands;

import java.util.Map;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserService;

public class Commands {
	private volatile UserService _userService;

	public void login(String username, String password) {
		System.out.println("token: "+_userService.login(username, password));
     
	}
	
	public void testtoken(String token) {
		System.out.println("token: "+_userService.testToken(token));
	}
	
	public void create(String username, String firstname, String lastname, String password) {
		User user = new User();
		user.setUsername(username);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setPassword(password);
		
		Map<String, Object> ret = _userService.createUser(user);
		String status = (String) ret.get("status");
		System.out.println("called shell command 'createUser' - execution status: "+status);
	}

	public void list() {
		System.out.println("users number: "+_userService.listUsers().size());
	}
	
}
