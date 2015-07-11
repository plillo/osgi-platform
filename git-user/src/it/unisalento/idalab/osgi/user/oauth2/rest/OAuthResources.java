package it.unisalento.idalab.osgi.user.oauth2.rest;

import java.io.IOException;
import java.util.Map;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserService;
import it.unisalento.idalab.osgi.user.oauth2.manager.Manager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;

@Path("OAuth2")
@Description("API for OAuth management version 1.0")
public class OAuthResources {
	private volatile Manager _OAuthManager;
	private volatile UserService _userService;
	
	@GET
	@Path("callback")
	@Produces(MediaType.APPLICATION_JSON)
	@Description("OAuth callback")
	public Map<String, Object> callback(@QueryParam("code") String code, @QueryParam("state") String state) {

		try {
			// Get authentication source
			ObjectMapper mapper = new ObjectMapper();
	        JsonNode jsn;	
			jsn = mapper.readTree(state);
	        String authenticator = jsn.path("authenticator").getTextValue();
	        
			Map<String, Object> authenticated_user_map = _OAuthManager.authenticate(code, authenticator);
			
	        // Create user
	        User user = new User();
	        user.setEmail((String)authenticated_user_map.get("email"));
	        user.setFirstName((String)authenticated_user_map.get("firstName"));
	        user.setLastName((String)authenticated_user_map.get("lastName"));
	        
	        return _userService.createUser(user);
	        
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO: chiedere a Paolo verifica "return null"
        return null;
	}
}
