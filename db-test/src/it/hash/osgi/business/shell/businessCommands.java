package it.hash.osgi.business.shell;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.service.BusinessService;

public class businessCommands {
	private volatile BusinessService _businessService;

	public void addBusiness(String username, String businessname, String password,String email, String mobile) {
		Business business = new Business();
		business.setUsername(username);
		business.setBusinessname(businessname);
		business.setPassword(password);
		business.setEmail(email);
		business.setMobile(mobile);

		Map<String, Object> ret = _businessService.create(business);
		business = null;
		business = (Business) ret.get("business");
		if (business != null) {
			System.out.println("business_Id" + business.get_id());
		}
		for (Map.Entry<String, Object> entry : ret.entrySet())
			System.out.println(entry.getKey() + " - " + entry.getValue().toString());

		System.out.println("called shell command 'createBusiness' - created: " + (Boolean) ret.get("created"));
	}

	public void updateBusiness(String username,String businessname,String email,String mobile,String mauthor) {
		// TODO : ma bisogna prevedere u parametro per ogni attributo????

		Map<String, Object> pars = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		pars.put("username",username);
		pars.put("businessname",businessname);
		pars.put("email",email);
		pars.put("mobile",mobile);
		pars.put("mauthor",mauthor);
        response = _businessService.updateBusiness(pars);
        System.out.println("ReturnCode "+response.get("returncode"));
       
	}

	public void deleteBusiness(String username, String businessname, String password) {

		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("username", username);
		pars.put("businessname", businessname);
		pars.put("password", password);

		Map<String, Object> ret = _businessService.deleteBusiness(pars);
		for (Map.Entry<String, Object> entry : ret.entrySet())
			System.out.println(entry.getKey() + " - " + (String) entry.getValue());
	}

	public void listBusiness() {

		List<Business> businesses = _businessService.getBusinesses();
		if (businesses != null) {
			for (Iterator<Business> it = businesses.iterator(); it.hasNext();) {
				Business business = it.next();
				System.out.print(String.format("%-30s%-20s%-20s", business.get_id(), business.getUsername(),
						business.getBusinessname()));
				System.out.println(String.format("%-20s%-20s", business.getEmail(), business.getMobile()));
			}
		}

	}
}
