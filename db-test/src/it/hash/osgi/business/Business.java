package it.hash.osgi.business;

import java.util.Map;





public class Business implements Comparable<Business>{
	private String _id;
	private String username;
	private String password;
	private String companyname;
	private String password_mdate;
	private String email;
	private String published;
	private String last_login_date;
	private String last_login_ip;
	private String trusted_email;
	private String cauthor;
	private String cdate;
	private String mauthor;
	private String mdate;
	private String lauthor;
	private String ldate;
	private String business_data; 
	private Map <String,Object> others;
	
	public String get_id() {
		return _id;
	}

	public void set_id(String id) {
		this._id = id;
	}

	


	
	@Override
	public int compareTo(Business obj) {
		   return this._id.compareTo(obj.get_id());
			}
	

}
