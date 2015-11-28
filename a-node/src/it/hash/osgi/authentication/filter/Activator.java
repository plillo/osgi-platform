package it.hash.osgi.authentication.filter;

import org.apache.felix.dm.tracker.ServiceTracker;
import org.apache.felix.http.api.ExtHttpService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {
	private ServiceTracker httpTracker;

	public void start(BundleContext context) throws Exception {
		httpTracker = new ServiceTracker(context, ExtHttpService.class.getName(), null) {
			public void removedService(ServiceReference reference, Object service) {
			}

			public Object addingService(ServiceReference reference) {
				// HTTP service is available, register our resources...
				ExtHttpService httpService = (ExtHttpService) this.context.getService(reference);
				try {
					httpService.registerFilter(new AuthenticationFilter(), "/.*", null, 0, null);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return httpService;
			}
		};
		// start tracking all HTTP services...
		httpTracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		// stop tracking all HTTP services...
		httpTracker.close();
	}
}

