package it.hash.osgi.uuid.mock;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.uuid.api.UuidService;

public class UuidServiceImpl implements  UuidService, ManagedService {
   List<Integer> uuids = new ArrayList<Integer>();
   
	@Override
	public Map<String, Object> createUuid() {
	
		Map<String, Object> response = new TreeMap<String, Object>();
     
		boolean esiste=true;
		Random random = new Random();
		while(esiste){
			Integer l=random.nextInt(10);
			if (!uuids.contains(l)){
				uuids.add(l);
				esiste=false;
				response.put("Ok", "200");
				response.put("insertUuid", l.toString());
			}
		}
		return response;
	}

	   
	@Override
	public Map<String, Object> getUuid(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> createFromString(String s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> compareTo(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}


}
