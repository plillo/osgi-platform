package it.hash.osgi.parser.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.hash.osgi.user.attribute.Attribute;

public class ParserManagerImpl implements ParserManager{
	
	Map<String, ParserService> applications = new HashMap<String, ParserService>();

	

	public void add(ParserService parserService){
		System.out.println("Registering in whiteboard: "+parserService.getAppCode());
		applications.put(parserService.getAppCode(), parserService);
	}
	
	public void remove(ParserService parserService){
		System.out.println("Deleting from whiteboard: "+ parserService.getAppCode());
		applications.remove(parserService);
	}

	@Override
	public void parser(String appCode, String type, String nomefile) {
		ParserService service = applications.get(appCode);
		if(service!=null)
			service.createCollectionBy(type,nomefile);
	}
}
