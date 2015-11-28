package it.hash.osgi.security;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eclipsesource.jaxrs.provider.security.AuthenticationHandler;
import com.eclipsesource.jaxrs.provider.security.AuthorizationHandler;


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
