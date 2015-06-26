package it.unisalento.idalab.osgi.user.oauth2.rest;

import java.util.Map;

import it.unisalento.idalab.osgi.user.oauth2.manager.Manager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;

@Path("OAuth2")
@Description("API for OAuth management version 1.0")
public class OAuthResources {
	private volatile Manager _OAuthManager;
	
	@GET
	@Path("callback")
	@Produces(MediaType.APPLICATION_JSON)
	@Description("OAuth callback")
	public Map<String, Object> callback(@QueryParam("code") String code, @QueryParam("state") String state) {

		return _OAuthManager.authenticate(code, state);
	}
}
