package it.unisalento.idalab.osgi.user.oauth.google.service;

import it.unisalento.idalab.osgi.user.api.UserService;

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
		arg1.add(createComponent().setInterface(GoogleOAuthMng.class.getName(), null)
				.setImplementation(GoogleOAuthMngService.class)
				.add(createServiceDependency().setService(UserService.class).setRequired(true)));
	}

}
