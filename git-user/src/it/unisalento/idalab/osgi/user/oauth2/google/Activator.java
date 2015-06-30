package it.unisalento.idalab.osgi.user.oauth2.google;

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
	}

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		Properties properties = new Properties();
		properties.put(Constants.SERVICE_PID, "it.unisalento.idalab.osgi.user.oauth2.google");
      
		String[] classes = new String[] { Authenticator.class.getName(), ManagedService.class.getName() };
		manager.add(createComponent() 
				.setInterface(classes, properties) 
				.setImplementation(AuthenticatorImpl.class));
	}

}
