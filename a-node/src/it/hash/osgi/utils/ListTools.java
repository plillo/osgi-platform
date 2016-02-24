package it.hash.osgi.utils;

import java.util.List;

import it.hash.osgi.user.attribute.Attribute;

public class ListTools {
	
	
	public static List<Attribute> mergeList(List<Attribute> attributes, List<Attribute> attributesUser) {
		  for (Attribute elem : attributes){
			  if (attributesUser.contains(elem)){
				  attributes.remove(elem);
				  attributes.add(elem);
			  }
			  
		  }
		
		return attributes;
	}

}
