package it.hash.osgi.user.attribute.shell;

import java.util.List;

import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

public class Commands {
	private volatile AttributeService _attributeService;
	
	void getAttributesByCategories(String categories){
		List<Attribute> list = _attributeService.getAttributesByCategories(categories.split(","));
		System.out.println(list.toString());
	}
}
