package it.unisalento.idalab.osgi.user.oauth.service;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

public class OAuth2CallbackImpl implements OAuth2Callback{
	
	private volatile UserService userService;

	@Override
	public String getToken(String code, OAuth2Authenticator source) {
		String access_token = null;
		System.out.println("GetToken "+source);
		
		try {			
	        
	        //post parameters
			URL url = null;
			String urlParameters = null;
			
			switch(source){
			case GOOGLE:
				urlParameters = "code="
		                + code
		                + "&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com"
		                + "&client_secret=ta8XLK_XUvipxp_ynyhYkmbv"
		                + "&redirect_uri=http://localhost:8080/Oauth/Oauth2callback"
		                + "&grant_type=authorization_code";
				
				url = new URL("https://accounts.google.com/o/oauth2/token");
				break;
			case FACEBOOK:
				urlParameters = "code="
		                + code
		                + "&client_id=492601017561308"
		                + "&client_secret=953b9c7c351c57efd421d2c900802e48"
		                + "&redirect_uri=http://localhost:8080/Oauth/Oauth2callback"
		                + "&grant_type=authorization_code";
				
				url = new URL("https://graph.facebook.com/oauth/access_token");
				break;
				
			default:break;
			}
			
	        URLConnection urlConn = url.openConnection();
	        urlConn.setDoOutput(true);
	        OutputStreamWriter writer = new OutputStreamWriter(
	                urlConn.getOutputStream());
	        writer.write(urlParameters);
	        writer.flush();
	        
	        // get output in outputString 
	        String line, outputString = "";
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                urlConn.getInputStream()));
	        while ((line = reader.readLine()) != null) {
	            outputString += line;
	        }
	        System.out.println(outputString);
	        
	        // get Access Token 
			switch(source){
			case GOOGLE:
		        ObjectMapper mapper = new ObjectMapper();
		        JsonNode jsn = mapper.readTree(outputString);
		        access_token = jsn.path("access_token").getTextValue();
	            break;
			case FACEBOOK:
	        	String[] pairs = outputString.split("&");
	        	String[] tmp = pairs[0].split("=");
	        	access_token = tmp[1];
	        	break;
	        default:
	        	break;
	        }
	        
	        writer.close();
	        reader.close();
		} catch (MalformedURLException e) {
            System.out.println( e);
        } catch (ProtocolException e) {
            System.out.println( e);
        } catch (IOException e) {
            System.out.println( e);
        }
        
		return access_token;
	}

	@Override
	public String getUserInfo(String token, OAuth2Authenticator source) {
		//TODO Auto-generated method stub
		String outputString = null;
		System.out.println("GetUserInfo "+source);
		try
		{
			//get User Info 
			URL url = null;
			switch(source){
			case GOOGLE:
				url = new URL(
	                "https://www.googleapis.com/oauth2/v1/userinfo?access_token="
	                        + token);
				break;
			case FACEBOOK:
				url = new URL("https://graph.facebook.com/me?access_token="
	                        + token);
	        	break;
	        default:
	        	break;
	        }	
			URLConnection urlConn = url.openConnection();
	        String line; 
	        outputString = "";
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                urlConn.getInputStream()));
	        while ((line = reader.readLine()) != null) {
	            outputString += line;
	        }
	        System.out.println(outputString);
	        reader.close();
	        
	        // Create user
	        User user = new User();
	        
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode jsn = mapper.readTree(outputString);
	        user.setEmail(jsn.path("email").getTextValue());
	        user.setPassword("1234");
	        user.setUsername(jsn.path("email").getTextValue());
	        
			switch(source){
			case GOOGLE:
		        user.setFirstName(jsn.path("given_name").getTextValue());
		        user.setLastName(jsn.path("family_name").getTextValue());
				break;
			case FACEBOOK:
	        	user.setFirstName(jsn.path("first_name").getTextValue());
		        user.setLastName(jsn.path("last_name").getTextValue());
	        	break;
	        default:
	        	break;
	        }	
	        userService.createUser(user);
	        
		} catch (MalformedURLException e) {
	        System.out.println( e);
	    } catch (ProtocolException e) {
	        System.out.println( e);
	    } catch (IOException e) {
	        System.out.println( e);
	    }
        
		return outputString;
	}

}
