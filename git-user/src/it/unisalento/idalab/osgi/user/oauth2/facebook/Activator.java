package it.unisalento.idalab.osgi.user.oauth2.facebook;

import it.unisalento.idalab.osgi.user.oauth2.authenticator.Authenticator;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase{

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1)
			throws Exception {
	}

	@Override
	public void init(BundleContext arg0, DependencyManager arg1)
			throws Exception {
		arg1.add(createComponent() 
				.setInterface(Authenticator.class.getName(), null) 
				.setImplementation(AuthenticatorImpl.class));
	}

}