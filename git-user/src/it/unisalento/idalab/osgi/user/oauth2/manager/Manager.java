package it.unisalento.idalab.osgi.user.oauth2.manager;

import java.util.Map;

public interface Manager {
	public Map<String, Object> authenticate(String code, String name);
}