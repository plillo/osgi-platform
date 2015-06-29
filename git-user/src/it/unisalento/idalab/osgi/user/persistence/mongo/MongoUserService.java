package it.unisalento.idalab.osgi.user.persistence.mongo;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.persistence.api.UserServicePersistence;
import it.unisalento.idalab.osgi.user.password.Password;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.amdatu.mongo.MongoDBService;
import org.osgi.service.log.LogService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;



public class MongoUserService implements UserServicePersistence {

	private static final String COLLECTION = "users";
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	private volatile LogService logService;
	private volatile Password passwordService;
	
	// Mongo User collection
	private DBCollection userCollection;
	
	public void start() {
		// Initialize user collection
		userCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}
	
	// READ methods
	// ============
	@Override
	public Map<String,Object> getUser(Map<String, Object> user){
		return getUser(user, false);
	}
	
	@Override
	public Map<String,Object> getConstrainedUser(Map<String, Object> user){
		return getUser(user, true);
	}

	private Map<String,Object> getUser(Map<String, Object> user, boolean constrained) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String,Object> response = new HashMap<String,Object>();
		TreeSet<String> ids = new TreeSet<String>();
		TreeMap<String, User> matchs = new TreeMap<String, User>();
		User found_user = null;
		
		if(constrained){
			// TODO get User with constraining conditions
		}
		else {
			if(user.containsKey("userId")) {
				found_user = users.findOne(new BasicDBObject("_id", user.get("userId")));
				if(found_user!=null){
					ids.add(found_user.get_id());
					matchs.put("userId", found_user);
				}
			}
			if(user.containsKey("username")) {
				found_user = users.findOne(new BasicDBObject("username", user.get("username")));
				if(found_user!=null){
					ids.add(found_user.get_id());
					matchs.put("username", found_user);
				}
			}
			if(user.containsKey("email")) {
				found_user = users.findOne(new BasicDBObject("email", user.get("email")));
				if(found_user!=null){
					ids.add(found_user.get_id());
					matchs.put("email", found_user);
				}
			}
			if(user.containsKey("mobile")) {
				found_user = users.findOne(new BasicDBObject("mobile", user.get("mobile")));
				if(found_user!=null){
					ids.add(found_user.get_id());
					matchs.put("mobile", found_user);
				}
			}
			
			// Set response: number of matched users
			response.put("matched", ids.size());
			
			// Set response details
			switch(ids.size()){
			case 0:
				break;
			case 1:
				response.put("user", matchs.get(matchs.firstKey()));
				response.put("keys", matchs.keySet());
				break;
			default:
				response.put("keys", matchs.keySet());
			}
		}
		
