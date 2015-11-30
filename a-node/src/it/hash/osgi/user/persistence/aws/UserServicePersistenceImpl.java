package it.hash.osgi.user.persistence.aws;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import it.hash.osgi.aws.console.Console;
import it.hash.osgi.user.User;
import it.hash.osgi.user.persistence.api.UserServicePersistence;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getUser(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getConstrainedUser(Map<String, Object> user) {
		// TODO Auto-generated method stub
		return null;
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
