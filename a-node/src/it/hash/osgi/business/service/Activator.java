package it.hash.osgi.business.service;
 
import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;

import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();

		manager.add(createComponent()
				.setInterface(new String[] { BusinessService.class.getName(), ManagedService.class.getName() },
						properties)
				.setImplementation(BusinessServiceImpl.class)
				.add(createServiceDependency().setService(BusinessServicePersistence.class).setRequired(true))
				.add(createServiceDependency().setService(EventAdmin.class).setRequired(true))
				.add(createServiceDependency().setService(UUIDService.class).setRequired(true))
				);
		System.out.println(" Business service actived.");
	}

	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
