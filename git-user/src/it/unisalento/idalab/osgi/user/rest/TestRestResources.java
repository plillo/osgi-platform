package it.unisalento.idalab.osgi.user.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;
import org.amdatu.web.rest.doc.Notes;
import org.amdatu.web.rest.doc.ResponseMessage;
import org.amdatu.web.rest.doc.ResponseMessages;

@Path("hellorest")
public class TestRestResources {
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Description("Returns a friendly hellorest message")
	@Notes("This is an example")
	public String SayHello() {
		return "Hello RESTful!";
	}
	
	@GET
	@Path("another")
	@Description("Returns a friendly TEXT_PLAIN message")
	@Notes("This is another example")
	@Produces(MediaType.TEXT_PLAIN)
	@ResponseMessages({ @ResponseMessage(code = 200, message = "In case of success") })
	public String SayAnotherHello() {
		return "Another hello RESTful!";
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{id:\\d+}")
	public String getById(@PathParam("id") long id)
			throws Exception {
		return "You gave: "+id;
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{id:\\d+}/ciao")
	public String getByCiaoId(@PathParam("id") long id)
			throws Exception {
		return "You gave ciao: "+id;
	}
}
