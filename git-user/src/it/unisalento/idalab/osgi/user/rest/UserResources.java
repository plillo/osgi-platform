package it.unisalento.idalab.osgi.user.rest;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;
import org.amdatu.web.rest.doc.Notes;
import org.amdatu.web.rest.doc.ResponseMessage;
import org.amdatu.web.rest.doc.ResponseMessages;

@Path("users/1.0")
@Description("API for Users management version 1.0")
public class UserResources {
	private volatile UserService _userService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Description("Returns a list of users")
	@Notes("TODO: the list must be filtered by means of parameters")
	public List<User> list() {
		return _userService.listUsers();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Description("Create a user")
	public Map<String, Object> create(User user) {
		return _userService.createUser(user);
	}
	
	
	@GET
	@Path("login")
	@Description("Log-in")
	@Notes("Login resourse API")
	@Produces(MediaType.APPLICATION_JSON)
	@ResponseMessages({ @ResponseMessage(code = 200, message = "In case of success") })
	public Map<String, Object> login(@QueryParam("username") String username, @QueryParam("email") String email, @QueryParam("mobile") String mobile, @QueryParam("password") String password) {
		Map<String, Object> map = new TreeMap<String, Object>();
		
		if (password == null || "".equals(password)) {
			map.put("isLogged", false);
			map.put("message", "Missing password");
			map.put("status", 401);

			return map;
		}
		
		map.put("isLogged", true);
		map.put("message", "");
		map.put("status", 400);

		return map;
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{userId}")
	public String getByUserId(@PathParam("userId") String userId)
			throws Exception {
		return "Load UID: "+userId;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateUsername")
	public Map<String, Object> validateUsername(@QueryParam("username") String username)
			throws Exception {
		
		Map<String, Object> validation = new TreeMap<String, Object>();
		
		validation.put("validatingItem", username);
		validation.put("isValid", true);
		validation.put("message", "\""+username+"\" is a valid username");
		validation.put("status", 400);

		return validation;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateEMail")
	public Map<String, Object> validateEMail(@QueryParam("email") String email)
			throws Exception {
		
		Map<String, Object> validation = new TreeMap<String, Object>();
		
		validation.put("validatingItem", email);
		validation.put("isValid", true);
		validation.put("message", "\""+email+"\" is a valid e-Mail address");
		validation.put("status", 400);

		return validation;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateMobile")
	public Map<String, Object> validateMobile(@QueryParam("mobile") String mobile)
			throws Exception {
		
		Map<String, Object> validation = new TreeMap<String, Object>();
		
		validation.put("validatingItem", mobile);
		validation.put("isValid", true);
		validation.put("message", "\""+mobile+"\" is a valid mobile number");
		validation.put("status", 400);

		return validation;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{userId}/changePassword")
	public String changePassword(@PathParam("userId") String userId, @QueryParam("oldPassword") String oldPassword, @QueryParam("newPassword") String newPassword)
			throws Exception {
		return "Changing password to: "+userId;
	}
}
