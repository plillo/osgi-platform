package it.unisalento.idalab.osgi.user.oauth.rest;

import it.unisalento.idalab.osgi.user.oauth.service.OAuth2Callback;

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
				.setInterface(Object.class.getName(), null) 
				.setImplementation(OAuth2RestResources.class)
				.add(createServiceDependency().setService(OAuth2Callback.class).setRequired(true)));
	}

}
