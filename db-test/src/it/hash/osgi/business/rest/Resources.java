package it.hash.osgi.business.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import it.hash.osgi.business.Business;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.utils.StringUtils;

@Path("businesses/1.0")
public class Resources {

	private volatile BusinessService _businessService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		System.out.println("All Business ");
	
	return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getBusinesses()).build();
	}
//	presupposto che le richieste non vengano fatte tramite id
	// i parametri sono username email e mobile
	@GET
	@Path("{username}{email}{mobile}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBusiness(@PathParam("username") String username,@PathParam("email") String email,
			@PathParam("mobile") String mobile) {
		System.out.println(" ");
	    Map <String,Object> pars = new TreeMap<String, Object>();
   	    pars.put("username", username);
   	    pars.put("email", email);
   	    pars.put("mobile", mobile);

   	    return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getBusiness(pars)).build();
	}
	
	@GET
	@Path("{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBusiness(@PathParam("username") String username) {
		System.out.println(" ");
	    Map <String,Object> pars = new TreeMap<String, Object>();
	  	    pars.put("username", username);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getBusiness(pars)).build();
	}
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML})
	public Response create(Business business) {
	/*	Business bb=new Business();
		bb.set_id("01");
		bb.setBusinessname("Test");
		bb.setUsername("USERTest");
		bb.setPassword("12345");*/
		
	    Map<String,Object> response=  _businessService.create(business);
		System.out.println("Add "+ business.get_id());
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
      	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{identificator}")
	public Response delete(@PathParam("identificator") String id) {
			Map<String,Object> pars = new HashMap<String,Object>();
			pars.put("_id", id);
		    Map<String,Object> response=  _businessService.deleteBusiness(pars);
			System.out.println("Delete  "+ id);
		
			return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	      	}
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{identificator}")
	public Response update(@PathParam("identificator") String id, Business newBusiness ) {
			Map<String,Object> pars = new HashMap<String,Object>();
			pars.put("id", id);
			pars.put("newBusiness", newBusiness);
		    Map<String,Object> response=  _businessService.updateBusiness(pars);
			System.out.println("Update  "+ id);
		
			return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	      	}

}
