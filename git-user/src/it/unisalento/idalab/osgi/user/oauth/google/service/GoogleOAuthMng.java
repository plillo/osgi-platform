package it.unisalento.idalab.osgi.user.oauth.google.service;

public interface GoogleOAuthMng {
	
	String getToken(String code);
	String getUserInfo(String token);

}
