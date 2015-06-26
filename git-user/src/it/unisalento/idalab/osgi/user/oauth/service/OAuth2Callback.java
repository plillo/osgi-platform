package it.unisalento.idalab.osgi.user.oauth.service;

public interface OAuth2Callback {
	String getToken(String code, OAuth2Authenticator source);
	String getUserInfo(String token, OAuth2Authenticator source);
}
