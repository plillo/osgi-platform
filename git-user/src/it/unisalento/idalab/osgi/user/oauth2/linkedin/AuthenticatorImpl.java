package it.unisalento.idalab.osgi.user.oauth2.linkedin;

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
			
			// TODO: parametrizzare maggiormente questi parametri URL
			urlParameters = "code="
	                + code
	                + (String) parameters.get("fixParameters_linkedin");
			
			url = new URL((String) parameters.get("tokenURL_linkedin"));
			
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
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode jsn = mapper.readTree(outputString);
	        access_token = jsn.path("access_token").getTextValue();
            
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
			// TODO: parametrizzare meglio questo URL in funzione dell'autenticatore
			URL url = new URL((String) parameters.get("userInfoURL_linkedin")+token);
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
	        mapInfo.put("firstName",jsn.path("firstName").getTextValue());
	        mapInfo.put("lastName",jsn.path("lastName").getTextValue());
	        
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
		return "linkedin";
	}

	@Override
	public void setParameters(Map<String, String> parameters) {
		// TODO Auto-generated method stub
		this.parameters = parameters;
	}
}