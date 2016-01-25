package it.hash.osgi.user.service;

import java.util.Locale;
import java.util.ResourceBundle;

public enum Status {
	// INFORMATIVE
	LOGGED(1001, "logged"), 
	FOUND(1002, "found"),
	FOUND_MANY(1003, "foundMany"),
	NOT_FOUND(1004, "notFound"),
	EXISTING_NOT_CREATED(1201, "existingNotCreated"),
	EXISTING_MANY_NOT_CREATED(1202, "existingManyNotCreated"),
	
	// SUCCESS
	CREATED(2001, "created"),
	
	// CLIENT ERROR
	
	// SERVER ERROR
	ERROR_HASHING_PASSWORD(5001, "errorHashingPassword"),
	ERROR_GENERATING_UUID(5002, "errorGeneratingUuid");

	private final int code;
	private final String messageKey;

	Status(int code, String messageKey) {
		this.code = code;
		this.messageKey = messageKey;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		ResourceBundle labels = ResourceBundle.getBundle("it.hash.osgi.user.service.codes");
		String message = labels.getString(this.messageKey);
		
		System.out.println(message);
		return message;
	}
	
	public String getMessage(Locale locale) {
		ResourceBundle labels = ResourceBundle.getBundle("codes", locale);
		String message = labels.getString(this.messageKey);
		
		System.out.println(message);
		return message;
	}
}

