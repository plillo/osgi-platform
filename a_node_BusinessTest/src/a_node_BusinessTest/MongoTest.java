package a_node_BusinessTest;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.amdatu.mongo.MongoDBService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventAdmin;

import it.hash.osgi.business.Business;
import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.business.service.BusinessService;
import it.hash.osgi.business.service.BusinessServiceImpl;
import it.hash.osgi.resource.uuid.api.UUIDService;

import org.amdatu.bndtools.test.BaseOSGiServiceTest;

@SuppressWarnings("restriction")
public class MongoTest extends BaseOSGiServiceTest<BusinessServicePersistence> {
	private volatile UUIDService uuidservice;
	Map<String, Object> pars;
	String uuid;
	String uuid1;
	Business business;

	public MongoTest() {
		super(BusinessServicePersistence.class);

	}

	@Override
	public void setUp() throws Exception {
		List<String> categories = new ArrayList<String>();
		Properties mongoProperties = new Properties();
		mongoProperties.put("dbName", "demo");
		configureFactory("org.amdatu.mongo", mongoProperties);

		addServiceDependencies(MongoDBService.class, UUIDService.class);
		super.setUp();
		uuid = uuidservice.createUUID("app/business");
		uuid1 = uuidservice.createUUID("app/business");

		pars = new HashMap<String, Object>();
		pars.put("businessName", "Montinari");
		pars.put("codiceFiscale", "MNTNNL");
		pars.put("email", "montinari");
		pars.put("mobile", "34588");
		pars.put("uuid", uuid1);
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
		Business findBusiness = instance
				.getBusinessByBusinessName(((Business) response.get("business")).getBusinessName());
		System.out.println(" BusinessName notNull - " + findBusiness);

		assertNotNull("è stato trovato", findBusiness);

		System.out.println(
				" TESTFINDBUSINESS - CodiceFiscale" + ((Business) response.get("business")).getCodiceFiscale());
		findBusiness = instance.getBusinessByCodiceFiscale(businessCodiceFiscale);
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

}
