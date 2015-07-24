package it.unisalento.idalab.osgi.content.commands;

import it.unisalento.idalab.osgi.content.api.ContentService;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class Commands {
	private volatile ContentService _contentService;

	public void create(String name, String lang, String type, String content) {
		/*
		if("image/jpg".equals(type)) {
			byte[] bcontent = Base64.encodeBase64(content.getBytes(Charset.forName("UTF-8")));
		}
		*/
		
		Map<String, ?> ret = _contentService.setContent(name, lang, type,  Base64.encodeBase64(content.getBytes(Charset.forName("UTF-8"))));
		
		int status = (Integer) ret.get("returnCode");
		System.out.println("called shell command 'createContent': "+content+" - return-code: "+status);
	}

}
