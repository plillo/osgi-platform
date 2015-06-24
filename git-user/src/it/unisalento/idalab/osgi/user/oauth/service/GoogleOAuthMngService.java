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

public class GoogleOAuthMngService implements GoogleOAuthMng{
	
	private volatile UserService userService;

	@Override
	public String getToken(String code, String source) {
		// TODO Auto-generated method stub
		String access_token = null;
		System.out.println("GetToken "+source);
		try
		{			
	        
	        //post parameters
			URL url = null;
			String urlParameters = null;
			
			if (source.equalsIgnoreCase("google"))
			{
				urlParameters = "code="
		                + code
		                + "&client_id=553003668237-olh0snp5lfpl6k6h4rophl6on5u7fodd.apps.googleusercontent.com"
		                + "&client_secret=ta8XLK_XUvipxp_ynyhYkmbv"
		                + "&redirect_uri=http://localhost:8080/Oauth/Oauth2callback"
		                + "&grant_type=authorization_code";
				
				url = new URL("https://accounts.google.com/o/oauth2/token");
			}
			else if (source.equalsIgnoreCase("facebook"))
			{
				urlParameters = "code="
		                + code
		                + "&client_id=492601017561308"
		                + "&client_secret=953b9c7c351c57efd421d2c900802e48"
		                + "&redirect_uri=http://localhost:8080/Oauth/Oauth2callback"
		                + "&grant_type=authorization_code";
				
				url = new URL("https://graph.facebook.com/oauth/access_token");
			}
			
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
	        if (source.equalsIgnoreCase("google"))
	        {
		        ObjectMapper mapper = new ObjectMapper();
		        JsonNode jsn = mapper.readTree(outputString);
		        access_token = jsn.path("access_token").getTextValue();
	        }
	        else if (source.equalsIgnoreCase("facebook"))
	        {
	        	String[] pairs = outputString.split("&");
	        	String[] tmp = pairs[0].split("=");
	        	access_token = tmp[1];
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
	public String getUserInfo(String token, String source) {
		//TODO Auto-generated method stub
		String outputString = null;
		System.out.println("GetUserInfo "+source);
		try
		{
			//get User Info 
			URL url = null;
			if (source.equalsIgnoreCase("google"))
				url = new URL(
	                "https://www.googleapis.com/oauth2/v1/userinfo?access_token="
	                        + token);
			else if (source.equalsIgnoreCase("facebook"))
				url = new URL("https://graph.facebook.com/me?access_token="
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
	        user.setEmail(jsn.path("email").getTextValue());
	        user.setPassword("1234");
	        user.setUsername(jsn.path("email").getTextValue());
	        
	        if (source.equalsIgnoreCase("google"))
	        {
		        user.setFirstName(jsn.path("given_name").getTextValue());
		        user.setLastName(jsn.path("family_name").getTextValue());
	        }
	        else if (source.equalsIgnoreCase("facebook"))
	        {
	        	user.setFirstName(jsn.path("first_name").getTextValue());
		        user.setLastName(jsn.path("last_name").getTextValue());
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
