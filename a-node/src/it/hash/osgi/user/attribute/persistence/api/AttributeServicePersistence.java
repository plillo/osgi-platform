package it.hash.osgi.user.attribute.persistence.api;

import java.util.List;
import java.util.Map;

import it.hash.osgi.user.attribute.Attribute;

public interface AttributeServicePersistence {

	
	List<Attribute> getAttributesByCategories(List<String> categories);

	List<Attribute> getAttribute();
   
	Map<String, Object> addAttribute(Attribute attribute);
}
