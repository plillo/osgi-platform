package a_node_BusinessTest;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyManager;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.business.service.BusinessServiceImpl;
import it.hash.osgi.business.service.api.BusinessService;
import it.hash.osgi.resource.uuid.api.UUIDService;
import junit.framework.TestCase;

import org.amdatu.bndtools.test.BaseOSGiServiceTest;

@SuppressWarnings("restriction")
public  class MongoTest extends TestCase {
//public class MongoTest extends BaseOSGiServiceTest<BusinessServicePersistence> {
	private volatile UUIDService uuidservice;
	
	 protected final BundleContext context;
	    protected DependencyManager dependencyManager;
	    protected volatile  BusinessServicePersistence instance;
	    protected final Class<BusinessServicePersistence> clazz;
	    private List<Configuration> m_currentConfigurations = new ArrayList<Configuration>();
	    protected volatile ConfigurationAdmin configurationAdmin;
	    private CountDownLatch countDownLatch;
	    private List<Class<?>> serviceDependencies = new ArrayList<Class<?>>();
	   
	
	
	Map<String, Object> pars;
	String uuid;
	String uuid1;
	Business business;

	public MongoTest() {
		 this.clazz =BusinessServicePersistence.class ;
	        context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();

	}

	@Override
	public void setUp() throws Exception {
		List<String> categories = new ArrayList<String>();
		Properties mongoProperties = new Properties();
		mongoProperties.put("dbName", "demo");
		configureFactory("org.amdatu.mongo", mongoProperties);
		addServiceDependencies( UUIDService.class);
		
		  dependencyManager = new DependencyManager(context);

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

	        boolean created = countDownLatch.await(10, TimeUnit.SECONDS);
	        if (!created) {
	            fail("Service instance could not be injected");
	        }

		
		
	//	addServiceDependencies(MongoDBService.class, UUIDService.class);
	//	super.setUp();
		uuid = uuidservice.createUUID("app/business");
		uuid1 = uuidservice.createUUID("app/business");

		pars = new HashMap<String, Object>();
		pars.put("businessName", "Montinari");
		pars.put("codiceFiscale", "MNTNNL");
		pars.put("email", "montinari");
		pars.put("mobile", "34588");
		pars.put("uuid", uuid1);
		pars.put("Attributo","Nuovo");
		categories.add("1");
		categories.add("2");
		pars.put("categories", categories);
	
		business = new Business();
		business.setUuid(uuid);
		business.setBusinessName("Montina");
		business.setCodiceFiscale("MNT");
		business.setEmail("montina");
		business.setMobile("3458834978");
		business.setCategories(categories);

		Map <String,Object> create=instance.addBusiness(pars);
		System.out.println("CREATO Business with UUID "+((Business) create.get("business")).getUuid());

	}

	public void tearDown() {
		// TODO implementare il metodo per vedere se due business s

		uuidservice.removeUUID(uuid);
		uuidservice.removeUUID(uuid1);
		Map<String, Object> pars = new HashMap<String, Object>();
		List<Business> list = instance.getBusinesses();
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Business b = list.get(i);
				pars.put("businessName", b.getBusinessName());
				pars.put("codiceFiscale", b.getCodiceFiscale());
				pars.put("email", b.getEmail());
				pars.put("mobile", b.getMobile());

				instance.deleteBusiness(pars);
			}
		}

	}

	@Test
	public void testAddBusiness() { // business non esistente
		System.out.println(" TEST ADD BUSINESS - NON ESISTENTE");

		Map<String, Object> createBusiness = instance.addBusiness(business);
		System.out.println("true - " + createBusiness.get("created"));
		System.out.println("TestAddBusiness - not null - " + createBusiness.get("business"));
		assertEquals(true, createBusiness.get("created"));
		assertNotNull(createBusiness.get("business"));

		System.out.println("");

	}

	@Test
	public void testFindBusiness() {
		System.out.println(" TESTFINDBUSINESS -");

		Map<String, Object> response = instance.addBusiness(business);
		String businessCodiceFiscale = business.getCodiceFiscale();
		Map<String,Object> find = instance.getBusiness(business);
		System.out.println(" BusinessName notNull - " + find.get("business"));

		assertNotNull("è stato trovato", find.get("business"));

		System.out.println(
				" TESTFINDBUSINESS - CodiceFiscale" + ((Business) response.get("business")).getCodiceFiscale());
		Business findBusiness = instance.getBusinessByCodiceFiscale(businessCodiceFiscale);
		System.out.println("codiceFiscale notNull - " + findBusiness);
		assertNotNull("è stato trovato", findBusiness);

		/*
		 * findBusiness = instance.getBusinessByUuid(business.getUuid());
		 * System.out.println(" UUID notNull - " + findBusiness); assertNotNull(
		 * "è stato trovato", findBusiness);
		 * 
		 * 
		 */

	}

	@Test
	public void testAddBusinessMap() {

		// business già esistente System.out.println(" testAddBusinessMap()");
		Map<String, Object> createBusiness = instance.addBusiness(pars);
		System.out.println(" Business created - false " + createBusiness.get("created") + " già esiste!!!");

		assertEquals("business  created", false, createBusiness.get("created"));

	}

	@Test
	public void testDeleteBusiness() {
		Map<String, Object> pars = new HashMap<String, Object>();
		String u = uuidservice.createUUID("app/business");
		System.out.println("testDeleteBusiness\n \n");
		pars.put("businessName", "Carluccio");
		pars.put("codiceFiscale", "CRLMTT");
		pars.put("email", "carluccio");
		pars.put("mobile", "327013");
		pars.put("uuid", u);

		Map<String, Object> deleteBusiness = instance.addBusiness(pars);

		Map<String, Object> findBusiness = instance.getBusiness(pars);
		Business findB = (Business) findBusiness.get("business");

		Map<String, Object> pars1 = new HashMap<String, Object>();
		pars1.put("uuid", findB.getUuid());

		deleteBusiness = instance.deleteBusiness(pars1);
		assertEquals(true, deleteBusiness.get("delete"));
		System.out.println(" DeletedBusiness - notNull - " + deleteBusiness.get("business"));

		assertNotNull(deleteBusiness.get("business"));
		uuidservice.removeUUID(u);

	}

	@Test
	public void testUguali() {
		System.out.println("Confronto di " + uuid1);
		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("uuid", uuid1);

		Map<String,Object> response =  instance.getBusiness(pars);
        Business b1= (Business) response.get("business");
		Business b2 = new Business();
		b2.set_id(b1.get_id());
		b2.setBusinessName(b1.getBusinessName());
		b2.setCodiceFiscale(b1.getCodiceFiscale());
		b2.setPIva(b1.getPIva());
		b2.setUuid(b1.getUuid());
		System.out.println(" uguali - True - " + b1.equals(b2));

		assertEquals(true, b1.equals(b2));

	}
//TODO implementare test per UPDATE 
	
	
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

}
