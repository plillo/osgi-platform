package slf4;

import org.osgi.service.log.LogService;

public class Test {
	private volatile LogService logService;
	
	public void echo(String echo) {
		if(logService!=null)
			logService.log(LogService.LOG_INFO, "LOG command: echo");
		
		System.out.println("echo command: "+echo);
	}
}
