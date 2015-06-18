package it.unisalento.idalab.osgi.user.help;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {
	@Override
	public void init(BundleContext context, DependencyManager dm) 
			throws Exception {
		dm.add(createComponent()
		.setInterface(Object.class.getName(), null)
		.setImplementation(HelpResourceHandler.class)
		.add(createBundleDependency()
				.setFilter("(X-HelpResource=*)")
				.setCallbacks("bundleAdded","bundleRemoved")
				.setStateMask(Bundle.ACTIVE)));
		
		System.out.println("Help Resource extender actived");
	}

	@Override
	public void destroy(BundleContext context, DependencyManager dm) 
			throws Exception{
	}
}
