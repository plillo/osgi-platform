package it.hash.osgi.jwt.shell;

import org.jose4j.lang.JoseException;

import it.hash.osgi.jwt.service.JWTService;

public class Commands {
	private volatile JWTService _jwtservice;

	public void token(String payload) throws JoseException{
	    System.out.println(_jwtservice.getToken(payload));
	}
	
	public void claims(String jwt){
	    System.out.println(_jwtservice.getClaims(jwt));
	}
	
	public void issuer(String jwt){
	    System.out.println(_jwtservice.getIssuer(jwt));
	}
}
