package it.hash.osgi.user.attribute.rest;

import java.util.ArrayList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;

import it.hash.osgi.business.Business;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;
@Path("attributes/1.0")
public class Resources {
	@SuppressWarnings("unused")
	private volatile AttributeService _attributeService;

	@GET

	@Produces(MediaType.APPLICATION_JSON)
	public Response getAttribute() {
		System.out.println(" ");

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_attributeService.getAttribute())
				.build();
	}

	@PUT
	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML})
//@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response create(Attribute attribute){/* (MultivaluedMap<String, String> form) {*/

	//	Map<String, Object> toForm = extractToForm(form);

//	Business business = (Business) toForm.get("business");

		Map<String, Object> response = _attributeService.createAttribute(attribute);
		
		
		System.out.println("Add " + attribute.get_id() + "returnCode " + response.get("returnCode"));
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

}
