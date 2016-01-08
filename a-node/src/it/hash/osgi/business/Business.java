package it.hash.osgi.business;

import java.util.Map;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

/*
 * Pojo attività commerciale
 */
    
public class Business implements Comparable<Business>{
	@ObjectId @Id
	private String _id;
	private String UUID;
	private String username;
	private String password;
	private String businessname;
	private String password_mdate;
	private String email;
	private String mobile;
	private String published;
	private String last_login_date;
	private String last_login_ip;
	private String trusted_email;
	private String trusted_mobile;
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

	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBusinessname() {
		return businessname;
	}

	public void setBusinessname(String businessname) {
		this.businessname = businessname;
	}

	public String getPassword_mdate() {
		return password_mdate;
	}

	public void setPassword_mdate(String password_mdate) {
		this.password_mdate = password_mdate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	public String getLast_login_date() {
		return last_login_date;
	}

	public void setLast_login_date(String last_login_date) {
		this.last_login_date = last_login_date;
	}

	public String getLast_login_ip() {
		return last_login_ip;
	}

	public void setLast_login_ip(String last_login_ip) {
		this.last_login_ip = last_login_ip;
	}

	public String getTrusted_email() {
		return trusted_email;
	}

	public void setTrusted_email(String trusted_email) {
		this.trusted_email = trusted_email;
	}

	public String getCauthor() {
		return cauthor;
	}

	public void setCauthor(String cauthor) {
		this.cauthor = cauthor;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

	public String getMauthor() {
		return mauthor;
	}

	public void setMauthor(String mauthor) {
		this.mauthor = mauthor;
	}

	public String getMdate() {
		return mdate;
	}

	public void setMdate(String mdate) {
		this.mdate = mdate;
	}

	public String getLauthor() {
		return lauthor;
	}

	public void setLauthor(String lauthor) {
		this.lauthor = lauthor;
	}

	public String getLdate() {
		return ldate;
	}

	public void setLdate(String ldate) {
		this.ldate = ldate;
	}

	public String getBusiness_data() {
		return business_data;
	}

	public void setBusiness_data(String business_data) {
		this.business_data = business_data;
	}

	public Map<String, Object> getOthers() {
		return others;
	}

	public void setOthers(Map<String, Object> others) {
		this.others = others;
	}

	@Override
	public int compareTo(Business obj) {
		   return this._id.compareTo(obj.get_id());
			}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTrusted_mobile() {
		return trusted_mobile;
	}

	public void setTrusted_mobile(String trusted_mobile) {
		this.trusted_mobile = trusted_mobile;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uuid) {
		UUID = uuid;
	}
}
