package it.unisalento.idalab.osgi.user.oauth.service;

public interface GoogleOAuthMng {
	
	String getToken(String code, String source);
	String getUserInfo(String token, String source);

}
