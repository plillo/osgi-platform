package it.unisalento.idalab.osgi.user.client;

import it.unisalento.idalab.osgi.user.api.UserService;

public class Client {
	private volatile UserService service;
	
	public void start() {
		System.out.println(service.login("pippo", "wowwow").toString());
	}
}
