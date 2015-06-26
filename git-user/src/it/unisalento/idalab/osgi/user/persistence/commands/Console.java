package it.unisalento.idalab.osgi.user.persistence.commands;

import java.util.HashMap;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.persistence.api.UserServicePersistence;



public class Console {
	
	private volatile UserServicePersistence userservice;

	public void listuser() {
		userservice.listUsers();	
	}

	public void adduser(String username, String password, String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setMobile(mobile);
		userservice.saveUser(user);
	}
	
	public void validateU(String userId,String username) {
		
		userservice.validateUsername(userId, username);
	}

	public void getusermail(String email) {
		userservice.getUserByEmail(email);
	}

	public void removeuser(String username, String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setMobile(mobile);;
		user.setEmail(email);
		userservice.removeUser(user);
	}
	
	public void login(String username,String password, String email, String mobile) {
		
		HashMap<String,Object> hm = new HashMap<String,Object> ();
		
		hm.put("username", username);
		hm.put("password", password);
		hm.put("mobile", mobile);
		hm.put("email", email);
		
		userservice.login(hm);
	}
	

	public void updateuser(String username,String password ,String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);;
		user.setEmail(email);
		userservice.updateUser(user);
	}

}
