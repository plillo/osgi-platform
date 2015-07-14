package it.unisalento.idalab.osgi.user.oauth2.manager;

import it.unisalento.idalab.osgi.user.api.UserService;
import it.unisalento.idalab.osgi.user.oauth2.authenticator.Authenticator;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ManagerImpl implements Manager {
	@SuppressWarnings("unused")
	private volatile UserService _userService;

	Map<String, Authenticator> authenticators = new HashMap<String, Authenticator>();
	
	public void add(Authenticator auth){
		System.out.println("Registering in whiteboard: "+auth.getName());
		authenticators.put(auth.getName(), auth);
	}
	
	public void remove(Authenticator auth){
		System.out.println("Deleting from whiteboard: "+auth.getName());
		authenticators.remove(auth);
	}
	
	public Map<String, Object> authenticate(String code, String name){
		Map<String, Object> mapInfo = new TreeMap<String, Object>();
		Authenticator auth = authenticators.get(name);
		if(auth!=null){
			System.out.println("Authenticating from: "+name);

			String token = auth.getToken(code);
			// TODO: gestire caso token = null
			mapInfo = auth.getUserInfo(token);
		}

		return mapInfo;
	}
}
