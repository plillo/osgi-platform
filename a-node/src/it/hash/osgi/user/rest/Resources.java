package it.hash.osgi.user.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

import it.hash.osgi.business.Business;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.service.UserService;
import static it.hash.osgi.utils.StringUtils.*;

@Path("users/1.0")
public class Resources {
	private volatile UserService _userService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// @RolesAllowed("root")
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
		if (isEON(password)) {
			response.put("isLogged", false);
			response.put("status", it.hash.osgi.user.service.Status.ERROR_MISSING_PASSWORD);
			response.put("message", it.hash.osgi.user.service.Status.ERROR_MISSING_PASSWORD.getMessage());

			return Response.status(Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity(response)
					.status(403).build();
		}

		// LOGIN
		Map<String, Object> loginResponse = _userService.validateIdentificatorAndLogin(identificator, password);

		if ((int) loginResponse.get("status") == it.hash.osgi.user.service.Status.LOGGED.getCode()) {
			// LOGGED
			String uuid = (String) loginResponse.get("uuid");
			System.out.println("Logged user: " + uuid);
			return Response.ok().header("Access-Control-Allow-Origin", "*").entity(loginResponse).build();
		} else {
			// NOT LOGGED
			String errorMessage = (String) loginResponse.get("message");
			return Response.status(Status.UNAUTHORIZED).entity(errorMessage).header("Access-Control-Allow-Origin", "*")
					.build();
		}
	}

	private Map<String, Object> extractToForm(MultivaluedMap<String, String> form) {
		Map<String, Object> response = new TreeMap<String, Object>();
		List<Attribute> list = new ArrayList<Attribute>();
		Set<String> entry = form.keySet();
		for (String elem : entry) {
			 if (elem.equals("userUuid")) 
					response.put("userUuid", form.get("userUuid").get(0));
			 if (elem.equals("businessUuid")) {
				response.put("businessUuid", form.get("businessUuid").get(0));

			} else {
				String key = elem.toString();
				if (key.startsWith("att")) {
					String at = form.get(key).get(0);
					Attribute a = new Gson().fromJson(at, Attribute.class);
					list.add(a);
				}
			}

		}

		response.put("attributes", list);

		return response;
	}

	@Path("follow")
	@POST
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)

	public Response follow(MultivaluedMap<String, String> form) {
		/*
		 * nel form ci deve stare nome valore nome: businessUuid valore: stringa
		 * di uuid nome: "attributeI" valore:oggetto attribute con i= 1...N
		 */
		Map<String, Object> response = extractToForm(form);
		// se contiene businessUuid vuol dire che bisogna aggiornare user
		if (response.containsKey("businessUuid")) {
			// inserisco questa informazione per dire al metodo update che deve
			// aggiornare solo gli attributi del user
			// che non si tratta di update generale!!!!
			response.put("update", "attribute");
  
			response = _userService.updateUser(response);
		}
		// altrimenti chiede aggiornare alla lista che già sta
		else
			response = _userService.updateAttributes(response);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

}
