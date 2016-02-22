package it.hash.osgi.user.attribute.service;

import java.util.List;
import java.util.Map;

import it.hash.osgi.user.attribute.Attribute;

public interface AttributeService {

	Map<String, Object> createAttribute(Attribute attribute);

	List<Attribute> getAttributesByCategories(List<String> categories);
	   
	List<Attribute> getAttribute();
}
