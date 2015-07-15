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
		User found_user = null;
		
		Map<User, TreeSet<String>> matchs = new TreeMap<User, TreeSet<String>>();
		
		if(constrained){
			// TODO get User with constraining conditions
		}
		else {
			if(user.containsKey("userId") && user.get("userId")!=null) {
				found_user = users.findOne(new BasicDBObject("_id", user.get("userId")));
				if(found_user!=null){
					TreeSet<String> list = matchs.get(found_user);
					if(list==null)
						list = new TreeSet<String>();
					    
					list.add("userId");
					matchs.put(found_user, list);
				}
			}
			if(user.containsKey("username") && user.get("username")!=null) {
				found_user = users.findOne(new BasicDBObject("username", user.get("username")));
				if(found_user!=null){
					TreeSet<String> list = matchs.get(found_user);
					if(list==null)
						list = new TreeSet<String>();
					    
					list.add("username");
					matchs.put(found_user, list);
				}
			}
			if(user.containsKey("email") && user.get("email")!=null) {
				found_user = users.findOne(new BasicDBObject("email", user.get("email")));
				if(found_user!=null){
					TreeSet<String> list = matchs.get(found_user);
					if(list==null)
						list = new TreeSet<String>();
					    
					list.add("email");
					matchs.put(found_user, list);
				}
			}
			if(user.containsKey("mobile") && user.get("mobile")!=null) {
				found_user = users.findOne(new BasicDBObject("mobile", user.get("mobile")));
				if(found_user!=null){
					TreeSet<String> list = matchs.get(found_user);
					if(list==null)
						list = new TreeSet<String>();
					    
					list.add("mobile");
					matchs.put(found_user, list);
				}
			}
			
			// Set response: number of matched users
			response.put("matched", matchs.size());

			// Set response details
			switch(matchs.size()){
			case 0:
				break;
			case 1:
				response.put("user", found_user);
				response.put("keys", matchs.get(found_user));
				break;
			default:
				response.put("users", matchs);
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
	public Map<String, Object> createUser(Map<String, Object> user) {

		User user_obj = new User();
		user_obj.setUsername((String)user.get("username"));
		user_obj.setEmail((String)user.get("email"));
		user_obj.setMobile((String)user.get("mobile"));
		user_obj.setFirstName((String)user.get("firstName"));
		user_obj.setLastName((String)user.get("lastName"));
		// ...
		
		return createUser(user_obj);
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
			response.put("returnCode", 101); // 101: missing password

			return response;
		}
		
		// Search and get user
		Map<String, Object> map = getUser(user);
		
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
	
	// LOGIN BY OAuth2
		// =====
	@Override
	public Map<String, Object> loginByOAuth2(Map<String, Object> user) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		Map<String, Object> result = getUser(user);
		if((int)result.get("matched")==1){
			
			// TODO: UPDATE PROFILO: inserimento info mancanti
			Map<String, Object> updateResult = updateUserbyOAuth2(user);
			response.put("created", false);
			response.put("user", result.get("user"));
			response.put("returnCode", 100); // 100: existing user
			if(updateResult.containsKey("updatedFields"))
				response.put("updatedFields", updateResult.get("updatedFields") );
			if(updateResult.containsKey("addedFields"))
				response.put("addedFields", updateResult.get("addedFields") );
			
		}
		else if((int)result.get("matched")==0){
			// il metodo createUser produce le chiavi "created" (boolean) e "user" (User)
			response = createUser(user);
			response.put("returnCode", 101); // 101: created user
		}
		else if((int)result.get("matched")>1){
			response.put("created", false);
			response.put("returnCode", 102); // 102: matched more than 1 user
		}

		return response;
	}
	
	
	
	private Map<String, Object> updateUserbyOAuth2(Map<String, Object> user) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String,Object> result = getUser(user);
		Map<String, Object> response = new HashMap<String, Object>();
		List<String> addedFields = new ArrayList<String>(); 
		
		User user_obj = new User();
		if (result.containsKey("username") && result.get("username") == null){
			user_obj.setUsername((String) user.get("username"));
			addedFields.add("username");
		}
		if (result.containsKey("email") && result.get("email") == null){
			user_obj.setEmail((String) user.get("email"));
			addedFields.add("email");
		}
		if (result.containsKey("mobile") && result.get("mobile") == null){
			user_obj.setMobile((String) user.get("mobile"));
			addedFields.add("mobile");
		}
		if (result.containsKey("firstName") && result.get("firstName") == null){
			user_obj.setFirstName((String) user.get("firstName"));
			addedFields.add("firstName");
		}
		if (result.containsKey("lastName") && result.get("lastName") == null){
			user_obj.setLastName((String) user.get("lastName"));
			addedFields.add("lastName");
		}
		
		if(addedFields.size()>0)
			response.put("addedFields", addedFields);

		// ...
		//se decidiamo di aggiornare alcuni campi usermemo la chiave updated
		
		users.updateById(((User)result.get("user")).get_id(), user_obj);
		
		
		return response;
	}


	// VALIDATE methods
	// ================
	@Override
	public Map<String, Object> validateUsername(String userId, String username) {
		// TODO ...
		// il metodo verifica la validità in termini di unicità dello username; 
		// se userId NON è null lo username da validare è accettabile ANCHE se coincide con l'attuale username dell'utente userId.
		// Se invece userId è null allora username è accettabile solo se NON già associato a un utente.
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

	
	


}
