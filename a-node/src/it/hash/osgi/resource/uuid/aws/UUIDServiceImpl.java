package it.hash.osgi.resource.uuid.aws;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;

import it.hash.osgi.aws.console.Console;
import it.hash.osgi.resource.uuid.api.UUIDService;

public class UUIDServiceImpl implements UUIDService, ManagedService {
	private volatile Console _console;
    List<Integer> uuids = new ArrayList<Integer>();
   
	@Override
	public String createUUID(String type) {
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(_console.getCredentials());
		//TODO end-point da mettere in configurazione
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		DynamoDB dynamoDB = new DynamoDB(ddbClient);
        Table table = dynamoDB.getTable("Unids");

        boolean loop = true;
        int counter = 1;
        while(loop) {
        	// RANDOM UUID
            String random_UUID = UUID.randomUUID().toString();
            
            // Set item
    	    Item item = new Item()
    	    		.withPrimaryKey(new PrimaryKey("Id", random_UUID))
    	    		.withLong("cdate", new java.util.Date().getTime());
    	    if(type!=null)
    	    	item.withString("type", type);
    	    
    	    // Set specs
            PutItemSpec putItemSpec = new PutItemSpec()
                    .withItem(item)
                    .withConditionExpression("attribute_not_exists(Id)");
            // Put item
	        try {
	            PutItemOutcome pioc=table.putItem(putItemSpec);
	          Item it=  pioc.getItem();
	            loop = false;
	            ddbClient.shutdown();
	            return random_UUID;
	        } catch (ConditionalCheckFailedException e) {
	        	loop = counter++<=10;
	        }
        }
        
        System.out.println("ERROR generating UUID");
        return null;
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getUUID(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> removeUUID(String uuid) {
		// TODO Auto-generated method stub
		
		AmazonDynamoDBClient ddbClient = new AmazonDynamoDBClient(_console.getCredentials());
		//TODO end-point da mettere in configurazione
		
		Map<String,Object> response= new HashMap<String,Object>();
		ddbClient.setEndpoint("https://dynamodb.eu-central-1.amazonaws.com");
		DynamoDB dynamoDB = new DynamoDB(ddbClient);
        Table table=dynamoDB.getTable("Unids");
        PrimaryKey pk= new PrimaryKey().addComponent("Id",uuid);
        DeleteItemOutcome dIOc = table.deleteItem(pk);
        if (dIOc.getItem()!= null)
              response.put("returnCode", 200);
        
		return null;
	}
}
