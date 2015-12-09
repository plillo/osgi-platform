package it.hash.osgi.jwt.service;

import java.util.Map;

import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwt.JwtClaims;

public interface JWTService {
	String getToken(Map<String, Object> map);
	JwtClaims getClaims(String jwt);
	String getIssuer(String jwt);
	RsaJsonWebKey getRSA();
}
