package it.hash.osgi.user.shell;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.hash.osgi.user.User;
import it.hash.osgi.user.service.UserService;


public class Commands {
	private volatile UserService _userService;

	public void login(String username, String password) {
		System.out.println("token: "+_userService.login(username, password));
	}
	
	public void add(String username, String firstname, String lastname, String password) {
		User user = new User();
		user.setUsername(username);
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setPassword(password);
		
		Map<String, Object> ret = _userService.createUser(user);
		System.out.println("called shell command 'createUser' - created: "+(Boolean) ret.get("created"));
	}

	public void number() {
		System.out.println("users number: "+_userService.getUsers().size());
	}
	
	public void list() {
		List<User> users = _userService.getUsers();
		
		if(users!=null){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				System.out.println(String.format("%-20s%-20s", user.getUsername(), user.getEmail()));
			}
		}
	}
	
	public void UUID() {
		String UUID = _userService.getUUID();
		System.out.println(String.format("Current user UUID: %s",UUID==null?"NO ONE":UUID));
	}
	
}
