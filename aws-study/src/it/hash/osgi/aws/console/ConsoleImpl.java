package it.hash.osgi.aws.console;

import java.util.Dictionary;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

public class ConsoleImpl implements Console, ManagedService{
    AWSCredentials credentials = null;
    Dictionary<String, ?> properties = null;
	@SuppressWarnings("unused")
	private volatile LogService logService;
	
	void setCredentials(Dictionary<String, ?> properties){
		if(properties==null){
			System.out.printf("Error accessing AWS credentials");
			return;
		}

		try {
			//credentials = new ProfileCredentialsProvider("elpablito").getCredentials();
			String access_key_id = (String)properties.get("aws_access_key_id");
			String secret_access_key = (String)properties.get("aws_secret_access_key");
			credentials = new BasicAWSCredentials(access_key_id, secret_access_key);
		} catch (Exception e) {
			System.out.printf(e.toString());
			throw new AmazonClientException(""
					+ "Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (C:\\Users\\Paolo\\.aws\\credentials), and is in valid format.", e);
		}
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		this.properties = properties;
		if(properties!=null)
			setCredentials(properties);
		
	}

	@Override
	public AWSCredentials getCredentials() {
		return credentials;
	}
}
