package it.hash.osgi.user.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBList;

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
		if (this.values==null)
			   this.values = values;
			else {
				for(String elem:values)
					if (!this.values.contains(elem))
						this.values.add(elem);
						
			}
		
	}
	public boolean addContext(String value) {
		if (this.context==null)
			this.context= new ArrayList<String>();
		if (!this.context.contains(value))
			return this.context.add(value);
		return false;
	}
	public boolean removeContext(String value){
		
		return this.context.remove(value);
	}
	public boolean addValues(String value) {
		if (this.values==null)
			this.values= new ArrayList<String>();
		if (!this.values.contains(value))
			return this.values.add(value);
		return false;
	}
		public boolean removeValues(String value){
			
			return this.values.remove(value);
		}
		
	
	
	public List<String> getContext() {
		return context;
	}
	public void setContext(List<String> context) {
		if (this.context==null)
		   this.context = context;
		else {
			for(String elem:context)
				if (!this.context.contains(elem))
					this.context.add(elem);
					
		}
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Attribute other = (Attribute) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	@Override
	public int compareTo(Attribute obj) {
		 return this.uuid.compareTo(obj.getUuid());
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
