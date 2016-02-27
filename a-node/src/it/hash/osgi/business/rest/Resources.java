package it.hash.osgi.business.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.service.api.BusinessService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;
import it.hash.osgi.user.service.UserService;
import static it.hash.osgi.utils.ListTools.*;

@Path("businesses/1.0")
public class Resources {

	private volatile BusinessService _businessService;
	private volatile AttributeService _attributeService;
	private volatile UserService _userService;

	@GET
	@Path("/business/{search}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBusiness(@PathParam("search") PathSegment s) {

		Map<String, Object> response = new TreeMap<String, Object>();
		List<Business> businesses = new ArrayList<Business>();
		String search = s.getPath();
		String criterion = s.getMatrixParameters().getFirst("criterion");

		businesses = _businessService.retrieveBusinesses(criterion, search);

		if (businesses == null)
			return Response.serverError().build();

		response.put("businesses", businesses);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	@GET 
	@Path("/business/owned")
	@Produces (MediaType.APPLICATION_JSON)
	public Response getOwnerBusinesses() {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// SET Business's owned
		String actual_user_uuid = _userService.getUUID();
		
		// Retrieve
		List<Business> businesses = _businessService.retrieveOwnedBusinesses(actual_user_uuid);

		if (businesses == null)
			return Response.serverError().build();

		response.put("businesses", businesses);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	
	@GET
	@Path("/business/followed")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFollowedBusinesses() {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// SET Business's follower
		String actual_user_uuid = _userService.getUUID();
		
		// Retrieve
		List<Business> businesses = _businessService.retrieveFollowedBusinesses(actual_user_uuid);

		if (businesses == null)
			return Response.serverError().build();

		response.put("businesses", businesses);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@GET
	@Path("/attributeBusiness/{search}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAttribute(@PathParam("search") PathSegment s) {
		System.out.println(" ");
 
		Map<String, Object> response = new TreeMap<String, Object>();
		List<Business> businesses = new ArrayList<Business>();
		List<Attribute> attributes = new ArrayList<Attribute>();
		List<Attribute> attributesUser=new ArrayList<Attribute>();
		String search = s.getPath();
		String criterion = s.getMatrixParameters().getFirst("criterion");

		businesses = _businessService.retrieveBusinesses(criterion, search);
		if (!businesses.isEmpty()) {
			List<String> list = businesses.get(0).getCategories();

			attributes = _attributeService.getAttributesByCategories(list);
            attributesUser=  (List<Attribute>) _userService.getAttributes().get("attributes");
			attributes= mergeList(attributes,attributesUser);
            response.put("businsses", businesses.get(0));
			response.put("Attributes", attributes);
		} else
			response.put("Error", 400);
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();

	}

	

	// addBusiness
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })

	public Response create(Business business) {
		// SET Business's owner
		String actual_user_uuid = _userService.getUUID();
		business.setOwner(actual_user_uuid);
		
		Map<String, Object> response = _businessService.create(business);
		
		System.out.println("Add " + business.get_id() + "returnCode " + response.get("returnCode"));
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{UUID}")
	public Response delete(@PathParam("UUID") String uuid) {
		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("uuid", uuid);
		Map<String, Object> response = _businessService.deleteBusiness(pars);
		System.out.println("Delete  " + response.get("business") + "returnCode " + response.get("returnCode"));

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{search}")
	public Response update(Business newBusiness) {
		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("uuid", newBusiness.getUuid());

		pars.put("business", newBusiness);
		Map<String, Object> response = _businessService.updateBusiness(pars);
		System.out.println("returnCode" + response.get("returnCode"));

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}

	@SuppressWarnings("unused")
	private Map<String, Object> extractToForm(MultivaluedMap<String, String> form) {
		Map<String, Object> response = new TreeMap<String, Object>();

		if (form.containsKey("uuid")) {
			String attribute;
			Business business = new Business();
			Map<String, Object> others = new TreeMap<String, Object>();

			for (Entry<String, List<String>> entry : form.entrySet()) {
				attribute = entry.getKey();
				if (attribute.equals("_id")) {
					business.set_id(entry.getValue().toString());
				} else {

					switch (attribute) {
					case "uuid":
						business.setUuid((String) entry.getValue().get(0));
						break;
					case "name":
						business.setName((String) entry.getValue().get(0));
						break;
					case "pIva":
						business.setPIva((String) entry.getValue().get(0));
						break;
					case "fiscalCode":
						business.setFiscalCode((String) entry.getValue().get(0));
						break;
					case "address":
						business.setAddress((String) entry.getValue().get(0));
						break;
					case "city":
						business.setCity((String) entry.getValue().get(0));
						break;
					case "cap":
						business.setCap((String) entry.getValue().get(0));
						break;
					case "nation":
						business.setNation((String) entry.getValue().get(0));
						break;
					case "_description":
						business.set__Description((String) entry.getValue().get(0));
						break;
					case "__longDescription":
						business.set__longDescription((String) entry.getValue().get(0));
						break;

					case "email":
						business.setEmail((String) entry.getValue().get(0));
						break;

					case "mobile":
						business.setMobile((String) entry.getValue().get(0));
						break;

					case "published":
						business.setPublished((String) entry.getValue().get(0));
						break;

					case "trusted_email":
						business.setTrusted_email((String) entry.getValue().get(0));
						break;

					case "trusted_mobile":
						business.setTrusted_mobile((String) entry.getValue().get(0));
						break;

					case "cauthor":
						business.setCauthor((String) entry.getValue().get(0));
						break;
					case "cdate":
						business.setCdate((String) entry.getValue().get(0));
						break;
					case "mauthor":
						business.setMauthor((String) entry.getValue().get(0));
						break;
					case "mdate":
						business.setMdate((String) entry.getValue().get(0));
						break;
					case "lauthor":
						business.setLauthor((String) entry.getValue().get(0));
						break;
					case "ldate":
						business.setLdate((String) entry.getValue().get(0));
						break;
					case "categories":
						String cat = (String) entry.getValue().get(0);
						String[] categories = cat.split(",");
						for (String element : categories) {
							business.addCategory(element);
						}
						break;
					case "others":
						business.setOthers((Map<String, Object>) entry.getValue());
						break;
					default:
						if (business.getOthers() == null)
							business.setOthers(new HashMap<String, Object>());
						if (!business.getOthers().containsKey(attribute))
							others.put(attribute, entry.getValue());

					}
				}
			}
			response.put("business", business);
		}
		return response;
	}

	// addBusiness
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/business/{uuid}/follow")
	public Response follow(@PathParam("uuid") String businessUuid) {
		// SET Business's owner
		String actual_user_uuid = _userService.getUUID();
		
		Map<String, Object> response = _businessService.followBusiness(businessUuid, actual_user_uuid);
		
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}
	
}
