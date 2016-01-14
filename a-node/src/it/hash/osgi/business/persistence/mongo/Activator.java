package it.hash.osgi.business.persistence.mongo;

import java.util.Properties;

import org.amdatu.mongo.MongoDBService;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.log.LogService;

import it.hash.osgi.business.persistence.api.BusinessServicePersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;
      
public class Activator extends DependencyActivatorBase {
    @Override
    
    public synchronized void init(BundleContext context, DependencyManager manager) throws Exception {
    	Properties props = new Properties();
		props.put(Constants.SERVICE_RANKING, 200);
    	
		manager.add(createComponent()
        	.setInterface(BusinessServicePersistence.class.getName(), props)
            .setImplementation(BusinessServicePersistenceImpl.class)
            .add(createServiceDependency()
                    .setService(LogService.class)
                    .setRequired(false))
            .add(createServiceDependency()
                .setService(MongoDBService.class)
                .setRequired(true))
            .add(createServiceDependency()
            		.setService(UUIDService.class)
            		.setRequired(true))
                );
    }

    
    @Override
    public synchronized void destroy(BundleContext context, DependencyManager manager) throws Exception {
    }
}
