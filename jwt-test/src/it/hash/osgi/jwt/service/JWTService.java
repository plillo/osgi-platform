package it.hash.osgi.jwt.service;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;

public interface JWTService {
	String getToken(String payload);
	JwtClaims getClaims(String jwt);
	String getIssuer(String jwt);
	RsaJsonWebKey getRSA();
}
