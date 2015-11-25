package it.hash.osgi.authentication.shell;

import org.osgi.service.log.LogService;

public class Commands {
	private volatile LogService logService;
	
	public void echo(String echo) {
		if(logService!=null)
			logService.log(LogService.LOG_INFO, "executing command: echo");
		System.out.println("echo command: "+echo);
	}
}


