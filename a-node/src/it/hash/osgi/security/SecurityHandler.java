package it.hash.osgi.security;

import java.security.Principal;
import java.util.Iterator;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler {
	String name = "SecurityHandler";

	@Override
	public boolean isUserInRole(Principal user, String role) {
		return user.getName().equals("test") && role.equals("secure");
	}

	@Override
	public Principal authenticate(ContainerRequestContext requestContext) {
		/*
		 * String user = requestContext.getHeaderString( "user" ); if( user ==
		 * null ) { return null; }
		 */
		
		MultivaluedMap<String,String> map = requestContext.getHeaders();

		logRequest(map);

		return new User("test");
	}

	@Override
	public String getAuthenticationScheme() {
		return null;
	}

	private void logRequest(MultivaluedMap<String, String> map) {
		Iterator<String> itr = map.keySet().iterator();

		for(;itr.hasNext();){
			String key = (String) itr.next();
			doLog("Header: " + key + "=" + map.get(key));
		}
	}

	private void doLog(String message) {
		System.out.println("## [" + this.name + "] " + message);
	}
}
