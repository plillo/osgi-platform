package it.unisalento.idalab.osgi.user.oauth2.metatypeEx;

import java.util.Hashtable;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.metatype.MetaTypeService;

public class Activator extends DependencyActivatorBase{

	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		// TODO Auto-generated method stub
		Hashtable<String, String> serviceProperties = new Hashtable<String, String>();
		serviceProperties.put(Constants.SERVICE_PID, "it.unisalento.idalab.osgi.user.oauth2.metatypeEx");
		
		manager.add(createComponent() 
				.setInterface(ManagedService.class.getName(), serviceProperties) 
				.setImplementation(ExampleManagedService.class)
				.add(createServiceDependency()
						.setService(MetaTypeService.class)));
	}

}
