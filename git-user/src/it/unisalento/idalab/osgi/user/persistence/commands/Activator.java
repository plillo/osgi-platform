package it.unisalento.idalab.osgi.user.persistence.commands;

import it.unisalento.idalab.osgi.user.persistence.api.UserServicePersistence;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;






public class Activator extends DependencyActivatorBase {
	@Override
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "usermanage");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[] {"adduser", "listuser", "getusermail", "removeuser", "updateuser", "login","validateU"});
		manager.add(createComponent().setInterface(Object.class.getName(), props)
				.setImplementation(Console.class).add(createServiceDependency().setService(UserServicePersistence.class).setRequired(false)));
    }

    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}

