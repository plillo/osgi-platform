package it.unisalento.idalab.osgi.user.api;



public class UserPersistenceResponse {
	String idUser;
	boolean check;
	boolean usernameFound;
	boolean emailFound;
	boolean mobileFound;

	public boolean isCheck() {
		return check;
	}
	public void setCheck(boolean check) {
		this.check = check;
	}
	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public boolean isUsernameFound() {
		return usernameFound;
	}
	public void setUsernameFound(boolean usernameFound) {
		this.usernameFound = usernameFound;
	}
	public boolean isEmailFound() {
		return emailFound;
	}
	public void setEmailFound(boolean emailFound) {
		this.emailFound = emailFound;
	}
	public boolean isMobileFound() {
		return mobileFound;
	}
	public void setMobileFound(boolean mobileFound) {
		this.mobileFound = mobileFound;
	}

}
