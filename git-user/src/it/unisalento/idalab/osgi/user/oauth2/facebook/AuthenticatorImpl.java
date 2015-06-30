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
import java.util.Dictionary;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class AuthenticatorImpl implements Authenticator, ManagedService {
	
	// configurable variables
	String fixParameters = null;
	String tokenURL = null;
	String userInfoURL = null;
	String authName = null;

	@Override
	public String getToken(String code) {
		String access_token = null;
		
		try {			
			URL url = null;
			String urlParameters = null;
			
			// TODO: usare la configurazione per inizializzare le stringhe
			urlParameters = "code="
	                + code
	                + fixParameters;
			
			url = new URL(tokenURL);
			
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
			URL url = new URL(userInfoURL+token);
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
		return authName;
	}

	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		// TODO: service che fornisce parametri passando PID e chiave?
		if (fixParameters == null)
			fixParameters = (String) properties.get("fixParameters");
		if (tokenURL == null)
			tokenURL = (String) properties.get("tokenURL");
		if (userInfoURL == null)
			userInfoURL = (String) properties.get("userInfoURL");
		if (authName == null)
			authName = (String) properties.get("authName");
		
	}

}
