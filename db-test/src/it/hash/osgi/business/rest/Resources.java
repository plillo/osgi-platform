package it.hash.osgi.business.rest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import it.hash.osgi.business.service.BusinessService;
import static it.hash.osgi.utils.StringUtils.*;

@Path("businesss/1.0")
public class Resources {
	private volatile BusinessService _businessService;
	 @GET
	  @Produces(MediaType.APPLICATION_JSON)
	  public List<String> list() {
		return Arrays.asList("A", "B", "C");
	  
	//@GET
	//@Produces(MediaType.APPLICATION_JSON)
//	public Response list() {
		
	//	System.out.println("Prova");
	//	return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getBusinesss()).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateIdentificator")
	public Response validateIdentificator(@QueryParam("value") String identificator) {
		Map<String, Object> response = new TreeMap<String, Object>();
		response.put("validatingItem", identificator);
		
		// Check and identify the type of identificator (businessname/email/mobile)
		Map<String, Object> map = _businessService.validateIdentificator(identificator);
		String identificator_type = (String)map.get("identificatorType");
		response.put("identificatorType", identificator_type);
		response.put("isValid", (Boolean)map.get("isValid"));
		
		if((Boolean)map.get("isValid")) {
			// Get the business (if any) matching the identificator
			// ================================================
			map = new TreeMap<String, Object>();

			if("businessname".equals(identificator_type))
				map.put("businessname", identificator);
			else if("email".equals(identificator_type))
				map.put("email", identificator);
			else if("mobile".equals(identificator_type))
				map.put("mobile", identificator);
			Map<String, Object> businessMap = _businessService.getBusiness(map);
			// ======================================================
			
			// Build the response
			if((int)businessMap.get("matched")>0){
				// MATCHED business
				response.put("matched", true);
				response.put("message", "Matched business with \""+identificator+"\" identificator");
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
	
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@QueryParam("identificator") String identificator, @QueryParam("password") String password) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Check for password: ERROR if missing password
		if(isEON(password)) {
			response.put("isLogged", false);
			response.put("message", "Missing password");
			response.put("status", 401);

			return Response.status(Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity(response).status(403).build();
		}
		
		// Check and identify the type of identificator (businessname/email/mobile)
		Map<String, Object> validate = _businessService.validateIdentificator(identificator);
		
		if((Boolean)validate.get("isValid")) {
			Map<String, Object> map = new TreeMap<String, Object>();
			map.put("password", password);
			
			String identificator_type = (String)validate.get("identificatorType");
			if("businessname".equals(identificator_type))
				map.put("businessname", identificator);
			else if("email".equals(identificator_type))
				map.put("email", identificator);
			else if("mobile".equals(identificator_type))
				map.put("mobile", identificator);
			 
			Map<String, Object> loginResponse = _businessService.login(map);
			
			//String tokenCookieName = (String)loginResponse.get("tokenCookieName");
			//String token = (String)loginResponse.get("token");
			
			if((boolean)loginResponse.get("verified")){
				String id = (String)loginResponse.get("id");
				System.out.println("Logged business: "+id);
				return Response.ok().header("Access-Control-Allow-Origin", "*").entity(loginResponse).build();
			}
			else {
				String errorMessage = (String)loginResponse.get("message");
				return Response.status(Status.UNAUTHORIZED).entity(errorMessage).header("Access-Control-Allow-Origin", "*").build();
			}
		}

		return Response.status(Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity(response).status(401).build();
	}
}
