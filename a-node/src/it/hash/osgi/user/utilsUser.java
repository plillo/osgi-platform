package it.hash.osgi.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.persistence.mongo.AttributeServicePersistenceImpl;

public class utilsUser {

	public static User toMap(Map mapUser) {
		User user = new User();
		String attribute = null;

		Set entry = mapUser.keySet();
		for (Object elem : entry) {
			attribute = (String) elem;
			switch (attribute) {
			case "_id":
				user.set_id(mapUser.get(attribute).toString());
				break;

			case "uuid":
				user.setUuid((String) mapUser.get(attribute));
				break;

			case "username":
				user.setUsername((String) mapUser.get(attribute));
				break;
			case "password":
				user.setPassword((String) mapUser.get(attribute));
				break;

			case "salted_hash_password":
				user.setSalted_hash_password((String) mapUser.get(attribute));
				break;

			case "firstName":
				user.setFirstName((String) mapUser.get(attribute));
				break;
			case "lastName":
				user.setLastName((String) mapUser.get(attribute));
				break;

			case "password_mdate":
				user.setPassword_mdate((String) mapUser.get(attribute));
				break;

			case "email":
				user.setEmail((String) mapUser.get(attribute));
				break;
			case "mobile":
				user.setMobile((String) mapUser.get(attribute));
				break;
			case "attributes":
				user.setAttributes((List<AttributeValue>) mapUser.get(attribute));
				break;
			
			case "extra":
				Map<String, Object> map = new HashMap<String, Object>();
				if (mapUser.get(elem) instanceof Map) {
					Map mapExtra = (Map) mapUser.get(elem);
					Set<String> key = mapExtra.keySet();
					for (String value : key) {
						if (value.equals("Attributes")) {
							List<Attribute> listAtt = new ArrayList<Attribute>();
							if (mapExtra.get(value) instanceof BasicDBList) {
								BasicDBList list = (BasicDBList) mapExtra.get(value);
								Set<String> keyList = list.keySet();
								for (String valueList : keyList) {
									BasicDBObject dbo = (BasicDBObject) list.get(valueList);
									System.out.println(dbo.toString());
									listAtt.add(AttributeServicePersistenceImpl.mapToAttribute(dbo.toMap()));
									System.out.println();

								}
							}
							map.put("attributes", listAtt);

						} else
							map.put(value, mapExtra.get(value));
					}
				
				}
				user.setExtra(map);
				break;
			case "published":
				user.setPublished((String) mapUser.get("elem"));
				break;
			case "last_login_date":
				user.setLast_login_date((String) mapUser.get("elem"));
				break;
			case "last_login_ip":
				user.setLast_login_ip((String) mapUser.get("elem"));
				break;

			case "trusted_email":
				user.setTrusted_email((String) mapUser.get("elem"));
				break;
			case "trusted_mobile":
				user.setTrusted_mobile((String) mapUser.get("elem"));
				break;
			case "cauthor":
				user.setCauthor((String) mapUser.get("elem"));
				break;
			case "cdate":
				user.setCdate((String) mapUser.get("elem"));
				break;
			case "mauthor":
				user.setMauthor((String) mapUser.get("elem"));
				break;
			case "mdate":
				user.setMdate((String) mapUser.get("elem"));
				break;
			case "lauthor":
				user.setLauthor((String) mapUser.get("elem"));
				break;
			case "ldate":
				user.setLdate((String) mapUser.get("elem"));
				break;
			case "user_data":
				user.setUser_data((String) mapUser.get("elem"));
				break;

			default:
				if (user.getExtra() == null)
					user.setExtra(new HashMap<String, Object>());
				if (!user.getExtra().containsKey(attribute))
					user.setExtra(attribute, mapUser.get(elem));

			}

		}
		return user;
	}

}
