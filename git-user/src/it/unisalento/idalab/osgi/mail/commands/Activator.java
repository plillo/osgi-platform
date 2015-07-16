package it.unisalento.idalab.osgi.mail.commands;

import it.unisalento.idalab.osgi.mail.api.SmtpSender;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;

public class Activator extends DependencyActivatorBase {

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "mail");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[]{"send","test"});
		
		manager.add(createComponent()
				.setInterface(Object.class.getName(), props)
				.setImplementation(Commands.class)
				.add(createServiceDependency()
						.setService(SmtpSender.class)
						.setRequired(true)));
		}

		@Override
		public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
		}

}
