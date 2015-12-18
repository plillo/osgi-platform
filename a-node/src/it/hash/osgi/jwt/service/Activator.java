package it.hash.osgi.jwt.service;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.security.SecurityService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.hash.osgi.jwt.service");
		
		manager.add(createComponent()
			.setInterface(new String[]{JWTService.class.getName(), ManagedService.class.getName()}, properties)
			.setImplementation(JWTServiceImpl.class)
			.add(createServiceDependency().setService(SecurityService.class).setRequired(true))
			);
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}

