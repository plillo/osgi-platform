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
import javax.ws.rs.core.Response;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.business.service.api.BusinessService;
import it.hash.osgi.user.User;
import it.hash.osgi.user.service.UserService;

@Path("businesses/1.0")
public class Resources {

	private volatile BusinessService _businessService;
	private volatile CategoryService _categoryService;
	private volatile UserService _userService;

	/*
	 * @POST
	 * 
	 * @Consumes("application/x-www-form-urlencoded")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public String post(
	 * MultivaluedMap<String, String> form) {
	 * 
	 * Set<String> chiavi= form.keySet(); Iterator<String> it=
	 * chiavi.iterator(); while (it.hasNext()){ List<String> value =
	 * form.get(it.next()); for(int i=0;i<value.size();i++){
	 * System.out.println(value.get(i)); } } return "OK"; }
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response list() {
		System.out.println("All Business ");

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getBusinesses())
				.build();
	}

	// presupposto che le richieste non vengano fatte tramite id
	// i parametri sono username email e mobile
	@GET
	@Path("{UUID}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getBusiness(@PathParam("UUID") String uuid) {
		System.out.println(" ");
		Map<String, Object> pars = new TreeMap<String, Object>();

		pars.put("uuid", uuid);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_businessService.getBusiness(pars))
				.build();
	}

	
	// addBusiness
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({MediaType.APPLICATION_JSON,  MediaType.APPLICATION_XML})

	public Response create(Business business){//MultivaluedMap<String, String> form) {

//		Map<String, Object> toForm = extractToForm(form);

	//	Business business = (Business) toForm.get("business");

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
	@Path("{UUID}")
	public Response update(@PathParam("UUID") String uuid, Business newBusiness) {
		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("uuid", uuid);

		pars.put("business", newBusiness);
		Map<String, Object> response = _businessService.updateBusiness(pars);
		System.out.println("returnCode" + response.get("returnCode"));

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(response).build();
	}





	private Map<String, Object> extractToForm(MultivaluedMap<String, String> form) {
		String attribute;
		Business business = new Business();
		Map<String, Object> response = new TreeMap<String, Object>();

		Map<String, Object> others = new TreeMap<String, Object>();
		for (Entry<String, List<String>> entry : form.entrySet()) {
			attribute = entry.getKey();
			if (attribute.equals("_id")) {
				business.set_id(entry.getValue().toString());
			} else {

				switch (attribute.toLowerCase()) {
				case "uuid":
					business.setUuid((String) entry.getValue().get(0));
					break;
				case "name":
					business.setName((String) entry.getValue().get(0));
					break;
				case "piva":
					business.setPIva((String) entry.getValue().get(0));
					break;
				case "codicefiscale":
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
				case "__description":
					business.set__Description((String) entry.getValue().get(0));
					break;
				case "__longdescription":
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
		return response;
	}

	

}
