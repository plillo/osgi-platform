package it.hash.osgi.user.rest;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.hash.osgi.user.service.UserService;
import static it.hash.osgi.utils.StringUtils.*;

@Path("users/1.0")
public class Resources {
	private volatile UserService _userService;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("root")
	public Response list() {
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_userService.getUsers()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateIdentificator")
	public Response validateIdentificator(@QueryParam("value") String identificator) {
		Map<String, Object> response = _userService.validateIdentificatorAndGetUser(identificator);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("identificator") String identificator, @QueryParam("password") String password) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Check for password: ERROR if missing password
		if(isEON(password)) {
			response.put("isLogged", false);
			response.put("status", it.hash.osgi.user.service.Status.ERROR_MISSING_PASSWORD);
			response.put("message", it.hash.osgi.user.service.Status.ERROR_MISSING_PASSWORD.getMessage());

			return Response.status(Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity(response).status(403).build();
		}
		
		// LOGIN
		Map<String, Object> loginResponse = _userService.validateIdentificatorAndLogin(identificator, password);
		
		if((int)loginResponse.get("status")==it.hash.osgi.user.service.Status.LOGGED.getCode()){
			// LOGGED
			String uuid = (String)loginResponse.get("uuid");
			System.out.println("Logged user: "+uuid);
			return Response.ok().header("Access-Control-Allow-Origin", "*").entity(loginResponse).build();
		}
		else {
			// NOT LOGGED
			String errorMessage = (String)loginResponse.get("message");
			return Response.status(Status.UNAUTHORIZED).entity(errorMessage).header("Access-Control-Allow-Origin", "*").build();
		}
	}
}
