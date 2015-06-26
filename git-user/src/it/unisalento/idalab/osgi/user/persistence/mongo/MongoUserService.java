package it.unisalento.idalab.osgi.user.persistence.mongo;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.persistence.api.UserPersistenceResponse;
import it.unisalento.idalab.osgi.user.persistence.api.UserServicePersistence;
import it.unisalento.idalab.osgi.user.password.Password;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.amdatu.mongo.MongoDBService;
import org.osgi.service.log.LogService;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;



public class MongoUserService implements UserServicePersistence{

	private static final String COLLECTION = "users";
	private volatile MongoDBService m_mongoDBService;
	private volatile LogService logService;
	private volatile Password passwordService;
	private DBCollection userCollection;
	
	public void start() {
		userCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	public Map<String,Object> findUser(User user){
		return findUser(user, false);
	}

	@Override
	public List<User> listUsers() {
		JacksonDBCollection<User, Object> users = JacksonDBCollection.wrap(userCollection, User.class);
		DBCursor<User> cursor = users.find();
		List<User> user = new ArrayList<>();
		while(cursor.hasNext()) {
			System.out.println("username: "+cursor.next().getUsername());
			//result.add(cursor.next());
		}
		return user;
	}

	@Override
	public User getUserByEmail(String email) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		User user = users.findOne(new BasicDBObject("email", email));
		if(user == null) {
			System.out.println("User was not found");
		}
		System.out.println("username: "+user.getUsername());
		return user;
	}

