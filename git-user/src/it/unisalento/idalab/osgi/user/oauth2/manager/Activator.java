package it.unisalento.idalab.osgi.user.oauth2.manager;

import it.unisalento.idalab.osgi.user.api.UserService;
import it.unisalento.idalab.osgi.user.oauth2.authenticator.Authenticator;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase{

	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
	}

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {

		manager.add(createComponent() 
				.setInterface(Manager.class.getName(), null) 
				.setImplementation(ManagerImpl.class)
				// Authenticators whiteboard
				.add(createServiceDependency()
						.setService(Authenticator.class)
						.setCallbacks("add", "remove"))
				// UserService injection
				.add(createServiceDependency()
						.setService(UserService.class)
						.setRequired(true)));
	}

}
