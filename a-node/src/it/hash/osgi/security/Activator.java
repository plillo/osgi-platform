package it.hash.osgi.security;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;

import it.hash.osgi.jwt.service.JWTService;

/*
public class Activator implements BundleActivator {

  private ServiceRegistration resourceRegistration;
  private ServiceRegistration authenticationRegistration;
  private ServiceRegistration authorizationRegistration;

  @Override
  public void start( BundleContext bundleContext ) throws Exception {
    resourceRegistration = bundleContext.registerService( SecureResource.class.getName(), new SecureResource(), null );
    SecurityHandler securityService = new SecurityHandler();
    authenticationRegistration = bundleContext.registerService( AuthenticationHandler.class.getName(), securityService, null );
    authorizationRegistration = bundleContext.registerService( AuthorizationHandler.class.getName(), securityService, null );
    System.out.println("SecurityHandler registered");
  }

  @Override
  public void stop( BundleContext bundleContext ) throws Exception {
    authenticationRegistration.unregister();
    authorizationRegistration.unregister();
    resourceRegistration.unregister();
  }
}
*/

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager)
			throws Exception {
		
		// Registration of REST resources
		manager.add(createComponent()
				.setInterface(Resources.class.getName(), null)
				.setImplementation(Resources.class));
		
		manager.add(createComponent()
				.setInterface(new String[] {AuthenticationHandler.class.getName(),AuthorizationHandler.class.getName()}, null)
				.setImplementation(SecurityHandler.class)
				.add(createServiceDependency().setService(JWTService.class).setRequired(true)));
		
		// Registration of Security Service
		manager.add(createComponent()
				.setInterface(SecurityService.class.getName(),null)
				.setImplementation(SecurityServiceImpl.class));
	}
	
	@Override
	public void destroy(BundleContext context, DependencyManager manager)
			throws Exception {

	}
}
