package it.unisalento.idalab.osgi.user.oauth.rest;

import it.unisalento.idalab.osgi.user.oauth.service.OAuth2Authenticator;
import it.unisalento.idalab.osgi.user.oauth.service.OAuth2Callback;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;

@Path("Oauth")
public class OAuth2RestResources {

	private volatile OAuth2Callback _callbackOAuth2Service;
	
	@GET
	@Path("Oauth2callback")
	@Produces(MediaType.APPLICATION_JSON)
	@Description("oauth callback")
	public String getUser(@QueryParam("code") String code, @QueryParam("state") String source) {
		String token = _callbackOAuth2Service.getToken(code, OAuth2Authenticator.fromString(source));

		return _callbackOAuth2Service.getUserInfo(token, OAuth2Authenticator.fromString(source));
	}

}
