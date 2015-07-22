package it.unisalento.idalab.osgi.content.api;

import java.util.Map;

public interface ContentService {
	Map<String, ?> getContent(String name, String lang, String type);
	Map<String, ?> setContent(String name, String lang, String type, String content);
}
