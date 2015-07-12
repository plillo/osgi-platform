package it.unisalento.idalab.osgi.user.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.amdatu.mongo.MongoDBService;
import org.apache.commons.fileupload.FileItem;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import it.unisalento.idalab.osgi.user.password.Password;
import it.unisalento.idalab.osgi.user.persistence.api.UserServicePersistence;
import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserService;
import static it.unisalento.idalab.osgi.user.service.StringTools.*;

@SuppressWarnings("rawtypes")
public class UserServiceImpl implements UserService, ManagedService {
	Dictionary properties;
	
	private volatile EventAdmin _eventAdmin;
	private volatile MongoDBService _mongoDBService;
	private volatile Password _passwordService;
	private volatile UserServicePersistence _userPersistenceService;
	private Validator validator = new Validator();
	
	void start(){
		System.out.println("started service: "+this.getClass().getName());
	}
	

	@Override
	public Map<String, Object> login(Map<String, Object> pars) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		Map<String, Object> login = _userPersistenceService.login(pars);
		
		// TODO compiling response

		return login;
	}
	
	@Override
	public Map<String, Object> login(String username, String password) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		DBCollection coll = _mongoDBService.getDB().getCollection("user");
		JacksonDBCollection<User, Object> users = JacksonDBCollection.wrap(coll, User.class);
	    BasicDBObject whereQuery = new BasicDBObject();
	    whereQuery.put("username", username);
		DBCursor<User> cursor = users.find(whereQuery);
		boolean matched = false;
	    while(cursor.hasNext()) {
	        User user = cursor.next();
	        try {
				matched = _passwordService.check(password, user.getPassword());
			} catch (Exception e) {
			}
	    }
		
		String token = "123456";
		map.put("token", token);
		map.put("username", username);
		map.put("verified", matched);
		
		// Produce event: 'user/login'
		Map<String,Object> event_props = new HashMap<>();
		event_props.put("token", token);
		event_props.put("username", username);
		event_props.put("verified", matched);
		_eventAdmin.sendEvent(new Event("user/login", event_props));
		
		return map;
	}

	@Override
	public Map<String, Object> testToken(String token) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token-verified", "123456".equals(token));
		
		return map;
	}

	@Override
	public Map<String, Object> createUser(User user) {
		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> context = new HashMap<>();
		Map<String, Object> timing = new HashMap<>();
		timing.put("start", System.nanoTime());
		context.put("id-code", "AN-031");

		@SuppressWarnings("unused")
		Map<String,Object> upr = _userPersistenceService.createUser(user);
		
		timing.put("end", System.nanoTime());
		context.put("error-code", 0);
		//da rivedere
		//context.put("status", upr.isCheck()?200:400);
		context.put("timing", timing);
		ret.put("_context", context);
		ret.put("datetime", new Date().getTime());

		return ret;
	}

	@Override
	public List<User> listUsers() {
		return _userPersistenceService.getUsers();
	}
	
	@Override
	public Map<String, Object> validateUsername(String userId, String username) {

		Map<String, Object> map = _userPersistenceService.validateUsername(userId, username);
		
		return map;
	}
	
	@Override
	public Map<String, Object> validateEMail(String userId, String email) {

		Map<String, Object> map = _userPersistenceService.validateEMail(userId, email);
		
		return map;
	}
	
	@Override
	public Map<String, Object> validateMobile(String userId, String mobile) {

		Map<String, Object> map = _userPersistenceService.validateMobile(userId, mobile);
		
		return map;
	}

	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		this.properties = properties;
		
		validator.setProperties(properties);
	}

	@Override
	public Map<String, Object> validateIdentificator(String identificator) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("identificatorType", "unmatched");
		map.put("isValid", true);
		
		if(validator.isValidEmail(identificator))
			map.put("identificatorType", "email");
		else if(validator.isValidMobile(identificator))
			map.put("identificatorType", "mobile");
		else if(validator.isValidUsername(identificator))
			map.put("identificatorType", "username");
		else
			map.put("isValid", false);

		return map;
	}
	
	private String _getFieldByMap(Hashtable<String, Integer> map, String[] fields, String key, String object) {
		if(map.containsKey(key) && map.get(key)<fields.length)
			return stripSuffix(stripPrefix(fields[map.get(key)],"\""),"\"").trim();

		return object;
	}
	
	public String _getRandomPassword(int length) {
		return String.format("%0"+length+".0f",Math.random()*Math.pow(10, length));
	}

	@Override
	public void createUsersByCSV(BufferedReader reader, boolean simulation, boolean activation) throws IOException {
		StringBuffer log = new StringBuffer();

		try {
			String separator = ",";

			String line = reader.readLine();

			// Fields mapping
			String[] headers = line.split(separator, -1);
			Hashtable<String, Integer> fields_map = new Hashtable<String, Integer>();
			int p = 0;
			for(String f:headers){
				String stripped = stripPrefixAndSuffix(f,"\"");
				if(stripped.equalsIgnoreCase("first_name"))
					fields_map.put("first_name", p);
				else if(stripped.equalsIgnoreCase("last_name"))
					fields_map.put("last_name", p);
				else if(stripped.equalsIgnoreCase("email"))
					fields_map.put("email", p);
				else if(stripped.equalsIgnoreCase("mobile"))
					fields_map.put("mobile", p);
				else if(stripped.equalsIgnoreCase("phone"))
					fields_map.put("phone", p);
				else if(stripped.equalsIgnoreCase("address"))
					fields_map.put("address", p);
				else if(stripped.equalsIgnoreCase("username"))
					fields_map.put("username", p);
				else if(stripped.equalsIgnoreCase("employment"))
					fields_map.put("employment", p);
				else if(stripped.equalsIgnoreCase("province"))
					fields_map.put("province", p);
				else if(stripped.equalsIgnoreCase("city"))
					fields_map.put("city", p);

				p += 1;
			}

			String username = "";
			String firstName = "***";
			String lastName = "***";
			String employment = "";
			String province = "";
			String city = "";
			String address = "";
			String mobile = "";
			String phone = "";
			String email = "";

			// LOG
			log.append("CSV importing process: "+(simulation ? "simulation" : "registration"));
			log.append("\n[-----] username,first_name,last_name,email,mobile,phone,province,city,address");
			log.append("\n"+repeat("=", 100));

			int row = 0;
			int tot_trace = 0;

			while((line = reader.readLine()) != null){
				row++;

				// Pattern per splittare i campi del CSV del tipo: field1,"field2",...
				String regex = ",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))";
				String[] fields = Pattern.compile(regex).split(line,-1);

				username = _getFieldByMap(fields_map, fields, "username", null);
				if(!validator.isValidUsername(username))
					username = null;

				email = _getFieldByMap(fields_map, fields, "email", null);
				if(!validator.isValidEmail(email))
					email = null;

				mobile = _getFieldByMap(fields_map, fields, "mobile", null);
				if(!validator.isValidMobile(mobile))
					mobile = null;

				firstName = capitalizeFirstLetters(_getFieldByMap(fields_map, fields, "first_name",null));
				lastName = capitalizeFirstLetters(_getFieldByMap(fields_map, fields, "last_name",null));
				employment = _getFieldByMap(fields_map, fields, "employment", null);
				address = capitalizeFirstLetters(_getFieldByMap(fields_map, fields, "address",null));
				phone = _getFieldByMap(fields_map, fields, "phone", null);
				province = _getFieldByMap(fields_map, fields, "province",null);
				city = _getFieldByMap(fields_map, fields, "city",null);

				// Instantiation and initialization of a User object
				User user = new User();
				user.setUsername(username);
				user.setEmail(email);
				user.setMobile(mobile);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setPassword(_getRandomPassword(10));
				
				
				if(simulation){
					// Get user
					Map<String,Object> upr = _userPersistenceService.getUser(user);

					// Log
					if(upr.containsKey("user"))
						log.append("\n"+"#"+lastName+"\t"+firstName+"\t["+email+"]");
					else
						log.append("\n"+"+"+lastName+"\t"+firstName+"\t["+email+"]");
				}
				else {
					// Create user
					Map<String,Object> upr = _userPersistenceService.createUser(user);
					
					// Log
					if(upr.containsKey("user"))
						log.append("\n"+((boolean)upr.get("created")?"+":"#")+lastName+"\t"+firstName+"\t["+email+"]");

				}
				
			}
			log.append("\n"+repeat("=", 100));
			log.append("\nErrors: " + tot_trace);
		}
		catch (Exception e) {
			log.append(e.toString()); 
		}
		finally {
			reader.close();
		}

		System.out.println(log.toString());
	}


}