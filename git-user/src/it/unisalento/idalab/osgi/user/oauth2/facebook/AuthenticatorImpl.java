package it.unisalento.idalab.osgi.user.oauth2.facebook;

import it.unisalento.idalab.osgi.user.oauth2.authenticator.Authenticator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class AuthenticatorImpl implements Authenticator {
	
	// configurable variables
	Map<String, String> parameters = new HashMap<String, String>();

	@Override
	public String getToken(String code) {
		String access_token = null;
		
		try {			
			URL url = null;
			String urlParameters = null;
			
			urlParameters = "code="
	                + code
	                + (String) parameters.get("fixParameters_facebook");
			
			url = new URL((String) parameters.get("tokenURL_facebook"));
			
			// write to connection
	        URLConnection urlConn = url.openConnection();
	        urlConn.setDoOutput(true);
	        OutputStreamWriter writer = new OutputStreamWriter(urlConn.getOutputStream());
	        writer.write(urlParameters);
	        writer.flush();
	        
	        // read from connection
	        String line, outputString = "";
	        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
	        while ((line = reader.readLine()) != null) {
	            outputString += line;
	        }
	        
	        // get token from response
        	String[] pairs = outputString.split("&");
        	String[] tmp = pairs[0].split("=");
        	access_token = tmp[1];
            
	        writer.close();
	        reader.close();
		} catch (MalformedURLException e) {
            System.out.println(e);
        } catch (ProtocolException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
        
		return access_token;
	}

	@Override
	public Map<String, Object> getUserInfo(String token) {
		Map<String, Object> mapInfo = new TreeMap<String, Object>();
		String outputString = null;
		try {
			URL url = new URL((String) parameters.get("userInfoURL_facebook")+token);
			URLConnection urlConn = url.openConnection();
	        String line; 
	        outputString = "";
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                urlConn.getInputStream()));
	        while ((line = reader.readLine()) != null) {
	            outputString += line;
	        }
	        reader.close();
	        
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode jsn = mapper.readTree(outputString);
	        
	        mapInfo.put("email", jsn.path("email").getTextValue());
	        mapInfo.put("firstName",jsn.path("first_name").getTextValue());
	        mapInfo.put("lastName",jsn.path("last_name").getTextValue());
	        mapInfo.put("trusted_email",jsn.path("verified").getTextValue());
	        
		} catch (MalformedURLException e) {
	        System.out.println( e);
	    } catch (ProtocolException e) {
	        System.out.println( e);
	    } catch (IOException e) {
	        System.out.println( e);
	    }
        
		return mapInfo;
	}

	@Override
	public String getName() {
		return "facebook";
	}

	@Override
	public void setParameters(Map<String, String> parameters) {
		// TODO Auto-generated method stub
		this.parameters = parameters;
	}
}
