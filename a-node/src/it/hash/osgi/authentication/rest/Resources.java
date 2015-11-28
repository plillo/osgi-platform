package it.hash.osgi.authentication.rest;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import io.swagger.annotations.Api;

@Path("auth/1.0")
@Api( value = "/users" )
public class Resources {
	
	@GET
	public String sayHello() {
		return "JAX-RS and OSGi are a lovely couple.";
	}

	@GET
    @Produces("text/plain;charset=UTF-8")
    @Path("/hello")
	@RolesAllowed( "secure" )
    public String sayHello(@Context SecurityContext sc) {
		return "helloooo world!";
		/*
            if (sc.isUserInRole("admin"))  return "Hello World!";
            throw new SecurityException("User is unauthorized.");
        */
    }
}
