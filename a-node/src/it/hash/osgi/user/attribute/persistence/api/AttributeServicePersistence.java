package it.hash.osgi.user.attribute.persistence.api;

import java.util.List;
import java.util.Map;

import it.hash.osgi.user.attribute.Attribute;

public interface AttributeServicePersistence {
	List<Attribute> getAttributesByCategories(String[] categories);
	
	Map<String, Object> createAttribute(Attribute attribute);
}
