package it.hash.osgi.user.attribute.shell;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

public class Commands {
	private volatile AttributeService _attributeService;
	
	public void getAttributesByCategories(String categories){
		List<Attribute> list = _attributeService.getAttributesByCategories(categories.split(","));
		
		ObjectMapper om = new ObjectMapper();
		String json;
		try {
			json = om.writeValueAsString(list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			json = e.toString();
		} catch (JsonMappingException e) {
			e.printStackTrace();
			json = e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			json = e.toString();
		}
		
		System.out.println(json);
	}
}
