package it.unisalento.idalab.osgi.user.oauth2.authenticator;

import java.util.Map;

public interface Authenticator {
	String getName();
	String getToken(String code);
	Map<String, Object> getUserInfo(String token);
}
