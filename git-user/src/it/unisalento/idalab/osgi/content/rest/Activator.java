package it.unisalento.idalab.osgi.content.rest;

import it.unisalento.idalab.osgi.content.api.ContentService;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {
	@Override
	public void init(BundleContext context, DependencyManager dm) 
			throws Exception {
		dm.add(createComponent()
		.setInterface(Object.class.getName(), null)
		.setImplementation(ContentResources.class)
		.add(createServiceDependency()
				.setService(ContentService.class)
				.setRequired(true)));
		
		System.out.println("Content REST resources actived");
	}

	@Override
	public void destroy(BundleContext context, DependencyManager dm) 
			throws Exception{
	}
}
