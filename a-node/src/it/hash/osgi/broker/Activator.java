package it.hash.osgi.broker;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;

import it.hash.osgi.jwt.service.JWTService;

public class Activator extends DependencyActivatorBase {
	@Override
	public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.hash.osgi.broker.service");
		
		manager.add(
				createComponent()
				.setInterface(Object.class.getName(), null)
				.setImplementation(Resources.class)
				.add(createServiceDependency().setService(JWTService.class).setRequired(true)));
		
		manager.add(
				createComponent()
				.setInterface(new String[] { BrokerService.class.getName(), ManagedService.class.getName() }, properties)
				.setImplementation(BrokerServiceImpl.class));
	}
 
	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}
}
