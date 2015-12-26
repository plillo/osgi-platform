package it.hash.osgi.broker.rest;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.jwt.service.JWTService;

public class Activator extends DependencyActivatorBase {
	@Override
	public void init(BundleContext context, DependencyManager dm) throws Exception {
		dm.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Resources.class)
				.add(createServiceDependency().setService(JWTService.class).setRequired(true)));

		System.out.println("Broker REST resources actived");
	}

	@Override
	public void destroy(BundleContext context, DependencyManager dm) 
			throws Exception{
	}
}
