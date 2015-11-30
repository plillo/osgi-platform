package it.hash.osgi.user.persistence.shell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import it.hash.osgi.user.User;
import it.hash.osgi.user.persistence.api.UserServicePersistence;
import it.hash.osgi.utils.StringUtils;

public class Commands {
	
	private volatile UserServicePersistence persistence;

	public void list() {
		List<User> users = persistence.getUsers();
		
		if(users!=null){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				System.out.println(String.format("%-20s%-20s", StringUtils.defaultIfNullOrEmpty(user.getUsername(),"#"), StringUtils.defaultIfNullOrEmpty(user.getEmail(),"#")));
			}
		}
	}

	public void adduser(String username, String password, String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setMobile(mobile);
		persistence.addUser(user);
	}
	
	public void validate(String userId,String username) {
		persistence.validateUsername(userId, username);
	}

	public void getusermail(String email) {
		persistence.getUserByEmail(email);
	}

	public void removeuser(String username, String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setMobile(mobile);;
		user.setEmail(email);
		persistence.deleteUser(user);
	}
	
	public void login(String username,String password, String email, String mobile) {
		HashMap<String,Object> hm = new HashMap<String,Object> ();
		
		hm.put("username", username);
		hm.put("password", password);
		hm.put("mobile", mobile);
		hm.put("email", email);
		
		persistence.login(hm);
	}
	
	public void loginOA(String email,String password, String firstName, String lastName) {
		HashMap<String,Object> hm = new HashMap<String,Object> ();
		
		hm.put("firstName", firstName);
		hm.put("password", password);
		hm.put("lastName", lastName);
		hm.put("email", email);
		
		persistence.loginByOAuth2(hm);
	}

	public void updateuser(String username,String password ,String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);
		user.setEmail(email);
		persistence.updateUser(user);
	}
	
	public void getuser(String username,String password ,String email, String mobile) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setMobile(mobile);;
		user.setEmail(email);
		persistence.getUser(user);
	}

	public void impl() {
		System.out.println(persistence.getImplementation());
	}
}
