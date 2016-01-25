package it.hash.osgi.user.shell;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.hash.osgi.user.User;
import it.hash.osgi.user.service.UserService;


public class Commands {
	private volatile UserService _userService;

	public void login(String username, String password) {
		System.out.println("token: "+_userService.login(username, password));
	}
	
	public void create(String identificator, String firstname, String lastname, String password) {
		User user = new User();
		
		Map<String, Object> map = _userService.validateIdentificator(identificator);
		String identificator_type = (String)map.get("identificatorType");
		
		if((Boolean)map.get("isValid")) {
			// Get the user (if any) matching the identificator
			// ================================================
			map = new TreeMap<String, Object>();

			if("username".equals(identificator_type))
				user.setUsername(identificator);
			else if("email".equals(identificator_type))
				user.setEmail(identificator);
			else if("mobile".equals(identificator_type))
				user.setMobile(identificator);

			user.setFirstName(firstname);
			user.setLastName(lastname);
			user.setPassword(password);
		
			Map<String, Object> response = _userService.createUser(user);
			
			System.out.println("shell command 'user:create'");
			System.out.println(String.format("-> %s [%d]", response.get("message"), (Integer) response.get("status")));
		}
		else
			System.out.println("shell command 'user:create' - "+identificator+": not a valid identificator.");
	}

	public void number() {
		System.out.println("users number: "+_userService.getUsers().size());
	}
	
	public void list() {
		List<User> users = _userService.getUsers();
		
		if(users!=null){
			for(Iterator<User> it = users.iterator();it.hasNext();){
				User user = it.next();
				System.out.println(String.format("%-20s%-20s%-20s", user.getLastName(), user.getFirstName(), user.getEmail()));
			}
		}
	}
	
	public void UUID() {
		String UUID = _userService.getUUID();
		System.out.println(String.format("Current user UUID: %s",UUID==null?"NO ONE":UUID));
	}
	
}
