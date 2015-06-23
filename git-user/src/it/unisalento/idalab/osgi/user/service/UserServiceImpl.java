package it.unisalento.idalab.osgi.user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.JacksonDBCollection;

import org.amdatu.mongo.MongoDBService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import it.unisalento.idalab.osgi.user.password.Password;
import it.unisalento.idalab.osgi.user.persistence.api.UserPersistenceResponse;
import it.unisalento.idalab.osgi.user.persistence.api.UserServicePersistence;
import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserService;

public class UserServiceImpl implements UserService{
	private volatile EventAdmin _eventAdmin;
	private volatile MongoDBService _mongoDBService;
	private volatile Password _passwordService;
	private volatile UserServicePersistence _userService;
	
	void start(){
		System.out.println("started service: "+this.getClass().getName());
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

		UserPersistenceResponse upr = _userService.saveUser(user);
		
		/*
		DBCollection coll = _mongoDBService.getDB().getCollection("user");
		JacksonDBCollection<User, Object> users = JacksonDBCollection.wrap(coll, User.class);
		
		String password = user.getPassword();
		try {
			user.setPassword(_passwordService.getSaltedHash(password));
		} catch (Exception e) {
			user.setPassword("#"+password);
		}
		users.save(user);
		*/

		timing.put("end", System.nanoTime());
		context.put("error-code", 0);
		context.put("status", upr.isCheck()?200:400);
		context.put("timing", timing);
		ret.put("_context", context);
		ret.put("datetime", new Date().getTime());

		return ret;
	}

	@Override
	public List<User> listUsers() {
		return _userService.listUsers();
		
		/*
		ArrayList<User> list = new ArrayList<User>();
		DBCollection coll = _mongoDBService.getDB().getCollection("user");
		JacksonDBCollection<User, Object> users = JacksonDBCollection.wrap(coll, User.class);
		
		DBCursor<User> cursor = users.find();
		while(cursor.hasNext()) {
			list.add(cursor.next());
		}
		
		return list;
		*/
	}

}