	@Override
	public User getUserByMobile(String mobile) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		User user = users.findOne(new BasicDBObject("mobile", mobile));
		if(user == null) {
			System.out.println("User was not found");
		}
		System.out.println("username: "+user.getUsername());
		return user;
	}

	@Override
	public User getUserByUsername(String username) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		User user = users.findOne(new BasicDBObject("username", username));
		if(user == null) {
			System.out.println("User was not found");
		}
		System.out.println("username: "+user.getUsername());
		return user;
	}

	@Override
	public void updateUser(User user) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String,Object> response = findUser(user);
		User userFound= (User) response.get("user");
		User findOne = users.findOneById(userFound.get_id());
		//User findOne = users.findOne(new BasicDBObject("_id", response.getIduser()));
		findOne.setEmail(user.getEmail());
		findOne.setPassword(user.getPassword());
		findOne.setUsername(user.getUsername());
		findOne.setMobile(user.getMobile());
		users.updateById(findOne.get_id(), findOne);
	}

	@Override
	public void removeUser(User user) {
		JacksonDBCollection<User, String> users = JacksonDBCollection
				.wrap(userCollection, User.class, String.class);
		//User findOne = users.findOne(new BasicDBObject("email", email));
		Map<String,Object> response = findUser(user);
		User userFound= (User) response.get("user");
		User findOne = users.findOneById(userFound.get_id());
		if(findOne == null) {
			System.out.println("User was not found");
		}
		users.removeById(findOne.get_id());
	}
	
	/*//Da rivedere il discorso password
	@Override
	public User login(User user) {
		User findOne = new User();
		JacksonDBCollection<User, String> users = JacksonDBCollection
				.wrap(userCollection, User.class, String.class);
		UserPersistenceResponse response = findUser(user);
		
		if(response.isEmailFound()==true){
			findOne = users.findOne(new BasicDBObject("email", user.getEmail()).append("password", user.getPassword()));
			if(findOne!=null){
			System.out.println("User found by mail");
			}else{
				System.out.println("Invalid password");}
		} 
		if(response.isMobileFound()==true){
			findOne = users.findOne(new BasicDBObject("mobile", user.getMobile()).append("password", user.getPassword()));
			if(findOne!=null){
				System.out.println("User found by mobile");
				}else{
					System.out.println("Invalid password");}
		}
		if(response.isUsernameFound()==true){
			findOne = users.findOne(new BasicDBObject("username", user.getUsername()).append("password", user.getPassword()));
			if(findOne!=null){
				System.out.println("User found by username");
				}else{
					System.out.println("Invalid password");}
		}
		if(response.isCheck()==false){
			System.out.println("User was not found");
		}
		// TODO Auto-generated method stub
		return findOne;
	}*/

	/*@Override
	public User login(Map<String, Object> user) {

        String username = (String) user.get("username");
        String password = (String) user.get("password");
		
		User usr = new User();
		usr.setUsername(username);
		
		JacksonDBCollection<User, String> users = JacksonDBCollection
				.wrap(userCollection, User.class, String.class);
		
		UserPersistenceResponse response = findUser(usr, false);
		usr = users.findOneById(response.getIdUser());
		try {
			if(passwordService.check(password, usr.getPassword())){
				System.out.println("cuc�");
				logService.log(LogService.LOG_INFO, "LOG IN!!!");
				return usr;
			}
				

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}*/
	
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
		map = findUser(usr);
		userFound = (User) map.get("user");
		if(userFound!=null){
		try {
			//controllo con meccanismo password da aggiungere dopo
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

	
	private Map<String,Object> findUser(User user, boolean constrained) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String,Object> response = new HashMap<String,Object>();
		
		if(constrained){
		}
		else {
			if(user.get_id() == null) {
				User usermail= users.findOne(new BasicDBObject("email", user.getEmail()));
				User usermobile= users.findOne(new BasicDBObject("mobile", user.getMobile()));
				User username= users.findOne(new BasicDBObject("username", user.getUsername()));	
				if(usermail != null) {
					if(usermail.getEmail()!=null){
					System.out.println("Mail exists");
					response.put("user", user);
					response.put("info", 101);
					response.put("mail-exist", true);
					response.put("check", true);
					}
				}else
					response.put("mail-exist", false);
				if(usermobile != null){
					if(usermobile.getMobile()!=null){
					System.out.println("Mobile exists");
					response.put("user", user);
					response.put("info", 102);
					response.put("mobile-exist", true);
					response.put("check", true);
					}
				}else
					response.put("mobile-exist", false);
				if(username != null){
					if(username.getUsername()!=null){
					System.out.println("Username exists");
					response.put("user", user);
					response.put("info", 103);
					response.put("username-exist", true);
					response.put("check", true);
					}
				}else
					response.put("username-exist", false);
				if((username == null)&&(usermobile == null)&&(usermail == null)){
					response.put("check", false);
				}
				if((username != null)&&(usermobile != null)&&(usermail != null)){
					response.put("info", 104);
				}else if((username != null)&&(usermobile != null))
					response.put("info", 104);
				else if((username != null)&&(usermail != null))
					response.put("info", 104);
				else if((usermobile != null)&&(usermail != null))
					response.put("info", 104);
			}else{
				response.put("user", user);
				response.put("check", true);
			}
		}
		
		return response;
	}
	
	@Override
	public Map<String, Object> saveUser(User user) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String, Object> response = findUser(user);
		
		if((boolean) response.get("check")){
			logService.log(LogService.LOG_INFO, "User already registred!");
			response.put("code", 500);
		}
		else {
			// codifica della password
			try {
				//user.setPassword(passwordService.getSaltedHash(user.getPassword()));
				String savedId = users.save(user).getSavedId();
				User userfound= users.findOneById(savedId);
				response.put("user", userfound);
				response.put("code", 100);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	System.out.println("USER: "+response.get("user")+" CODE: "+response.get("code") +" CHECK: "+response.get("check")+" INFO: "+response.get("info") 
			+" Mail exist: "+response.get("mail-exist")+" Mobile exist: "+response.get("mobile-exist")+" Username exist: "+response.get("username-exist"));
		return response;
	}

	@Override
	public Map<String, Object> validateUsername(String userId, String username) {
		// TODO ...
		// il metodo verifica la validit� in termini di unicit� dello username;
		// se userId NON � null lo username da validare � accettabile ANCHE se coincide con l'attuale username dell'utente userId.
		// Se invece userId � null allora username � accettabile solo se NON gi� associato a un utente.
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
