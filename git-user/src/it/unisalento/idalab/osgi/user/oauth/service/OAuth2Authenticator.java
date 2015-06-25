package it.unisalento.idalab.osgi.user.oauth.service;

public enum OAuth2Authenticator {
	GOOGLE("google"),
	FACEBOOK("facebook"),
	TWITTER("twitter"),
	LINKEDIN("linkedin"),
	FLICKR("flickr");

    private final String name;       

    private OAuth2Authenticator(String s) {
        name = s;
    }

    public boolean equals(String otherName){
        return (otherName == null)? false:name.equalsIgnoreCase(otherName);
    }

    public String toString(){
        return name;
    }

	public static OAuth2Authenticator fromString(String name) {
		if (name != null) {
			for (OAuth2Authenticator auth : OAuth2Authenticator.values()) {
				if (name.equalsIgnoreCase(auth.name)) {
					return auth;
				}
			}
		}

		return null;
	}
}