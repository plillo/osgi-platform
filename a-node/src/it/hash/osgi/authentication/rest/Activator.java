package it.hash.osgi.authentication.rest;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class Activator implements BundleActivator {
	private ServiceRegistration registration;

	@Override
	public void start(BundleContext context) throws Exception {
		Resources resources = new Resources();
		registration = context.registerService(Resources.class.getName(), resources, null);
		doLog("Authentication REST registered");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
	}

	private void doLog(String message) {
		System.out.println("## [" + this.getClass() + "] " + message);
	}
}
