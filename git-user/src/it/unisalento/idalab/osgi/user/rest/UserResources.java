package it.unisalento.idalab.osgi.user.rest;

import java.util.List;
import java.util.Map;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;
import org.amdatu.web.rest.doc.Notes;

@Path("users")
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
	
}
