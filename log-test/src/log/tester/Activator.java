package log.tester;

import org.apache.felix.dm.tracker.ServiceTracker;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator
{
    public void start(BundleContext context) throws Exception {
        ServiceTracker logServiceTracker = new ServiceTracker(context, org.osgi.service.log.LogService.class.getName(), null);
        logServiceTracker.open();
        LogService logservice = (LogService) logServiceTracker.getService();
       
        if (logservice != null) {
        	System.out.println("Found log service!");
            logservice.log(LogService.LOG_INFO, "hey, I logged that!");
        }
        else
        	System.out.println("Missing log service");
    }
   
    public void stop(BundleContext context) throws Exception {
    }
}
