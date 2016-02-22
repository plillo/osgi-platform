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
	
	public static Attribute attributeToMap(Map<String, Object> map){
		Attribute attribute = new Attribute();

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			switch (key.toLowerCase()) {
			case "_id":
				attribute.set_id(entry.getValue().toString());
				break;
			case "uuid":
				attribute.setUuid((String) entry.getValue());
				break;
			case "name":
				attribute.setName((String) entry.getValue());
				break;
			case "label":
				attribute.setLabel((String) entry.getValue());
				break;
			case "values":
				attribute.setValues((List<String>) entry.getValue());
				break;
			case "mandatory":
				attribute.setMandatory((boolean) entry.getValue());
				break;
			case "context":
				if (attribute.getContext() == null)
					attribute.setContext(new ArrayList<String>());
				if(entry.getValue() instanceof BasicDBList){
					 BasicDBList bd= (BasicDBList)entry.getValue();
					 Map mapContext =bd.toMap();
					 Set keyContext= 	mapContext.keySet()	;
					Iterator it= keyContext.iterator();
					while (it.hasNext()){
						String elem=(String) mapContext.get(it.next());
						if (!attribute.getContext().contains(elem))
						    attribute.addContext(elem);
					}
					
					 }
				else{
				if (!attribute.getContext().contains((String) entry.getValue()))
					attribute.addContext((String) entry.getValue());
				}
				break;
			case "cauthor":
				attribute.setCauthor((String) entry.getValue());
				break;
			case "cdate":
				attribute.setCdate((String) entry.getValue());
				break;
			case "mauthor":
				attribute.setMauthor((String) entry.getValue());
				break;
			case "mdate":
				attribute.setMdate((String) entry.getValue());
				break;
			case "lauthor":
				attribute.setLauthor((String) entry.getValue());
				break;
			case "ldate":
				attribute.setLdate((String) entry.getValue());
				break;
			case "others":
				attribute.setOthers((Map<String, Object>) entry.getValue());
				break;
			default:
				if (attribute.getOthers() == null)
					attribute.setOthers(new HashMap<String, Object>());

				attribute.setOthers(key, entry.getValue());
			}
		}

		return attribute;
	}
	
}
