package it.unisalento.idalab.osgi.user.oauth2.google;

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
	                + "&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com"
	                + "&client_secret=ta8XLK_XUvipxp_ynyhYkmbv"
	                + "&redirect_uri=http://localhost:8080/OAuth2/callback"
	                + "&grant_type=authorization_code";
			url = new URL("https://accounts.google.com/o/oauth2/token");
			
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
			URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?access_token="
	                + token);
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
	        mapInfo.put("firstName",jsn.path("given_name").getTextValue());
	        mapInfo.put("lastName",jsn.path("family_name").getTextValue());
	        
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
		return "google";
	}
}
