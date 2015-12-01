package it.hash.osgi.user.persistence.aws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import it.hash.osgi.aws.console.Console;
import it.hash.osgi.user.User;
import it.hash.osgi.user.persistence.api.UserServicePersistence;
import it.hash.osgi.utils.StringUtils;

public class UserServicePersistenceImpl implements UserServicePersistence{
	public volatile Console console;

	@Override
	public Map<String, Object> addUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> addUser(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return null;
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
	public Map<String,Object> getConstrainedUser(User user){
		Map<String, Object> map = new TreeMap<String, Object>();
		if(user.get_id()!=null && !"".equals(user))
			map.put("userId", user.get_id());
		if(user.getUsername()!=null && !"".equals(user))
			map.put("username", user.getUsername());
		if(user.getEmail()!=null && !"".equals(user))
			map.put("email", user.getEmail());		
		if(user.getMobile()!=null && !"".equals(user))
			map.put("mobile", user.getMobile());
		
		return getUser(map, true);
	}
	
	@Override
	public Map<String, Object> getUser(Map<String, Object> user) {
		return getUser(user, false);
	}
	
	@Override
	public Map<String, Object> getConstrainedUser(Map<String, Object> user) {
		return getUser(user, true);
	}

	private Map<String, Object> getUser(Map<String, Object> user, boolean constrained) {
		Map<String,Object> response = new HashMap<String,Object>();
		Map<User, TreeSet<String>> matchs = new TreeMap<User, TreeSet<String>>();
		
		// Setup Amazon DB client
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		
		if(constrained){
			// TODO get User with constraining conditions
		}
		else {
			searchBy(user, matchs, ddbClient, "Id", "userId");
			searchBy(user, matchs, ddbClient, "username", "username");
			searchBy(user, matchs, ddbClient, "email", "email");
			searchBy(user, matchs, ddbClient, "mobile", "mobile");
			
			// Set response: number of matched users
			response.put("matched", matchs.size());

			// Set response details
			switch(matchs.size()){
			case 0:
				response.put("found", false);
				break;
			case 1:
				User key = (User) matchs.keySet().toArray()[0];
				response.put("user", key);
				response.put("keys", matchs.get(key));
				response.put("found", true);
				break;
			default:
				response.put("users", matchs);
				response.put("returnCode", 110);
			}
		}
				
		return response;
	}

	private void searchBy(Map<String, Object> user, Map<User, TreeSet<String>> matchs, AmazonDynamoDBClient ddbClient, String fieldDBName, String fieldName) {
		if(StringUtils.isEmptyOrNull((String)user.get(fieldName)))
			return;
		
		Map<String, AttributeValue> expressionAttributeValues = new HashMap<String, AttributeValue>();
		expressionAttributeValues.put(":val", new AttributeValue().withS((String)user.get(fieldName))); 
		ScanRequest scanRequest = new ScanRequest()
	    	    .withTableName("Users")
	    	    .withLimit(1)
	    	    .withFilterExpression(fieldDBName+" = :val")
	    	    .withExpressionAttributeValues(expressionAttributeValues)
	    	    .withProjectionExpression("Id, lastName, firstName, username, email, mobile");

		ScanResult result = ddbClient.scan(scanRequest);
		for (Map<String, AttributeValue> item : result.getItems()) {
			User user_item = new User();
			if(item.get("Id")!=null)
				user_item.set_id(item.get("Id").getS());
			if(item.get("username")!=null)
				user_item.setUsername(item.get("username").getS());
			if(item.get("email")!=null)
				user_item.setEmail(item.get("email").getS());
			if(item.get("mobile")!=null)
				user_item.setMobile(item.get("mobile").getS());
			if(item.get("firstName")!=null)
				user_item.setFirstName(item.get("firstName").getS());
			if(item.get("lastName")!=null)
				user_item.setLastName(item.get("lastName").getS());
			
			TreeSet<String> list = matchs.get(user_item);
			if(list==null)
				list = new TreeSet<String>();
			    
			list.add("userId");
			matchs.put(user_item, list);
		}
	}

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(console.getCredentials());
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");

        ScanRequest scanRequest = new ScanRequest()
        	    .withTableName("Users")
        	    .withProjectionExpression("Id, lastName, firstName, username, email, mobile");

    	ScanResult result = ddbClient.scan(scanRequest);
    	for (Map<String, AttributeValue> item : result.getItems()) {
    		User user = new User();
    		if(item.get("Id")!=null)
    			user.set_id(item.get("Id").getS());
    		if(item.get("username")!=null)
    			user.setUsername(item.get("username").getS());
    		if(item.get("email")!=null)
    			user.setEmail(item.get("email").getS());
    		if(item.get("mobile")!=null)
    			user.setMobile(item.get("mobile").getS());
    		if(item.get("lastName")!=null)
    			user.setFirstName(item.get("lastName").getS());
    		if(item.get("lastName")!=null)
    			user.setLastName(item.get("lastName").getS());
    		
    		users.add(user);
    	}
    	
    	return users;
	}

	@Override
	public User getUserByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByMobile(String mobile) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUserById(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> getUserDetails(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateUser(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> login(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> loginByOAuth2(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateUsername(String userId, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateEMail(String userId, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> validateMobile(String userId, String mobile) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getImplementation() {
		return "AWS";
	}
}
