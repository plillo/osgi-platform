package it.unisalento.idalab.osgi.user.service;

import java.util.Properties;

import it.unisalento.idalab.osgi.user.password.Password;
import it.unisalento.idalab.osgi.user.persistence.api.UserServicePersistence;
import it.unisalento.idalab.osgi.user.api.UserService;

import org.amdatu.mongo.MongoDBService;
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
		properties.put(Constants.SERVICE_PID, "it.unisalento.idalab.osgi.user.service");
		
		manager.add(createComponent()
			.setInterface(new String[]{UserService.class.getName(), ManagedService.class.getName()}, properties)
			.setImplementation(UserServiceImpl.class)
			.add(createServiceDependency().setService(UserServicePersistence.class).setRequired(false))
			.add(createServiceDependency().setService(MongoDBService.class).setRequired(true))
			.add(createServiceDependency().setService(EventAdmin.class).setRequired(true))
		    .add(createServiceDependency().setService(Password.class).setRequired(true)));
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
