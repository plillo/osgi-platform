package it.hash.osgi.authentication.shell;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "auth");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[] { "echo" });

		manager.add(createComponent()
			.setInterface(Object.class.getName(), props)
			.setImplementation(Commands.class)
			.add(createServiceDependency().setService(LogService.class)));

		System.out.println("Authentication shell actived");
	}

	@Override
	public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
	}

}

