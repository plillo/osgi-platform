package it.unisalento.idalab.osgi.user.oauth2.rest;

public class Oauth2AuthenticatorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6769496690374296833L;

	@Override
	public String toString() {
		return "Error connecting to authenticator";
	}
}
