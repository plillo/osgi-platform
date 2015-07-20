package it.unisalento.idalab.osgi.captcha.rest;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {
	@Override
	public void init(BundleContext context, DependencyManager dm) 
			throws Exception {
		dm.add(createComponent()
		.setInterface(Object.class.getName(), null)
		.setImplementation(CaptchaResources.class));
		
		System.out.println("Captcha REST resources actived");
	}

	@Override
	public void destroy(BundleContext context, DependencyManager dm) 
			throws Exception{
	}
}
