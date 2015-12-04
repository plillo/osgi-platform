package it.hash.osgi.business.service;

import java.util.Dictionary;

import it.hash.osgi.business.service.BusinessService;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class BusinessServiceImpl implements BusinessService, ManagedService {

	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

}
