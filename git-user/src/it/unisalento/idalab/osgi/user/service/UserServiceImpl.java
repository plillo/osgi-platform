package it.unisalento.idalab.osgi.user.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.amdatu.mongo.MongoDBService;
import org.amdatu.security.tokenprovider.TokenProvider;
import org.amdatu.security.tokenprovider.TokenProviderException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import static it.unisalento.idalab.osgi.tools.StringTools.*;
import it.unisalento.idalab.osgi.user.password.Password;
import it.unisalento.idalab.osgi.user.persistence.api.UserServicePersistence;
import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserService;

@SuppressWarnings("rawtypes")
public class UserServiceImpl implements UserService, ManagedService {
	Dictionary properties;
	
	private volatile EventAdmin _eventAdmin;
	private volatile MongoDBService _mongoDBService;
	private volatile TokenProvider _tokenProvider;
	private volatile Password _passwordService;
	private volatile UserServicePersistence _userPersistenceService;
	private Validator validator = new Validator();
	
	void start(){
		System.out.println("started service: "+this.getClass().getName());
	}
 

	@Override
	public Response login(Map<String, Object> pars) {
		@SuppressWarnings("unused")
		Map<String, Object> response = new HashMap<String, Object>();
		
		Map<String, Object> login = _userPersistenceService.login(pars);
		int returnCode = (int)login.get("returnCode");
		
		switch(returnCode){
		case 100:
			User user = (User)login.get("user");
			SortedMap<String, String> userMap = new TreeMap<>();
			userMap.put(TokenProvider.USERNAME, user.get_id());
			
			String token;
			try {
				token = _tokenProvider.generateToken(userMap);
			} catch (TokenProviderException e) {
				return Response.serverError().entity("Error while logging in").header("Access-Control-Allow-Origin", "*").build();
			}
			
			return Response.ok().header("Access-Control-Allow-Origin", "*").entity(user).cookie(new NewCookie(TokenProvider.TOKEN_COOKIE_NAME, token)).build();
			
		case 110:
			return Response.status(403).header("Access-Control-Allow-Origin", "*").build();
		}

		return Response.status(403).header("Access-Control-Allow-Origin", "*").build();
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
	public Map<String, Object> loginByOAuth2(Map<String, Object> user) {
		Map<String, Object> ret = new HashMap<>();
		Map<String, Object> context = new HashMap<>();
		Map<String, Object> timing = new HashMap<>();
		timing.put("start", System.nanoTime());
		context.put("id-code", "AN-031");

		user.put("password", _getRandomPassword(10));

		Map<String,Object> loginResult = _userPersistenceService.loginByOAuth2(user);
		
		ret.put("created", loginResult.get("created"));
		ret.put("return-code", loginResult.get("returnCode"));
		if(loginResult.containsKey("user"))
			ret.put("user", loginResult.get("user"));
		
		if(loginResult.containsKey("updatedFields"))
			ret.put("updated-fields", loginResult.get("updatedFields"));
		
		if(loginResult.containsKey("addedFields"))
			ret.put("added-fields", loginResult.get("addedFields"));

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

	@SuppressWarnings("unused")
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


	@Override
	public Map<String, Object> create(User user) {
		return _userPersistenceService.createUser(user);
	}


	@Override
	public String generatePassword() {
		return _getRandomPassword(10);
	}


}