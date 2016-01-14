package it.hash.osgi.business.category.service;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.EventAdmin;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();

		manager.add(createComponent()
				.setInterface(new String[] { CategoryService.class.getName(), ManagedService.class.getName() },
						properties)
				.setImplementation(CategoryServiceImpl.class)
				.add(createServiceDependency().setService(EventAdmin.class).setRequired(true)));
	}

	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
