package it.unisalento.idalab.osgi.user.oauth2.google;

import it.unisalento.idalab.osgi.user.oauth2.authenticator.Authenticator;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase{

	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {
	}

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		manager.add(createComponent() 
				.setInterface(Authenticator.class.getName(), null) 
				.setImplementation(AuthenticatorImpl.class));
	}

}