		return response;
	}
	
	public Map<String, Object> getUser(User user) {
		Map<String, Object> map = new TreeMap<String, Object>();
		if(user.get_id()!=null && !"".equals(user))
			map.put("userId", user.get_id());
		if(user.get_id()!=null && !"".equals(user))
			map.put("username", user.getUsername());
		if(user.get_id()!=null && !"".equals(user))
			map.put("email", user.getEmail());		
		if(user.get_id()!=null && !"".equals(user))
			map.put("mobile", user.getMobile());		

		return getUser(map);
	}

	@Override
	public List<User> getUsers() {
		JacksonDBCollection<User, Object> users = JacksonDBCollection.wrap(userCollection, User.class);
		DBCursor<User> cursor = users.find();
		
		List<User> list = new ArrayList<>();
		while(cursor.hasNext()) {
			list.add(cursor.next());
		}

		return list;
	}
	
	private User getUserByKey(String key, String value) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(key, value);
		Map<String, Object> response =  getUser(map);
		if(response.containsKey("user"))
			return (User)response.get("user");

		return null;
	}

	@Override
	public User getUserByUsername(String username) {
		return getUserByKey("username", username);
	}

	@Override
	public User getUserByEmail(String email) {
		return getUserByKey("email", email);
	}
	
	@Override
	public User getUserByMobile(String mobile) {
		return getUserByKey("mobile", mobile);
	}
	
	
	// CREATE
	// ======
	@Override
	public Map<String, Object> createUser(User user) {
		Map<String, Object> response = new TreeMap<String, Object>();
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String, Object> result = getUser(user);
		
		if((int)result.get("matched")==0) {
			String password = user.getPassword();
			// Controllo di presenza di una password: in assenza impostazione di una password predefinita
			// N.B. :in realtà il controllo sulla password va fatto a monte dal chiamante del metodo createUser
			if(user.getPassword()==null || "".equals(user.getPassword()))
				password = "0123456789";  
				
			try {
				user.setPassword(passwordService.getSaltedHash(password));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String savedId = users.save(user).getSavedId();
			if(savedId!=null) {
				User created_user = users.findOneById(savedId);
				if(created_user!=null)
					response.put("user", created_user);
			}
		}
		
		// TODO: Gestire bene la composizione della risposta (deve essere più informativa possibile)

		return response;
	}
	
	// UPDATE
	// ======
	@Override
	public Map<String, Object> updateUser(User user) {
		Map<String, Object> response = new TreeMap<String, Object>();
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String,Object> result = getUser(user);
		
		// TODO: il metodo va ampiamente rivisto per evitare incoerenze nella modifica dell'utente (username, email, mobile già esistenti...)
		if(result.containsKey("user")) {
			if(user.getPassword()!=null && !"".equals(user.getPassword()))
				try {
					user.setPassword(passwordService.getSaltedHash(user.getPassword()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			users.updateById(((User)result.get("user")).get_id(), user);
		}
		
		// TODO: Gestire bene la composizione della risposta (deve essere più informativa possibile)
		
		return response;
	}
	
	// DELETE
	// ======
	@Override
	public Map<String, Object> deleteUser(User user) {
		Map<String, Object> response = new TreeMap<String, Object>();
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String,Object> result = getUser(user);
		
		if(result.containsKey("user")) {
			users.removeById(((User)response.get("user")).get_id());
		}
		
		// TODO: Gestire bene la composizione della risposta (deve essere più informativa possibile)
		
		return response;
	}
	
	// LOGIN
	// =====
	@Override
	public Map<String, Object> login(Map<String, Object> user) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> response = new HashMap<String,Object>();
        String username = (String) user.get("username");
        String email = (String) user.get("email");
        String mobile = (String) user.get("mobile");
        String password = (String) user.get("password");
        
		User usr = new User();
		User userFound = new User();
		if(username!=null){
		usr.setUsername(username);
		}
		else
			response.put("code", 101);
		if(email!=null){
		usr.setEmail(email);
		}
		else
			response.put("code", 102);
		if(mobile!=null){
		usr.setMobile(mobile);
		}
		else
			response.put("code", 103);
		map = getUser(usr);
		userFound = (User) map.get("user");
		
		
		
		
		if(userFound!=null){
		try {
			if(passwordService.check(password, userFound.getPassword())){
				System.out.println("LOG IN!!!");
				logService.log(LogService.LOG_INFO, "LOG IN!!!");
				response.put("user", usr);
				response.put("code", 100);
			}else
				response.put("code", 104);	

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else{
			response.put("code", 105);
		}
		System.out.println("Login response CODE:"+ response.get("code"));
		return response;
		
	}

	// VALIDATE methods
	// ================
	@Override
	public Map<String, Object> validateUsername(String userId, String username) {
		// TODO ...
		// il metodo verifica la validità in termini di unicitï¿½ dello username;
		// se userId NON è null lo username da validare ï¿½ accettabile ANCHE se coincide con l'attuale username dell'utente userId.
		// Se invece userId è null allora username ï¿½ accettabile solo se NON giï¿½ associato a un utente.
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String, Object> map = new TreeMap<String, Object>();
		if(userId==null){
			User user= users.findOne(new BasicDBObject("username", username));
			if(user != null) {
				if(user.getUsername()!=null){
				System.out.println("Username exists");
				map.put("errorCode",1);
				map.put("isValid", false);
				}
			}else{
				map.put("username-exist", false);
				map.put("isValid", true);
				map.put("errorCode", 0);
				System.out.println("Username valid");
			}
		}else{
			User userFound= users.findOneById(userId);
			if(userFound.getUsername().matches(username)){
				map.put("isValid", true);
				map.put("errorCode", 0);
				System.out.println("Username match");
			}else{
				map.put("isValid", false);
				map.put("errorCode", 2);
				System.out.println("Username no match");
			}
		}
		//map.put("isValid", true /*false*/);
		//map.put("errorCode", 0); // da settare con valori >0 in presenza di situazioni di errore (problemi di accesso DB,...)
		return map;
	}

	@Override
	public Map<String, Object> validateEMail(String userId, String email) {

		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String, Object> map = new TreeMap<String, Object>();
		if(userId==null){
			User user= users.findOne(new BasicDBObject("email", email));
			if(user != null) {
				if(user.getEmail()!=null){
				System.out.println("email exists");
				map.put("errorCode",1);
				map.put("isValid", false);
				}
			}else{
				map.put("isValid", true);
				map.put("errorCode", 0);
				System.out.println("email valid");
			}
		}else{
			User userFound= users.findOneById(userId);
			if(userFound.getEmail().matches(email)){
				map.put("isValid", true);
				map.put("errorCode", 0);
				System.out.println("email match");
			}else{
				map.put("isValid", false);
				map.put("errorCode", 2);
				System.out.println("email no match");
			}
		}
		
		return map;

	}

	@Override
	public Map<String, Object> validateMobile(String userId, String mobile) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String, Object> map = new TreeMap<String, Object>();
		if(userId==null){
			User user= users.findOne(new BasicDBObject("mobile", mobile));
			if(user != null) {
				if(user.getMobile()!=null){
				System.out.println("mobile exists");
				map.put("errorCode",1);
				map.put("isValid", false);
				}
			}else{
				map.put("isValid", true);
				map.put("errorCode", 0);
				System.out.println("mobile valid");
			}
		}else{
			User userFound= users.findOneById(userId);
			if(userFound.getMobile().matches(mobile)){
				map.put("isValid", true);
				map.put("errorCode", 0);
				System.out.println("mobile match");
			}else{
				map.put("isValid", false);
				map.put("errorCode", 2);
				System.out.println("mobile no match");
			}
		}
		
		return map;
	
	}

}
