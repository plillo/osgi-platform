package it.hash.osgi.user.attribute.service;

import java.util.List;
import java.util.Map;

import it.hash.osgi.user.attribute.Attribute;

public interface AttributeService {
	List<Attribute> getAttributesByCategories(String[] categories);
	
	Map<String, Object> createAttribute(Attribute attribute);
}
