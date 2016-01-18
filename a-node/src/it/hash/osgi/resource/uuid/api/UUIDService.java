package it.hash.osgi.resource.uuid.api;

import java.util.List;
import java.util.Map;

public interface UUIDService {
	public String createUUID(String type);
	public Map<String,Object> getTypeUUID(String uuid);
	public Map<String,Object> removeUUID(String uuid);
	public List<String> listUUID(String type);
	
}
