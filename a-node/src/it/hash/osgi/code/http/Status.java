package it.hash.osgi.code.http;

import java.util.Locale;
import java.util.ResourceBundle;

public enum Status {
	OK(200, "ok"), 
	NOT_FOUND(404, "notFound"),
	INTERNAL_SERVER_ERROR(500, "internalServerError"),
	CREATED(201, "notFound"),
	NOT_MODIFIED(304, "notModified"),
	BAD_REQUEST(400, "badRequest"),
	UNAUTHORIZED(401, "unauthorized"),
	FORBIDDEN(403, "forbidden");

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
		ResourceBundle labels = ResourceBundle.getBundle("codes");
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

