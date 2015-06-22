package it.unisalento.idalab.osgi.user.persistencemongo;



import it.unisalento.idalab.osgi.user.api.UserServicePersistence;

import java.util.Properties;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;


public class Activator extends DependencyActivatorBase {
    @Override
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(Constants.SERVICE_RANKING, 2);
    	manager.add(createComponent()
        	.setInterface(UserServicePersistence.class.getName(), props)
            .setImplementation(MongoUserService.class)
            .add(createServiceDependency()
                .setService(MongoDBService.class)
                .setRequired(true))
                );
    }

    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}