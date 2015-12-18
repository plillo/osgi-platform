package it.hash.osgi.security;

public interface SecurityService {
	String getToken();
	void setToken(String token);
	void unsetToken();
}
