package it.unisalento.idalab.osgi.content.service;

import it.unisalento.idalab.osgi.content.api.ContentService;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		
		manager.add(createComponent()
			.setInterface(ContentService.class.getName(), null)
			.setImplementation(ContentServiceImpl.class)
			.add(createServiceDependency().setService(MongoDBService.class).setRequired(true))
			);
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
