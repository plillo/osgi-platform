package it.hash.osgi.user.shell;

import java.util.Properties;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleContext;

import it.hash.osgi.user.persistence.api.UserServicePersistence;

public class Activator extends DependencyActivatorBase {
	@Override
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(CommandProcessor.COMMAND_SCOPE, "user");
		props.put(CommandProcessor.COMMAND_FUNCTION, new String[] {"adduser", "listuser", "getusermail","getuser", "removeuser", "updateuser", "login", "loginOA", "validate"});
		manager.add(createComponent().setInterface(Object.class.getName(), props)
				.setImplementation(Commands.class).add(createServiceDependency().setService(UserServicePersistence.class).setRequired(false)));
    }

    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}

