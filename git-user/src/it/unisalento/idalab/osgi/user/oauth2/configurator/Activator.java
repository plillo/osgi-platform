package it.unisalento.idalab.osgi.user.oauth2.configurator;

import java.util.Properties;

import it.unisalento.idalab.osgi.user.oauth2.authenticator.Authenticator;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ManagedService;

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
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.unisalento.idalab.osgi.user.oauth2.configurator");
		
		manager.add(createComponent() 
				.setInterface(ManagedService.class.getName(), properties) 
				.setImplementation(OAuth2Configurator.class)
				// Authenticators whiteboard
				.add(createServiceDependency()
						.setService(Authenticator.class)
						.setCallbacks("add", "remove")));
	}

}
