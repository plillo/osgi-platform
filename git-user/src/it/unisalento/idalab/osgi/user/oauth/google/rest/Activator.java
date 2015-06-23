package it.unisalento.idalab.osgi.user.oauth.google.rest;

import it.unisalento.idalab.osgi.user.oauth.google.service.GoogleOAuthMng;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase{

	@Override
	public void destroy(BundleContext arg0, DependencyManager arg1)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(BundleContext arg0, DependencyManager arg1)
			throws Exception {
		// TODO Auto-generated method stub
		arg1.add(createComponent() 
				.setInterface(Object.class.getName(), null) 
				.setImplementation(GoogleOAuthMngRest.class)
				.add(createServiceDependency().setService(GoogleOAuthMng.class).setRequired(true)));
	}

}
