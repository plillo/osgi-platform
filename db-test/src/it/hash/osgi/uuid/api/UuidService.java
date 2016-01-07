package it.hash.osgi.uuid.api;

import java.util.Map;

public interface UuidService {
	public Map<String,Object> createUuid();
	public Map<String, Object> createFromString(String s);
	public Map<String,Object> getUuid(Map<String,Object> pars);
	public Map<String,Object> compareTo(Map<String,Object> pars);
      
}
