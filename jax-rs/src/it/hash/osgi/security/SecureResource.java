package it.hash.osgi.security;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path( "/security" )
public class SecureResource {

  @GET
  @Path( "/unsecure" )
  public String getUnsecure() {
    return "This is the result of a request to the unsecured method";
  }

  @GET
  @Path( "/secure" )
  @RolesAllowed( "secure" )
  public String getSecure() {
    return "This is the result of a request to the secured method";
  }
}
