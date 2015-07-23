package it.unisalento.idalab.osgi.content.commands;

import it.unisalento.idalab.osgi.content.api.ContentService;

import java.util.Map;

public class Commands {
	private volatile ContentService _contentService;

	public void create(String name, String lang, String type, String content) {
		
		Map<String, ?> ret = _contentService.setContent(name, lang, type, content);
		
		int status = (Integer) ret.get("returnCode");
		System.out.println("called shell command 'createContent': "+content+" - return-code: "+status);
	}

}