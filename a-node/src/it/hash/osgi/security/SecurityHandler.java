package it.hash.osgi.security;

import java.security.Principal;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

import it.hash.osgi.jwt.service.JWTService;

public class SecurityHandler implements AuthenticationHandler, AuthorizationHandler {
	String name = "SecurityHandler";
	String jwt; // Actual TOKEN
	List<String> roles; // Actual roles
	private volatile JWTService _jwtService;

	@Override
	public boolean isUserInRole(Principal user, String role) {
		if(roles==null) return false;
		
		return roles!=null ? roles.contains(role) : true;
	}

	@Override
	public Principal authenticate(ContainerRequestContext requestContext) {
		String user_id = "anonymous";
		
		// GET token: 1) from Authorization header (Bearer), 2) from Query string
		String authCredentials = requestContext.getHeaderString("Authorization");
		if(authCredentials.startsWith("Bearer"))
			jwt = authCredentials.replaceFirst("Bearer" + " ", "");
		else // search token within query parameyters ("access-token")
			jwt = requestContext.getUriInfo().getQueryParameters().getFirst("access-token");
		
		if(jwt!=null) {
			roles = _jwtService.getRoles(jwt);
			user_id = _jwtService.getUID(jwt);
		}

		return new User(user_id);
	}

	@Override
	public String getAuthenticationScheme() {
		return null;
	}

}
