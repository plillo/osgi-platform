package it.unisalento.idalab.osgi.user.commands;

import it.unisalento.idalab.osgi.user.password.Password;
import it.unisalento.idalab.osgi.user.api.UserService;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;


public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "user");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[]{"login","testtoken","create","list"});
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), props)
				.setImplementation(Commands.class)
				.add(createServiceDependency()
						.setService(UserService.class)
						.setRequired(true))
				.add(createServiceDependency()
						.setService(Password.class)
						.setRequired(false)));
		}

		@Override
		public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
		}

}
