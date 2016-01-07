package slf4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Test {
	private static final Log logger = LogFactory.getLog( Test.class );
	
	public void echo(String echo) {
		logger.debug("LOG command: echo");
		
		System.out.println("echo command: "+echo);
	}
}
