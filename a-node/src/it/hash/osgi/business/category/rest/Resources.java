package it.hash.osgi.business.category.rest;

import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.resource.uuid.api.UUIDService;

@Path("businesses/1.0/categories/")
public class Resources {
	
	private volatile CategoryService _categoryService;


	@GET
	@Path("{search}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategory(@PathParam("search") String search) {
		System.out.println(" ");
		Map<String, Object> pars = new TreeMap<String, Object>();
		Map<String, Object> response = new TreeMap<String, Object>();
		Category cat = null;
		cat = _categoryService.getCategoryByKey(search);
		if (cat==null)
      {  return Response.serverError().build();
  	 }
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(cat).build();
	}
  
	
	
	@GET
	@Path("{type}-{search}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response list(@PathParam("type") String type, @PathParam("search") String search) {
   	    return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_categoryService.retrieveCategories(type,search)).build();
	}
}