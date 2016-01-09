package it.hash.osgi.resource.uuid.mock;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.resource.uuid.api.UUIDService;

public class Activator extends DependencyActivatorBase {
	@Override
	public  void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties props = new Properties();
		props.put(Constants.SERVICE_RANKING, 200);
		manager.add(createComponent()
		.setInterface(new String[] { UUIDService.class.getName(), ManagedService.class.getName() }, props)
		.setImplementation(UUIDServiceImpl.class)
		);
	}
     
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
