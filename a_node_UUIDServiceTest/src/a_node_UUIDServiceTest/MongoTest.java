package a_node_UUIDServiceTest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.amdatu.mongo.MongoDBService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;

import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.resource.uuid.mongo.UUIDServiceImpl;

import org.amdatu.bndtools.test.BaseOSGiServiceTest;

@SuppressWarnings("restriction")
public class MongoTest extends BaseOSGiServiceTest<UUIDService> {
	private List<String> uuid=new ArrayList<String>();

	public MongoTest() {
		super(UUIDService.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	@Before
	public void setUp() throws Exception {
		// super.setUp();
		Properties mongoProperties = new Properties();
		mongoProperties.put("dbName", "demo");
		configureFactory("org.amdatu.mongo", mongoProperties);
		addServiceDependencies(MongoDBService.class);
		super.setUp();
		if (uuid.isEmpty()) {
			uuid.add(instance.createUUID("app/business"));
			System.out.println("CREATO : - "+uuid.get(uuid.size() - 1));
		}

	}



	@After
	public void tearDown() {
	
		
		if (!uuid.isEmpty()) {
			for (int i = 0; i < uuid.size(); i++) {
				System.out.println("Remove: " + uuid.get(i));
				instance.removeUUID(uuid.get(i));

			}
		
/*		List<String> list=instance.listUUID("app/business");
		if (!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				System.out.println("Remove: " + list.get(i));
				instance.removeUUID(list.get(i));

			}*/
			
		}

	}
@Test
	public void testFind() throws Exception {
	System.out.print("TestFind: ");
		List<String> findUuid = instance.listUUID("app/business");
		System.out.println("false - "+findUuid.isEmpty());
		assertEquals(false, findUuid.isEmpty());
	}
@Test
	public void testCreateUUID() throws Exception {
		String uuidS = instance.createUUID("app/business");
        uuid.add(uuidS);
		System.out.println("TestCreateUUID  " + uuidS);
		System.out.println("false - "+uuidS.equals(null));
		
		assertNotNull( uuidS);
	}
@Test
	public void testRemoveUUID() throws Exception {
			Map<String, Object> response;

		response = instance.removeUUID(uuid.get(uuid.size() - 1));
		System.out.println("Cancellato  " + response.get("uuid"));
		
		System.out.println("notNull - "+response.get("uuid"));
	
		assertNotNull(response.get("uuid"));

	}

@Test	
public void testListUUID() {

		List<String> list = instance.listUUID("app/business");
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();)
			System.out.println("TestListType "+iterator.next());
	
		System.out.println("false - "+list.isEmpty());
	
		assertEquals(false, list.isEmpty());
	}
@Test
	public void testGetTypeUUID() {
		String type = "app/business";

		List<String> findUuid = instance.listUUID(type);
		String uuid = findUuid.get(0);
		Map<String, Object> uuid1 = instance.getTypeUUID(uuid);
		
		System.out.println("TestGetTypeUUIDUUID  " + type);
		System.out.println("app/business - "+uuid1.get("type"));
	
		assertEquals("app/business", uuid1.get("type"));
	}


}
