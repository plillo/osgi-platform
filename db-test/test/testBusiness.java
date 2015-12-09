import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;

import it.hash.osgi.business.Business;
import junit.framework.TestCase;

public class testBusiness{

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGet_id() {
		Business b = new Business();
		b.set_id("5");
		assertEquals("Result", "5", b.get_id());
	}

	@Test
	public void testSetUsername() {
		Business b = new Business();
		b.setUsername("Lillo");
		assertEquals("Result", "Lillo", b.getUsername());
	}

	@Test
	public void testSetPassword() {
		Business b = new Business();
		b.setPassword("Lillo");
		assertEquals("Result", "Lillo", b.getPassword());
	}



	@Test
	public void testEquals() {
	Business b=new Business();
	b.set_id("5");
	Business c= new Business();
	c.set_id("5");
	assertEquals("Result", 0, b.compareTo(c));
	c.set_id("6");
	
	assertEquals("Result", -1, b.compareTo(c));

	}

}
