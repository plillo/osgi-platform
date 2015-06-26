package it.unisalento.idalab.osgi.user.oauth2.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import it.unisalento.idalab.osgi.user.oauth2.authenticator.Authenticator;

public class AuthenticatorImpl implements Authenticator {

	@Override
	public String getToken(String code) {
		String access_token = null;
		
		try {			
			URL url = null;
			String urlParameters = null;
			
			// TODO: usare la configurazione per inizializzare le stringhe
			urlParameters = "code="
	                + code
	                + "&client_id=492601017561308"
	                + "&client_secret=953b9c7c351c57efd421d2c900802e48"
	                + "&redirect_uri=http://localhost:8080/Oauth/Oauth2callback"
	                + "&grant_type=authorization_code";
			
			url = new URL("https://graph.facebook.com/oauth/access_token");
			
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
			URL url = new URL("https://graph.facebook.com/me?access_token=" + token);
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
		return "facebook";
	}

}
