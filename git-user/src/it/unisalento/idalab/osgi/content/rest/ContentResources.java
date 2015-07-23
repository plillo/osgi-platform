package it.unisalento.idalab.osgi.content.rest;

import it.unisalento.idalab.osgi.content.api.Content;
import it.unisalento.idalab.osgi.content.api.ContentService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.amdatu.web.rest.doc.Description;

@Path("contents")
@Description("API for Contents management version 1.0")
public class ContentResources {
	private volatile ContentService _contentService;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{content:.*}")
	public String getContent(@PathParam("content") String content)
			throws Exception {
		Content cnt = (Content)_contentService.getContent(content, "IT", "text/plain").get("content");

		return "Content: "+cnt.getContent();
	}
}