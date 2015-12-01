package it.hash.osgi.user.rest;

import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.hash.osgi.user.service.UserService;

@Path("users/1.0")
public class Resources {
	private volatile UserService _userService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_userService.getUsers()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateIdentificator")
	public Response validateIdentificator(@QueryParam("value") String identificator) {
		Map<String, Object> response = new TreeMap<String, Object>();
		response.put("validatingItem", identificator);
		
		Map<String, Object> map = _userService.validateIdentificator(identificator);
		String identificator_type = (String)map.get("identificatorType");
		response.put("identificatorType", identificator_type);
		response.put("isValid", (Boolean)map.get("isValid"));
		
		if((Boolean)map.get("isValid")) {
			// Get the user (if any) matching the identificator
			map = new TreeMap<String, Object>();

			if("username".equals(identificator_type))
				map.put("username", identificator);
			else if("email".equals(identificator_type))
				map.put("email", identificator);
			else if("mobile".equals(identificator_type))
				map.put("mobile", identificator);
			Map<String, Object> userMap = _userService.getUser(map);
			
			// Build the response
			if((int)userMap.get("matched")>0){
				// MATCHED user
				response.put("matched", true);
				response.put("message", "Matched user with \""+identificator+"\" identificator");
			}
			else {
				response.put("matched", false);
				response.put("message", "The identificator \""+identificator+"\" is available");
			}
		}
		else {
			// Not a valid identificator
			response.put("message", "\""+identificator+"\" is not a valid identificator");
			response.put("status", 210);
		}

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
}
