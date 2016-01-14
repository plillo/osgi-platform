package it.hash.osgi.business.category;

import java.util.Map;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

public class Category {
	@ObjectId @Id
	private String _id;
	private String UUID;
	private String name;
	private String code;
	private String _locDescription;
	private String _locLongDescription;
	private String cauthor;
	private String cdate;
	private String mauthor;
	private String mdate;
	private String lauthor;
	private String ldate;
	private Map <String,Object> extra;
	
	public String getUUID() {
		return UUID;
	}
	
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String get_locDescription() {
		return _locDescription;
	}
	public void set_locDescription(String _locDescription) {
		this._locDescription = _locDescription;
	}
	
	public String get_locLongDescription() {
		return _locLongDescription;
	}
	
	public void set_locLongDescription(String _locLongDescription) {
		this._locLongDescription = _locLongDescription;
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

	public Map <String,Object> getExtra() {
		return extra;
	}

	public void setExtra(Map <String,Object> extra) {
		this.extra = extra;
	}
	
	public void setExtra(String key, Object value){
		getExtra().put(key, value);
	}
	
	public Object getQualifiedExtra(String qualification, String key){
		return getExtra().get(key);
	}
	
	public void setQualifiedExtra(String qualification, String key, Object value) {
		setExtra(qualification + "." + key, value);
	}
}
