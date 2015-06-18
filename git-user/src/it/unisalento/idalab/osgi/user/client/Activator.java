package it.unisalento.idalab.osgi.user.client;

import it.unisalento.idalab.osgi.user.api.UserService;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		manager.add(createComponent()
			.setInterface(Object.class.getName(), null)
			.setImplementation(Client.class)
			.add(createServiceDependency()
						.setService(UserService.class)
						.setRequired(true)));
	}

	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}