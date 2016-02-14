package it.hash.osgi.activemq;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
 
public class TokenAuthenticationPlugin implements BrokerPlugin {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	Map<String, String> authConfig;
	 
	@Override
	public Broker installPlugin(Broker broker) throws Exception {
		  logger.info("Started Authorization plugin: [{}]",getClass());

		  return new TokenAuthenticationBrokerFilter(broker, authConfig);
	}
	 
	public Map<String, String> getAuthConfig() {
		  return authConfig;
	}
	 
	public void setAuthConfig(Map<String, String> authConfig) {
		  this.authConfig = authConfig;
	}
}
