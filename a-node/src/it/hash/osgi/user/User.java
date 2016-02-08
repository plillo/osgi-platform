package it.hash.osgi.user;

import java.util.HashMap;
import java.util.Map;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

public class User implements Comparable<User>{
	@ObjectId @Id
	private String _id;
	private String uuid;
	private String username;
	private String password;
	private String salted_hash_password;
	private String firstName;
	private String lastName;
	private String password_mdate;
	private String email;
	private String mobile;
	private Map<String,Object> extra;
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
	private String user_data;

	public String get_id() {
		return _id;
	}

	public void set_id(String id) {
		this._id = id;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	

	public String getSalted_hash_password() {
		return salted_hash_password;
	}

	public void setSalted_hash_password(String salted_hash_password) {
		this.salted_hash_password = salted_hash_password;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getTrusted_mobile() {
		return trusted_mobile;
	}

	public void setTrusted_mobile(String trusted_mobile) {
		this.trusted_mobile = trusted_mobile;
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

	public String getUser_data() {
		return user_data;
	}

	public void setUser_data(String user_data) {
		this.user_data = user_data;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Map<String,Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}
	
	public Object getExtra(String key){
		return getExtra().get(key);
	}
	
	public void setExtra(String key, Object value){
		if (getExtra()==null){
			Map<String,Object> extra= new HashMap<String,Object>();
			this.setExtra(extra);
		}
		getExtra().put(key, value);
	}
	
	public Object getQualifiedExtra(String qualification, String key){
		return getExtra().get(key);
	}
	
	public void setQualifiedExtra(String qualification, String key, Object value) {
		setExtra(qualification + "." + key, value);
	}

	// implementing Comparable
	@Override
	public int compareTo(User obj) {
       return this._id.compareTo(obj.get_id());
	}

}
