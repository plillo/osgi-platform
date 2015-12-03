package it.hash.osgi.user.service;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.jwt.service.JWTService;
import it.hash.osgi.user.password.Password;
import it.hash.osgi.user.persistence.api.UserServicePersistence;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.hash.osgi.user.service");
		
		manager.add(createComponent()
			.setInterface(new String[]{UserService.class.getName(), ManagedService.class.getName()}, properties)
			.setImplementation(UserServiceImpl.class)
			.add(createServiceDependency().setService(UserServicePersistence.class).setRequired(false))
			.add(createServiceDependency().setService(EventAdmin.class).setRequired(true))
			.add(createServiceDependency().setService(JWTService.class).setRequired(true))
		    .add(createServiceDependency().setService(Password.class).setRequired(true)));
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
