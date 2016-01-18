package it.hash.osgi.resource.uuid.mock;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.resource.uuid.api.UUIDService;

public class UUIDServiceImpl implements UUIDService, ManagedService {
   List<String> uuids = new ArrayList<String>();
   
	@Override
	public String createUUID(String type) {
		Random random = new Random();
		while(true){
			String uuid = ""+random.nextInt(10000);
			if (!uuids.contains(uuid)){
				uuids.add(uuid);
				return uuid;
			}
		}
	}

	@Override
	public Map<String, Object> getTypeUUID(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> removeUUID(String uuid) {
		// TODO Auto-generated method stub
		uuids.remove(uuid);
		return null;
	}

	@Override
	public List<String> listUUID(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}
}
