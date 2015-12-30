package it.hash.osgi.broker;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class BrokerServiceImpl implements BrokerService, ManagedService {
	@SuppressWarnings("rawtypes")
	private Dictionary properties;
	private CoreMqttClient mqttClient;
	
	@Override
	public void send() {
		// TODO Auto-generated method stub
	}

	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary properties) throws ConfigurationException {
		if(properties==null)
			return;
		
		this.properties = properties;
		
		if(mqttClient!=null)
			mqttClient.stopClient();
		
		mqttClient = new CoreMqttClient(this.properties);
		mqttClient.runClient();
	}

}
