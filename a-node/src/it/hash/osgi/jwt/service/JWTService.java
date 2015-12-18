package it.hash.osgi.jwt.service;

import java.util.List;
import java.util.Map;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;

public interface JWTService {
	String getToken(Map<String, Object> map);
	JwtClaims getClaims(String jwt);
	JwtClaims getClaims();
	String getIssuer(String jwt);
	String getIssuer();
	List<String> getRoles(String jwt);
	List<String> getRoles();
	String getUID(String jwt);
	String getUID();
	RsaJsonWebKey getRSA();
}
