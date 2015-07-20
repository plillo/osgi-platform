package it.unisalento.idalab.osgi.user.oauth2.metatypeEx;

import java.util.Dictionary;
import java.util.Enumeration;

import org.osgi.framework.FrameworkUtil;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

public class ExampleManagedService implements ManagedService {
	
	private volatile MetaTypeService service;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void updated(Dictionary properties)
    {
        System.out.println("Updating configuration properties for ExampleManagedService");
 
        if (properties != null)
        {
            Enumeration<String> keys = properties.keys();
            
            MetaTypeInformation info = service.getMetaTypeInformation(FrameworkUtil.getBundle(this.getClass()));
        	ObjectClassDefinition ocd = info.getObjectClassDefinition("it.unisalento.idalab.osgi.user.oauth2.metatypeEx", null);
        	AttributeDefinition[] attributes = ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);
        	
        	for (int i=0; i<attributes.length; i++)
        	{
        		System.out.println("METATYPE ATTRIBUTE "+i+": ");
        		System.out.println("METATYPE INFO: " + attributes[i].getDescription());
        		System.out.println("METATYPE INFO: " + attributes[i].getName());
        		System.out.println("METATYPE INFO: " + attributes[i].getType());
        		System.out.println("METATYPE INFO: " + attributes[i].getDefaultValue()[0]);
        	}
 
            while (keys.hasMoreElements())
            {
                String key = keys.nextElement();                
                System.out.println(key + " : " + properties.get(key));
            }
        }
        else
        {
        	System.out.println("properties vuote");
        }
    }

}
