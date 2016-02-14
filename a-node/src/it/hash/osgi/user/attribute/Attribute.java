package it.hash.osgi.user.attribute;

import java.util.List;
import java.util.Map;

import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;

public class Attribute  implements Comparable<Attribute>{
	@ObjectId @Id
	private String _id;
	private String uuid;
	private String name;
	private String label;
	private List<String> values;
	private List<String> context;
	private String validator;
	private boolean mandatory;
	private String cauthor;
	private String cdate;
	private String mauthor;
	private String mdate;
	private String lauthor;
	private String ldate;
	private Map <String,Object> others;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<String> getValues() {
		return values;
	}
	public void setValues(List<String> values) {
		this.values = values;
	}
	public List<String> getContext() {
		return context;
	}
	public void setContext(List<String> context) {
		this.context = context;
	}
	public String getValidator() {
		return validator;
	}
	public void setValidator(String validator) {
		this.validator = validator;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
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
	
	@Override
	public int compareTo(Attribute obj) {
		 return this.uuid.compareTo(obj.getUuid());
	}
	public boolean addContext(String value) {
		return context.add(value);
		
	}
	public boolean removeContext(String value){
		
		return context.remove(value);
	}
	
	
	// EXTRA ATTRIBUTES
	// ================
	public Map<String, Object> getOthers() {
		return others;
	}
	public void setOthers(Map<String, Object> others) {
		this.others = others;
	}
	public Object setOthers(String attribute, Object o){
		return this.others.put(attribute, o);
	}
	public Object getOthers(String attribute){
		return this.others.get(attribute);
	}
	public Object removeOthers(String attribute){
		return this.others.remove(attribute);
	}
	
}
