package it.hash.osgi.uuid.mock;

import java.util.Dictionary;
import java.util.Map;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.uuid.api.UuidService;

public class UuidServiceImpl implements  UuidService, ManagedService {

	@Override
	public Map<String, Object> createUuid() {
		// TODO Auto-generated method stub
		return null;
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
	public String toString(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

}
