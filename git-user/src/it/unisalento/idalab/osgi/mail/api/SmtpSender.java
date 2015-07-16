package it.unisalento.idalab.osgi.mail.api;

import java.util.TreeMap;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public interface SmtpSender {
	void send(TreeMap<String, Object> pars) throws AddressException, MessagingException;
}
