package it.hash.osgi.business.rest;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.business.service.api.BusinessService;
import it.hash.osgi.user.service.UserService;


public class Activator extends DependencyActivatorBase {
	@Override
	public void init(BundleContext context, DependencyManager dm) throws Exception {
		dm.add(createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Resources.class)
				.add(createServiceDependency().setService(BusinessService.class).setRequired(true))
			);
		System.out.println("Business REST resources actived");
	}
       
	@Override
	public void destroy(BundleContext context, DependencyManager dm) 
			throws Exception{
	}
}
