package it.unisalento.idalab.osgi.user.oauth.rest;

import it.unisalento.idalab.osgi.user.oauth.service.GoogleOAuthMng;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;

@Path("Oauth")
public class GoogleOAuthMngRest {

private volatile GoogleOAuthMng getUserService;
	
	@GET
	@Path("Oauth2callback")
	@Produces(MediaType.APPLICATION_JSON)
	@Description("oauth callback")
	public String getUser(@QueryParam("code") String code, @QueryParam("state") String source) {
		
		return getUserService.getUserInfo(getUserService.getToken(code, source), source);
		
	}
	
}
