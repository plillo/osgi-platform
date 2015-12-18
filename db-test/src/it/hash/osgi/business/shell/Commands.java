package it.hash.osgi.business.shell;

import java.util.Map;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.service.BusinessService;


public class Commands {
	private volatile BusinessService _businessService;
	// da finire :-(
	public void add(String username, String businessname, String password) {
		Business business = new Business();
		business .setUsername(username);
		business .setBusinessname(businessname);
		business .setPassword(password);
		
		Map<String, Object> ret = _businessService.create(business);
		System.out.println("called shell command 'createBusiness' - created: "+(Boolean) ret.get("created"));
	}
}
