package it.hash.osgi.business.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
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

@Path("businesses/1.0")
public class Resources {
	private volatile BusinessService _businessService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		System.out.println("LISTA ");
	
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getBusinesses()).build();
	}
	
	@POST
	@Path("Create")
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

	@POST
	@Path("Delete/{id}/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML})
	public Response delete(@PathParam("id") String id,@PathParam("username") String username) {
		
			Map<String,Object> pars = new HashMap<String,Object>();
			pars.put("id", id);
			pars.put("username", username);
		    Map<String,Object> response=  _businessService.deleteBusiness(pars);
			System.out.println("Delete  "+ id);
			
			return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	      	}

}
