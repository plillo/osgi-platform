package it.unisalento.idalab.osgi.user.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import it.unisalento.idalab.osgi.user.api.User;
import it.unisalento.idalab.osgi.user.api.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.amdatu.web.rest.doc.Description;
import org.amdatu.web.rest.doc.Notes;
import org.amdatu.web.rest.doc.ResponseMessage;
import org.amdatu.web.rest.doc.ResponseMessages;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@Path("users/1.0")
@Description("API for Users management version 1.0")
public class UserResources {
	private volatile UserService _userService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Description("Returns a list of users")
	@Notes("TODO: the list must be filtered by means of parameters")
	public Response list() {
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_userService.listUsers()).build();
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Description("Create a user")
	public Response create(User user) {
		//return _userService.create(user);
		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(_userService.create(user)).build();
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Description("Create users")
	public Map<String, Object> createx(@Context HttpServletRequest request) {
		boolean simulation = false;
		BufferedReader reader = null;

		Map<String, Object> response = new TreeMap<String, Object>();
		ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
	    try {
	        List<FileItem> parseRequest = uploader.parseRequest(request);
	        for (FileItem fileItem : parseRequest) {
	            if (fileItem.isFormField()) {
	                System.out.println(fileItem.getFieldName() + ": " + fileItem.getString());
	                if("simulation".equals(fileItem.getFieldName()))
	                	simulation = Boolean.parseBoolean(fileItem.getString());
	            }
	            else {
	            	try {
						reader = new BufferedReader(new InputStreamReader(fileItem.getInputStream()));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
           	
	                System.out.println(fileItem.getName());
	            }
	        }
	        
			if(reader!=null)
				try {
					_userService.createUsersByCSV(reader, simulation, false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    } catch (FileUploadException e) {
	        e.printStackTrace();
	    }
	    
	    return response;
	}
	
	@GET
	@Path("login")
	@Description("Log-in")
	@Notes("Login resourse API")
	@Produces(MediaType.APPLICATION_JSON) // va tolto, cambiato???
	@ResponseMessages({ @ResponseMessage(code = 200, message = "In case of success") })
	public Response login(@QueryParam("identificator") String identificator, @QueryParam("password") String password) {
		Map<String, Object> response = new TreeMap<String, Object>();
		
		// Return ERROR if missing password
		if (password == null || "".equals(password)) {
			response.put("isLogged", false);
			response.put("message", "Missing password");
			response.put("status", 401);

			// TODO: gestione dello stato ???
			return Response.status(Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity(response).status(403).build();
			//return Response.status(Status.BAD_REQUEST).entity(response).status(403).build();
		}
		
		// Check and identify the type of identificator (username/email/mobile)
		Map<String, Object> validate = _userService.validateIdentificator(identificator);
		
		if((Boolean)validate.get("isValid")) {
			Map<String, Object> map = new TreeMap<String, Object>();
			map.put("password", password);
			
			String identificator_type = (String)validate.get("identificatorType");
			if("username".equals(identificator_type))
				map.put("username", identificator);
			else if("email".equals(identificator_type))
				map.put("email", identificator);
			else if("mobile".equals(identificator_type))
				map.put("mobile", identificator);
			
			return _userService.login(map);
			//return Response.ok().header("Access-Control-Allow-Origin", "*").entity(map).build();
		}

		return Response.status(Status.BAD_REQUEST).header("Access-Control-Allow-Origin", "*").entity(response).status(403).build();
		//return Response.status(Status.BAD_REQUEST).entity(response).status(403).build();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{userId}")
	public String getByUserId(@PathParam("userId") String userId)
			throws Exception {
		return "Load UID: "+userId;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateIdentificator")
	public Response validateIdentificator(@QueryParam("value") String identificator) {
		Map<String, Object> validation = new TreeMap<String, Object>();
		validation.put("validatingItem", identificator);
		
		Map<String, Object> response = _userService.validateIdentificator(identificator);
		
		boolean isValid = (Boolean)response.get("isValid");
		validation.put("isValid", isValid);
		validation.put("identificatorType", response.get("identificatorType"));
		validation.put("message", "\""+identificator+"\" is "+(isValid?"":"not ")+"a valid identificator");
		validation.put("status", 400);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(validation).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateUsername")
	public Response validateUsername(@QueryParam("value") String username)
			throws Exception {
		
		Map<String, Object> validation = new TreeMap<String, Object>();
		validation.put("validatingItem", username);
		
		Map<String, Object> response = _userService.validateUsername(null, username);
		
		if((Integer)response.get("errorCode")>0){
			validation.put("isValid", false);
			validation.put("status", 200);
			validation.put("message", "Persistence error");
			
			return Response.ok().header("Access-Control-Allow-Origin", "*").status(Status.INTERNAL_SERVER_ERROR).entity(validation).build();
		}
		boolean isValid = (Boolean)response.get("isValid");
		validation.put("isValid", isValid);
		validation.put("message", "\""+username+"\" is "+(isValid?"":"not ")+"a valid username");
		validation.put("status", 400);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(validation).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateEMail")
	public Response validateEMail(@QueryParam("value") String email) {
		Map<String, Object> validation = new TreeMap<String, Object>();
		validation.put("validatingItem", email);
		
		Map<String, Object> response = _userService.validateEMail(null, email);
		
		if((Integer)response.get("errorCode")>0){
			validation.put("isValid", false);
			validation.put("status", 200);
			validation.put("message", "Persistence error");
			
			return Response.ok().header("Access-Control-Allow-Origin", "*").status(Status.INTERNAL_SERVER_ERROR).entity(validation).build();
		}
		boolean isValid = (Boolean)response.get("isValid");
		validation.put("isValid", isValid);
		validation.put("message", "\""+email+"\" is "+(isValid?"":"not ")+"a valid e-mail");
		validation.put("status", 400);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(validation).build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("validateMobile")
	public Response validateMobile(@QueryParam("value") String mobile) {
		
		Map<String, Object> validation = new TreeMap<String, Object>();
		validation.put("validatingItem", mobile);
		
		Map<String, Object> response = _userService.validateMobile(null, mobile);
		
		if((Integer)response.get("errorCode")>0){
			validation.put("isValid", false);
			validation.put("status", 200);
			validation.put("message", "Persistence error");
			
			return Response.ok().header("Access-Control-Allow-Origin", "*").status(Status.INTERNAL_SERVER_ERROR).entity(validation).build();
		}
		boolean isValid = (Boolean)response.get("isValid");
		validation.put("isValid", isValid);
		validation.put("message", "\""+mobile+"\" is "+(isValid?"":"not ")+"a valid mobile number");
		validation.put("status", 400);

		return Response.ok().header("Access-Control-Allow-Origin", "*").entity(validation).build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{userId}/changePassword")
	public String changePassword(@PathParam("userId") String userId, @QueryParam("oldPassword") String oldPassword, @QueryParam("newPassword") String newPassword)
			throws Exception {
		return "Changing password to: "+userId;
	}
}
