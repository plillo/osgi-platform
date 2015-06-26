package it.unisalento.idalab.osgi.user.rest;

import it.unisalento.idalab.osgi.user.api.UserService;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {
	@Override
	public void init(BundleContext context, DependencyManager dm) 
			throws Exception {
		dm.add(createComponent()
		.setInterface(Object.class.getName(), null)
		.setImplementation(TestRestResources.class));
		
		dm.add(createComponent()
		.setInterface(Object.class.getName(), null)
		.setImplementation(UserResources.class)
		.add(createServiceDependency()
				.setService(UserService.class)
				.setRequired(true)));
		
		System.out.println("User REST resources actived");
	}

	@Override
	public void destroy(BundleContext context, DependencyManager dm) 
			throws Exception{
	}
}