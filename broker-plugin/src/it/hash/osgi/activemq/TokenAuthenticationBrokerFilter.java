package it.hash.osgi.activemq;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.region.MessageReference;
import org.apache.activemq.broker.region.Subscription;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.ConsumerInfo;
import org.apache.activemq.command.MessageDispatch;
import org.apache.activemq.command.ProducerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TokenAuthenticationBrokerFilter extends BrokerFilter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	Map<String, String> authConfig;

	public TokenAuthenticationBrokerFilter(Broker next, Map<String, String> authConfig) {
		super(next);
		this.authConfig = authConfig;
	}
	
	private String getMonitorUrl() {
    	String host = authConfig.get("host");
		int port = Integer.parseInt(authConfig.get("port"));
		String api_monitor = authConfig.get("api-monitor");
		
		return "http://"+host+":"+port+api_monitor;
	}
	
	private String getConnectionUrl() {
    	String host = authConfig.get("host");
		int port = Integer.parseInt(authConfig.get("port"));
		String api_connection = authConfig.get("api-connection");
		
		return "http://"+host+":"+port+api_connection;
	}

    @SuppressWarnings("unused")
	@Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
    	String host = authConfig.get("host");
		int port = Integer.parseInt(authConfig.get("port"));
		String api_connection = authConfig.get("api-connection");

		String token = context.getUserName();
    	boolean verified = false;
		
    	if("admin".equals(token))
    		verified = true;
    	else
			try {
		        URL url = new URL(getConnectionUrl()+"?token="+token);
		        HttpURLConnection con = (HttpURLConnection) url.openConnection();
		        con.setRequestMethod("GET");
		        int responseCode = con.getResponseCode();
		        System.out.println("GET token validation :: " + responseCode);
		        if (responseCode == HttpURLConnection.HTTP_OK) { // success
		            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		            String inputLine = in.readLine();
		            if(inputLine!=null) {
		            	System.out.println("-->"+inputLine);
		            	ObjectMapper mapper = new ObjectMapper();
		            	JsonNode rootNode = mapper.readTree(inputLine);
		            	JsonNode verifiedNode = rootNode.path("verified");
		            	if(verifiedNode!=null)
		            		verified = verifiedNode.asBoolean(false);
		            }
		            in.close();
		        } else {
		            System.out.println("GET request not worked");
		        }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		if (verified) {
			logger.info(">>Allowing connection with token: [{}]", token);
		} else {
			throw new SecurityException("Token not verified");
		}
		
		super.addConnection(context, info);
    }

	@Override
	public Subscription addConsumer(ConnectionContext context, ConsumerInfo info) throws Exception {
		String pname = info.getDestination().getPhysicalName();
		String qname = info.getDestination().getQualifiedName();
		logger.info(">>Add Consumer: dest(phi,qua): [{},{}]", pname, qname);

		return super.addConsumer(context, info);
	}

	@Override
	public void addProducer(ConnectionContext context, ProducerInfo info) throws Exception {
		String s_info = info.toString();
		logger.info(">>addProducer: {}", s_info);
		
		super.addProducer(context, info);
	}
    
    @Override
    public void messageConsumed(ConnectionContext context,MessageReference messageReference) {
		String message = messageReference.getMessage().toString();
		logger.info(">>messageConsumed: [{}]", message);
  
		super.messageConsumed(context, messageReference);
    }

    @Override
    public void messageDelivered(ConnectionContext context,MessageReference messageReference) {
		String message = messageReference.getMessage().toString();
		logger.info(">>messageDelivered: [{}]", message);
    	
		super.messageDelivered(context, messageReference);
    }
    
    @Override
    public void preProcessDispatch(MessageDispatch messageDispatch) {
		String content = new String(messageDispatch.getMessage().getContent().getData());
		String producerId = messageDispatch.getMessage().getProducerId().toString();
		String destination = messageDispatch.getMessage().getDestination().getQualifiedName();
		logger.info(">>preProcessDispatch:\n>>producerId:{}\n>>destination:{}\n>>content:{}", producerId, destination, content.trim());

		try {
	        URL obj = new URL(getMonitorUrl()+"?content="+content);
	        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	        con.setRequestMethod("GET");
	        int responseCode = con.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_OK) { // success
	            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	            String inputLine;
	            StringBuffer response = new StringBuffer();
	 
	            while ((inputLine = in.readLine()) != null) {
	                response.append(inputLine);
	            }
	            in.close();
	 
	            // print result
	            System.out.println(response.toString());
	        } else {
	            System.out.println("GET request not worked");
	        }	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.preProcessDispatch(messageDispatch);
    }

}
