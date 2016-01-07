package it.hash.osgi.uuid.mock;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;
//import org.osgi.service.event.EventAdmin;

import it.hash.osgi.uuid.api.UuidService;

public class Activator extends DependencyActivatorBase {
	@Override
	public  void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties props = new Properties();
		props.put(Constants.SERVICE_RANKING, 100);
		manager.add(createComponent()
		.setInterface(new String[] { UuidService.class.getName(), ManagedService.class.getName() }, props)
		.setImplementation(UuidServiceImpl.class)
	//	.add(createServiceDependency().setService(EventAdmin.class).setRequired(true))
		);
	}
     
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
