package a_node_BusinessServiceTest;

import junit.framework.TestCase;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyManager;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.business.service.api.BusinessService;
import it.hash.osgi.resource.uuid.api.UUIDService;

public class BusinessServiceTest extends TestCase {

	 protected DependencyManager dependencyManager;
	    protected volatile BusinessService instance;
	    
	    protected final Class<BusinessService> clazz;
	    private List<Configuration> m_currentConfigurations = new ArrayList<Configuration>();
	    protected volatile ConfigurationAdmin configurationAdmin;
	    private CountDownLatch countDownLatch;
	    private List<Class<?>> serviceDependencies = new ArrayList<Class<?>>();
	   
    private final BundleContext context;//= FrameworkUtil.getBundle(this.getClass()).getBundleContext();

    
    public BusinessServiceTest(){
    	context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
    	this.clazz= BusinessService.class;
    	
    }
  
    public void setUp() throws Exception {
        dependencyManager = new DependencyManager(context);
    	Properties mongoProperties = new Properties();
		mongoProperties.put("dbName", "demo");
		String s=	configureFactory("org.amdatu.mongo", mongoProperties);
		 System.out.println(s);

		addServiceDependencies(MongoDBService.class, UUIDService.class,BusinessService.class,Business.class);

        countDownLatch = new CountDownLatch(serviceDependencies.size() + 1);

        Component component =
                dependencyManager
                    .createComponent()
                    .setImplementation(this)
                    .add(
                        dependencyManager.createServiceDependency().setService(clazz).setRequired(true)
                            .setAutoConfig("instance").setCallbacks("serviceInjected", "serviceRemoved"));

            for (Class<?> serviceClass : serviceDependencies) {
                component.add(dependencyManager.createServiceDependency().setService(serviceClass).setRequired(true)
                    .setAutoConfig(true).setCallbacks("serviceInjected", "serviceRemoved"));
            }

        dependencyManager.add(component);
      
        
        
        //   startBundle("business.service");
        boolean created = countDownLatch.await(10, TimeUnit.SECONDS);
        if (!created) {
            fail("Service instance could not be injected");
        }
    }
    
    public void stopBundle(String bundleSymbolicName) {
        for (Bundle bundle : context.getBundles()) {
            if (bundle.getSymbolicName().equals(bundleSymbolicName)) {
                try {
                    bundle.stop();
                }
                catch (BundleException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
    }
    
    public void startBundle(String bundleSymbolicName) {
        for (Bundle bundle : context.getBundles()) {
        	System.out.println(bundle.getSymbolicName());
            if (bundle.getSymbolicName().equals(bundleSymbolicName)) {
                try {
                    bundle.start();
           
                }
                catch (BundleException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
    }


    /**
     * Override if you want to add confiuration besides the default configuration.
     */
    protected void configureBeforeTests() {
    }

    @SuppressWarnings("unused")
    private void serviceInjected() {
        countDownLatch.countDown();
    }

    protected void addServiceDependencies(Class<?>... services) {
        for (Class<?> clazz : services) {
            serviceDependencies.add(clazz);
        }
    }

    /**
     * Write configuration for a single service. For example,
     * <pre>
     * configure("org.apache.felix.http",
     * "org.osgi.service.http.port", "1234");
     * </pre>
     */
    protected void configure(String pid, Properties properties) {
        Configuration config;
        try {
            config = getConfiguration(pid);
            config.update(properties);
            m_currentConfigurations.add(config);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Properties properties(Object... values) {
        Properties props = new Properties();
        for (int i = 0; i < values.length; i += 2) {
            props.put(values[i], values[i + 1]);
        }
        return props;
    }

    protected void configure(String pid, String... props) {
        Properties properties = properties((Object[]) props);
        Configuration config;
        try {
            config = getConfiguration(pid);
            config.update(properties);
            m_currentConfigurations.add(config);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void removeConfig(String pid) {
        Configuration config;
        try {
            config = getConfiguration(pid);
            config.delete();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Creates a factory configuration with the given properties, just like {@link #configure}.
     * 
     * @return The PID of newly created configuration.
     */
    protected String configureFactory(String factoryPid, Properties properties) throws IOException {
        Configuration config = createFactoryConfiguration(factoryPid);
        config.update(properties);
        m_currentConfigurations.add(config);
        //waitForConfig();
        return config.getPid();
    }
    
    protected BundleContext getBundleContextFromService() throws NoSuchFieldException, IllegalAccessException {
        Field bundleContextField = null;
        try {
            bundleContextField = instance.getClass().getDeclaredField("m_bundleContext");
        }
        catch (NoSuchFieldException ex) {
            bundleContextField = instance.getClass().getSuperclass().getDeclaredField("m_bundleContext");
        }

        bundleContextField.setAccessible(true);
        BundleContext bundleContext = (BundleContext) bundleContextField.get(instance);
        return bundleContext;
    }

    protected void restartServiceBundle() throws NoSuchFieldException, IllegalAccessException, BundleException {
        Bundle bundle = getBundleContextFromService().getBundle();
        bundle.stop();
        bundle.start();
    }

    private Configuration getConfiguration(String pid) throws IOException {
    	checkConfigAdmin();
        return configurationAdmin.getConfiguration(pid, null);
    }

    private Configuration createFactoryConfiguration(String factoryPid) throws IOException {
    	checkConfigAdmin();
        return configurationAdmin.createFactoryConfiguration(factoryPid, null);
    }
    
    @SuppressWarnings({ "unchecked", "hiding" })
    protected <T> T getService(Class<T> serviceClass, String filterString) throws InvalidSyntaxException {
        T serviceInstance = null;

        ServiceTracker serviceTracker;
        if (filterString == null) {
            serviceTracker = new ServiceTracker(context, serviceClass.getName(), null);
        }
        else {
            String classFilter = "(" + Constants.OBJECTCLASS + "=" + serviceClass.getName() + ")";
            filterString = "(&" + classFilter + filterString + ")";
            serviceTracker = new ServiceTracker(context, context.createFilter(filterString), null);
        }
        serviceTracker.open();
        try {
            serviceInstance = (T) serviceTracker.waitForService(2 * 1000);

            if (serviceInstance == null) {
                fail(serviceClass + " service not found.");
            }
            else {
                return serviceInstance;
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            fail(serviceClass + " service not available: " + e.toString());
        }

        return serviceInstance;
    }

    @SuppressWarnings("hiding")
	protected <T> T getService(Class<T> serviceClass) {
        try {
            return getService(serviceClass, null);
        }
        catch (InvalidSyntaxException e) {
            return null;
            // Will not happen, since we don't pass in a filter.
        }
    }
    
    private void checkConfigAdmin() {
        if (configurationAdmin == null) {
            configurationAdmin = getService(ConfigurationAdmin.class);
        }
    }


    @SuppressWarnings("restriction")
	public void testExample() throws Exception {
        // TODO: method provided by template
    	Business business=new Business();
    	business.setBusinessName("Montina");
		business.setCodiceFiscale("MNT");
		business.setEmail("montina");
		business.setMobile("3458834978");

    	Map <String,Object> response =instance.create(business);
    	System.out.println(" Creato - " +response.get("business"));
    
    }
}
