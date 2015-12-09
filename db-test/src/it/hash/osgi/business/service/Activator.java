package it.hash.osgi.business.service;

import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
//import it.hash.osgi.jwt.service.JWTService;
//import it.hash.osgi.user.password.Password;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.business.service.BusinessServiceImpl;


import it.hash.osgi.jwt.service.JWTService;
import it.hash.osgi.user.password.Password;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.EventAdmin;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.hash.osgi.business.service");
		
		manager.add(createComponent()
			.setInterface(new String[]{BusinessService.class.getName(), ManagedService.class.getName()}, properties)
			.setImplementation(BusinessServiceImpl.class)
			.add(createServiceDependency().setService(BusinessServicePersistence.class).setRequired(false))
			.add(createServiceDependency().setService(EventAdmin.class).setRequired(true))
			.add(createServiceDependency().setService(JWTService.class).setRequired(true))
		    .add(createServiceDependency().setService(Password.class).setRequired(true)));
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
