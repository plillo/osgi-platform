package it.hash.osgi.utils;

import java.util.Map;

public class MapTools {
	static boolean containsKeyAndNotNullValue(Map<String, Object> map, String key, String value){
		if(!map.containsKey(key))
			return false;

		return map.get(key) != null;
	}


}