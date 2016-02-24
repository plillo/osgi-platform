package it.hash.osgi.user.attribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBList;

public class utilsAttribute {

	
public static Attribute toMap(Map<String, Object> map){
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
				if(entry.getValue() instanceof BasicDBList){
					 BasicDBList bd= (BasicDBList)entry.getValue();
					 Map mapValues =bd.toMap();
					 Set keyValues= 	mapValues.keySet()	;
					Iterator it= keyValues.iterator();
					while (it.hasNext()){
						String elem=(String) mapValues.get(it.next());
						    attribute.addValues(elem);
					}
					
					 }
				else{
					if (entry.getValue() instanceof List)
				   	attribute.setValues((List<String>) entry.getValue());
					else
				    	if (entry.getValue() instanceof String)
						   attribute.addValues((String)entry.getValue());
				
				}
				break;
			case "mandatory":
				attribute.setMandatory((boolean) entry.getValue());
				break;
			case "context":
				if(entry.getValue() instanceof BasicDBList){
					 BasicDBList bd= (BasicDBList)entry.getValue();
					 Map mapContext =bd.toMap();
					 Set keyContext= 	mapContext.keySet()	;
					Iterator it= keyContext.iterator();
					while (it.hasNext()){
						String elem=(String) mapContext.get(it.next());
						    attribute.addContext(elem);
					}
					
					 }
				else{
					if (entry.getValue() instanceof List)
					   	attribute.setContext((List<String>) entry.getValue());
					else
				    	if (entry.getValue() instanceof String)
						   attribute.addContext((String)entry.getValue());
				
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
