import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.osgi.framework.ServiceReference;

import it.hash.osgi.business.persistence.mock.BusinessServicePersistenceImpl;

import org.apache.felix.ipojo.junit4osgi.OSGiTestCase;
public class businesspersistencemock extends OSGiTestCase {

	public class ServiceTest extends OSGiTestCase {
		// Test Location Tracker Service Availability
		public void testLocationTrackerServiceAvailability() {
		    ServiceReference reference = context
		            .getServiceReference(BusinessServicePersistenceImpl.class.getName());
		    assertNotNull("Assert Availability", reference);
		}

		public void testLocationMessage() {
		    ServiceReference reference = context
		            .getServiceReference(BusinessServicePersistenceImpl.class.getName());
		    assertNotNull("Assert Availability", reference);
		    BusinessServicePersistenceImpl service = (BusinessServicePersistenceImpl) context
		            .getService(reference);
		    String message = service.getImplementation();
		    assertNotNull("Check Message Existance", message);
		    assertEquals("Check The Message", "mocked", message);
		 }
		}

}
