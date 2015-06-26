package it.unisalento.idalab.osgi.user.oauth2.rest;

import it.unisalento.idalab.osgi.user.api.UserService;
import it.unisalento.idalab.osgi.user.oauth2.manager.Manager;

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
				.setInterface(Object.class.getName(), null) 
				.setImplementation(OAuthResources.class)
				.add(createServiceDependency().setService(Manager.class).setRequired(true))
				.add(createServiceDependency().setService(UserService.class).setRequired(true)));
		
		System.out.println("OAuth2 REST resources actived");
	}

}