package it.unisalento.idalab.osgi.user.events;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class UserEventHandler implements EventHandler{

	@Override
	public void handleEvent(Event event) {
		String username = (String)event.getProperty("username");
		boolean verified = (Boolean)event.getProperty("verified");
		
		System.out.println("Intercepted event 'user/login' : "+username+" ("+verified+")");
	}
}