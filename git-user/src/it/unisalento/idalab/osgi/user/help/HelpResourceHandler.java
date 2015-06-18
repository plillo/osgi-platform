package it.unisalento.idalab.osgi.user.help;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Servlet;

import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.Bundle;

public class HelpResourceHandler {
	private volatile DependencyManager dm;
	private final Map<Long, Component> components = new ConcurrentHashMap<>();
	
	public void bundleAdded(Bundle bundle){
		System.out.println("Help Resource bundle found: " +
				bundle.getSymbolicName() +
				" with resources: " +
				bundle.getHeaders().get("X-HelpResource"));
		
		ResourceServlet servlet = new ResourceServlet(bundle);
		
		Properties properties = new Properties();
		properties.put("alias", bundle.getHeaders().get("X-HelpResource"));
		
		System.out.println("Registering servlet component with alias: "+bundle.getHeaders().get("X-HelpResource"));
		
		Component component = dm.createComponent()
				.setInterface(Servlet.class.getName(), properties)
				.setImplementation(servlet);
		components.put(bundle.getBundleId(), component);
		dm.add(component);
	}
	
	public void bundleRemoved(Bundle bundle) {
		System.out.println("Help Resource bundle removed");
		
		Component component = components.get(bundle.getBundleId());
		dm.remove(component);
		components.remove(bundle.getBundleId());
	}
}
