package it.unisalento.idalab.osgi.user.events;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class Activator extends DependencyActivatorBase {
	@Override
	public void init(BundleContext context, DependencyManager dm) 
			throws Exception {
		Properties props = new Properties();
		props.put(EventConstants.EVENT_TOPIC, "user/login");
		
		dm.add(createComponent()
		.setInterface(EventHandler.class.getName(), props)
		.setImplementation(UserEventHandler.class));
	}

	@Override
	public void destroy(BundleContext context, DependencyManager dm) 
			throws Exception{
	}
}
