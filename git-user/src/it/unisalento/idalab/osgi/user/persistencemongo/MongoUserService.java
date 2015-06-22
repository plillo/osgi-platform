package it.unisalento.idalab.osgi.user.persistencemongo;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserPersistenceResponse;
import it.unisalento.idalab.osgi.user.api.UserServicePersistence;
import it.unisalento.idalab.osgi.user.password.Password;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	@Override
	public UserPersistenceResponse saveUser(User user) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		UserPersistenceResponse response = findUser(user);
		
		if(response.isCheck())
			logService.log(LogService.LOG_INFO, "User already registred!");
		else {
			// codifica della password
			try {
				user.setPassword(passwordService.getSaltedHash(user.getPassword()));
				String savedId = users.save(user).getSavedId();
				user.set_id(savedId);
				response.setIdUser(user.get_id());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("ID: "+response.getIdUser()+" Email: "+response.isEmailFound()+" Username: "+response.isUsernameFound()+" Mobile: "+response.isMobileFound());
		return response;
	}

	public UserPersistenceResponse findUser(User user){
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
		UserPersistenceResponse response = findUser(user);
		User findOne = users.findOneById(response.getIdUser());
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
		UserPersistenceResponse response = findUser(user);
		User findOne = users.findOneById(response.getIdUser());
		if(findOne == null) {
			System.out.println("User was not found");
		}
		users.removeById(findOne.get_id());
	}
	
	//Da rivedere il discorso password
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
	}

	@Override
	public User login(HashMap<String, Object> user) {

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
				System.out.println("cucù");
				logService.log(LogService.LOG_INFO, "LOG IN!!!");
				return usr;
			}
				

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	private UserPersistenceResponse findUser(User user, boolean constrained) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		UserPersistenceResponse response = new UserPersistenceResponse();
		
		if(constrained){
		}
		else {
			if(user.get_id() == null) {
				User usermail= users.findOne(new BasicDBObject("email", user.getEmail()));
				User usermobile= users.findOne(new BasicDBObject("mobile", user.getMobile()));
				User username= users.findOne(new BasicDBObject("username", user.getUsername()));	
				if(usermail != null) {
					System.out.println("Mail exists");
					response.setEmailFound(true);
					response.setIdUser(usermail.get_id());
					response.setCheck(true);
				}
				if(usermobile != null){
					System.out.println("Mobile exists");
					response.setMobileFound(true);
					response.setIdUser(usermobile.get_id());
					response.setCheck(true);
				}
				if(username != null){
					System.out.println("Username exists");
					response.setUsernameFound(true);
					response.setIdUser(username.get_id());
					response.setCheck(true);
				}
				if((username == null)&&(usermobile == null)&&(usermail == null)){
					response.setCheck(false);
				}
			}else{
				response.setIdUser(user.get_id());
				response.setCheck(true);
			}
		}
		
		return response;
	}

}
