package it.unisalento.idalab.osgi.user.commands.persistence;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserServicePersistence;



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
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);
		user.setEmail(email);
		userservice.login(user);
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
