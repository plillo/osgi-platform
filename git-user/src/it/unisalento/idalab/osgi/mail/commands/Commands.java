package it.unisalento.idalab.osgi.mail.commands;

import java.util.TreeMap;

import javax.mail.MessagingException;

import it.unisalento.idalab.osgi.mail.api.SmtpSender;

public class Commands {
	private volatile SmtpSender _mailService;

	public void send(String recipient, String subject, String body) {
		System.out.println("sending to: "+recipient);
		
		TreeMap<String, Object> pars = new TreeMap<String, Object>();
		pars.put("from", "idalab@unisalento.it");
		pars.put("recipient", recipient);
		pars.put("subject", subject);
		pars.put("messageText", body);
		
		try {
			_mailService.send(pars);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}