package it.unisalento.idalab.osgi.user.oauth2.configurator;

import it.unisalento.idalab.osgi.user.oauth2.authenticator.Authenticator;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class OAuth2Configurator implements ManagedService{
	
//	ArrayList<Authenticator> authenticators = new ArrayList<Authenticator>();
	Map<String, Authenticator> authenticators = new HashMap<String, Authenticator>();
	Dictionary properties;

	@Override
	public void updated(Dictionary properties) throws ConfigurationException {
		// TODO Auto-generated method stub
		this.properties = properties;
//		for (int i=0; i<=authenticators.size(); i++)
		for (Map.Entry<String, Authenticator> entry : authenticators.entrySet())
			sendParameters(properties, entry.getValue());
	}
	
	public void add(Authenticator auth){
//		authenticators.add(auth);
		authenticators.put(auth.getName(), auth);
		System.out.println("Registering in whiteboard (config): "+auth.getName());
		sendParameters(properties, auth);
	}
	
	public void remove(Authenticator auth){
		authenticators.remove(auth);
		System.out.println("Deleting from whiteboard (config): "+auth.getName());
	}
	
	public void sendParameters(Dictionary properties, Authenticator auth)
	{
		System.out.println("INVIO PARAMETRI OAUTH2");
		Map<String, String> parameters = new HashMap<String, String>();
		
		parameters.put("fixParameters_"+auth.getName(), 
				(String) properties.get("fixParameters_"+auth.getName()));
		parameters.put("tokenURL_"+auth.getName(), 
				(String) properties.get("tokenURL_"+auth.getName()));
		parameters.put("userInfoURL_"+auth.getName(), 
				(String) properties.get("userInfoURL_"+auth.getName()));
		
		auth.setParameters(parameters);
	}
}
