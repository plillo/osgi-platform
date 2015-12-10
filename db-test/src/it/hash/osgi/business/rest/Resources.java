package it.hash.osgi.business.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.hash.osgi.business.service.BusinessService;

@Path("businesses/1.0")
public class Resources {
	private volatile BusinessService _businessService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getBusinesses()).build();
	}

}
