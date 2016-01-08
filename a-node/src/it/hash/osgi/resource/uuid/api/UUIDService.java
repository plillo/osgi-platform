package it.hash.osgi.resource.uuid.api;

import java.util.Map;

public interface UUIDService {
	public String createUUID(String type);
	public Map<String,Object> getUUID(String uuid);
}
