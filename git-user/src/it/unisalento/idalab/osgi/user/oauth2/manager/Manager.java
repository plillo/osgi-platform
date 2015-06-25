package it.unisalento.idalab.osgi.user.oauth2.manager;

import it.unisalento.idalab.osgi.user.api.UserService;
import it.unisalento.idalab.osgi.user.oauth2.authenticator.Authenticator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Manager {
	@SuppressWarnings("unused")
	private volatile UserService _userService;
	Map<String, Authenticator> authenticators = new HashMap<String, Authenticator>();
	
	public void add(Authenticator auth){
		authenticators.put(auth.getName(), auth);
	}
	
	public void remove(Authenticator auth){
		authenticators.remove(auth);
	}
	
	@SuppressWarnings("unused")
	public Map<String, Object> authenticate(String code, String name){
		Authenticator auth = authenticators.get(name);
		if(auth!=null){
			String token = auth.getToken(code);
			Map<String, Object>mapInfo = auth.getUserInfo(token);

			// TODO: inserire qui la logica di login/creazione dell'utente usando _userService
		}

		return new TreeMap<String, Object>();
	}
}
