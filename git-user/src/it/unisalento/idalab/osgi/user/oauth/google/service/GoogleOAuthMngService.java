package it.unisalento.idalab.osgi.user.oauth.google.service;

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

public class GoogleOAuthMngService implements GoogleOAuthMng{
	
	private volatile UserService userService;

	@Override
	public String getToken(String code) {
		// TODO Auto-generated method stub
		String access_token = null;
		
		try
		{
			String urlParameters = "code="
	                + code
	                + "&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com"
	                + "&client_secret=ta8XLK_XUvipxp_ynyhYkmbv"
	                + "&redirect_uri=http://localhost:8080/Google/Oauth2callback"
	                + "&grant_type=authorization_code";
	        
	        //post parameters
	        URL url = new URL("https://accounts.google.com/o/oauth2/token");
	        URLConnection urlConn = url.openConnection();
	        urlConn.setDoOutput(true);
	        OutputStreamWriter writer = new OutputStreamWriter(
	                urlConn.getOutputStream());
	        writer.write(urlParameters);
	        writer.flush();
	        
	        //get output in outputString 
	        String line, outputString = "";
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                urlConn.getInputStream()));
	        while ((line = reader.readLine()) != null) {
	            outputString += line;
	        }
	        System.out.println(outputString);
	        
	        //get Access Token 
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode jsn = mapper.readTree(outputString);
	        access_token = jsn.path("access_token").getTextValue();
	        
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
	public String getUserInfo(String token) {
		//TODO Auto-generated method stub
		String outputString = null;
		
		try
		{
			//get User Info 
			URL url = new URL(
	                "https://www.googleapis.com/oauth2/v1/userinfo?access_token="
	                        + token);
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
	        
	        User user = new User();
	        
	        ObjectMapper mapper = new ObjectMapper();
	        JsonNode jsn = mapper.readTree(outputString);
	        user.setFirstName(jsn.path("given_name").getTextValue());
	        user.setLastName(jsn.path("family_name").getTextValue());
	        user.setEmail(jsn.path("email").getTextValue());
	        user.setUsername(jsn.path("email").getTextValue());
	        user.setPassword("1234");
	        
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
