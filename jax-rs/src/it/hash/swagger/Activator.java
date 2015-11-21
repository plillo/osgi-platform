package it.hash.swagger;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;


public class Activator implements BundleActivator {

  private ServiceRegistration registration;

  @Override
  public void start( BundleContext context ) throws Exception {
    configureSwagger( context );
    
    System.out.println("Swagger activated");
    //UserService exampleService = new UserService();
    //registration = context.registerService( UserService.class.getName(), exampleService, null );
  }

  private void configureSwagger( BundleContext context ) throws Exception {
    ServiceReference reference = context.getServiceReference( ConfigurationAdmin.class.getName() );
    ConfigurationAdmin configAdmin = ( ConfigurationAdmin )context.getService( reference );
    Configuration configuration = configAdmin.getConfiguration( "com.eclipsesource.jaxrs.swagger.config", null );


    Dictionary<String, Object> properties = new Hashtable<>();

    properties.put( "swagger.basePath", "/api" );
    properties.put( "swagger.host", "localhost:8080" );
    // swagger.filterClass
    properties.put( "swagger.info.title", "A Swagger test API" );
    properties.put( "swagger.info.description", "This API only exist to test swagger support" );
    properties.put( "swagger.info.version", "1.0" );
    properties.put( "swagger.info.termsOfService", "Free to enjoy" );
    properties.put( "swagger.info.contact.name", "Holger Staudacher" );
    properties.put( "swagger.info.contact.url", "https://github.com/hstaudacher/osgi-jax-rs-connector" );
    properties.put( "swagger.info.contact.email", "holger.staudacher@gmail.com" );
    properties.put( "swagger.info.license.name", "Eclipse Public License, version 1.0" );
    properties.put( "swagger.info.license.url", "http://www.eclipse.org/legal/epl-v10.html" );
    configuration.update( properties );
    context.ungetService( reference );
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    //registration.unregister();
  }
}
