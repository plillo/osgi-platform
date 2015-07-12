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
	@SuppressWarnings("unused")
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
			if(user.containsKey("userId") && user.get("userId")!=null) {
				found_user = users.findOne(new BasicDBObject("_id", user.get("userId")));
				if(found_user!=null){
					ids.add(found_user.get_id());
					matchs.put("userId", found_user);
				}
			}
			if(user.containsKey("username") && user.get("username")!=null) {
				found_user = users.findOne(new BasicDBObject("username", user.get("username")));
				if(found_user!=null){
					ids.add(found_user.get_id());
					matchs.put("username", found_user);
				}
			}
			if(user.containsKey("email") && user.get("email")!=null) {
				found_user = users.findOne(new BasicDBObject("email", user.get("email")));
				if(found_user!=null){
					ids.add(found_user.get_id());
					matchs.put("email", found_user);
				}
			}
			if(user.containsKey("mobile") && user.get("mobile")!=null) {
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
	
	@Override
	public Map<String, Object> getUser(User user) {
		Map<String, Object> map = new TreeMap<String, Object>();
		if(user.get_id()!=null && !"".equals(user))
			map.put("userId", user.get_id());
		if(user.getUsername()!=null && !"".equals(user))
			map.put("username", user.getUsername());
		if(user.getEmail()!=null && !"".equals(user))
			map.put("email", user.getEmail());		
		if(user.getMobile()!=null && !"".equals(user))
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
		
		// Match user
		Map<String, Object> result = getUser(user);

		// If new user
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
				if(created_user!=null) {
					response.put("user", created_user);
					response.put("created", true);
				}
			}
		}
		// If existing user
		else if((int)result.get("matched")==1){
			User existing_user = (User) result.get("user");
			if(existing_user!=null) {
				response.put("user", existing_user);
				response.put("created", false);
			}
		}
		// If existing many users
		else{
			//una mappa con più utenti trovati
			System.out.println("Esistono più utenti");
			response.put("created", false);
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
		
		// TODO: il metodo va ampiamente rivisto per evitare incoerenze nella modifica dell'utente (username, email, mobile gi� esistenti...)
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
		
		// TODO: Gestire bene la composizione della risposta (deve essere pi� informativa possibile)
		
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
			users.removeById(((User)result.get("user")).get_id());
			response.put("isRemoved", true);
			//da valutare e concordare
			response.put("errorCode","");
		}else{
			response.put("isRemoved", false);
			//da valutare e concordare
			response.put("errorCode","");
		}
		// TODO: Gestire bene la composizione della risposta (deve essere pi� informativa possibile)
		
		return response;
	}
	
	// LOGIN
	// =====
	@Override
	public Map<String, Object> login(Map<String, Object> user) {
		Map<String, Object> response = new HashMap<String, Object>();
		String password = (String) user.get("password");
		
		// Return ERROR if missing password
		if (password == null || "".equals(password)) {
			response.put("status", 101); // 101: missing password

			return response;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		if(user.containsKey("username"))
			map.put("username", user.get("username"));
		if(user.containsKey("email"))
			map.put("email", user.get("email"));
		if(user.containsKey("mobile"))
			map.put("mobile", user.get("mobile"));

		// Search and get user
		map = getUser(map);
		
		// Get reference to user (if found)
		User userFound = (User) map.get("user");
		if (userFound != null) {
			try {
				if (passwordService.check(password, userFound.getPassword())) {
					response.put("user", userFound);
					response.put("returnCode", 100);
				} else
					response.put("returnCode", 102); // 102: mismatched password

			} catch (Exception e) {
				response.put("returnCode", 103); // 103: exception
			}
		} else {
			response.put("returnCode", 110); // 110: no user matching username/email/mobile
		}
		
		return response;
	}

	// VALIDATE methods
	// ================
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
				map.put("errorCode",0);
				map.put("isValid", false);
				}
			}else{
				map.put("isValid", true);
				map.put("errorCode", 0);
			}
		}else{
			User userFound= users.findOneById(userId);
			if(userFound.getUsername().matches(username)){
				map.put("isValid", true);
				map.put("errorCode", 0);
			}else{
				map.put("isValid", false);
				map.put("errorCode", 0);
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
				map.put("errorCode",0);
				map.put("isValid", false);
				}
			}else{
				map.put("isValid", true);
				map.put("errorCode", 0);
			}
		}else{
			User userFound= users.findOneById(userId);
			if(userFound.getEmail().matches(email)){
				map.put("isValid", true);
				map.put("errorCode", 0);
			}else{
				map.put("isValid", false);
				map.put("errorCode", 0);
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
				map.put("errorCode",0);
				map.put("isValid", false);
				}
			}else{
				map.put("isValid", true);
				map.put("errorCode", 0);
			}
		}else{
			User userFound= users.findOneById(userId);
			if(userFound.getMobile().matches(mobile)){
				map.put("isValid", true);
				map.put("errorCode", 0);
			}else{
				map.put("isValid", false);
				map.put("errorCode", 0);
			}
		}
		
		return map;
	
	}

	@Override
	public Map<String, Object> loginByOAuth2(Map<String, Object> user) {
		// PARTIRE DA QUESTA LOGICA 
		
		
//		Map<String, Object> response = new TreeMap<String, Object>();
//		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
//		Map<String, Object> result = getUser(user);
//
//		if((int)result.get("matched")==0) {
//			String password = user.getPassword();
//			// Controllo di presenza di una password: in assenza impostazione di una password predefinita
//			// N.B. :in realt� il controllo sulla password va fatto a monte dal chiamante del metodo createUser
//			if(user.getPassword()==null || "".equals(user.getPassword()))
//				password = "0123456789";  
//				
//			try {
//				user.setPassword(passwordService.getSaltedHash(password));
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			String savedId = users.save(user).getSavedId();
//			if(savedId!=null) {
//				User created_user = users.findOneById(savedId);
//				if(created_user!=null)
//					response.put("user", created_user);
//			}
//		}else if((int)result.get("matched")==1){
//			//EVENTUALMENTE QUI VA INSERITO L'AGGIORNAMENTO DELL'UTENTE
//			//POTREMMO RICHIAMARE UPDATE USER PASSANDO  existing_user.get_id() E user
//			//ESEMPIO:   users.updateById(existing_user.get_id(), user);
//			System.out.println("Utente esistente");
//			User existing_user = (User) result.get("user");
//			if(existing_user!=null)
//				response.put("user", existing_user);
//		}
//		else{
//			//una mappa con più utenti trovati
//			System.out.println("Esistono più utenti");
//		}
//		
//		// TODO: Gestire bene la composizione della risposta (deve essere pi� informativa possibile)
//
//		return response;
		return null;
	}

}
