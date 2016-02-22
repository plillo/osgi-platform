package it.hash.osgi.user.persistence.mongo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.amdatu.mongo.MongoDBService;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.osgi.service.log.LogService;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import it.hash.osgi.user.User;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.password.Password;
import it.hash.osgi.user.persistence.api.UserServicePersistence;
import it.hash.osgi.user.service.Status;
import net.vz.mongodb.jackson.DBCursor;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.JacksonDBCollection;

public class UserServicePersistenceImpl implements UserServicePersistence {

	private static final String COLLECTION = "users";
	// Injected services
	private volatile MongoDBService m_mongoDBService;
	@SuppressWarnings("unused")
	private volatile LogService logService;
	private volatile Password _passwordService;

	// Mongo User collection
	private DBCollection userCollection;

	public void start() {
		// Initialize user collection
		userCollection = m_mongoDBService.getDB().getCollection(COLLECTION);
	}

	// CREATE
	// ======
	@Override
	public Map<String, Object> addUser(Map<String, Object> user) {

		User user_obj = new User();
		user_obj.setUsername((String) user.get("username"));
		user_obj.setEmail((String) user.get("email"));
		user_obj.setMobile((String) user.get("mobile"));
		user_obj.setFirstName((String) user.get("firstName"));
		user_obj.setLastName((String) user.get("lastName"));
		// ...

		return addUser(user_obj);
	}

	// CREATE
	// ======
	@Override
	public Map<String, Object> addUser(User user) {
		Map<String, Object> response = new TreeMap<String, Object>();
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);

		// Search for existing user
		Map<String, Object> result = getUser(user);

		// not existing: CREATE
		// ====================
		if ((int) result.get("matched") == 0) {
			String savedId = users.save(user).getSavedId();
			if (savedId != null) {
				User created_user = users.findOneById(savedId);
				if (created_user != null) {
					response.put("user", created_user);
					response.put("created", true);
					response.put("status", Status.CREATED.getCode());
					response.put("message", Status.CREATED.getMessage());
				}
			}
		}
		// If existing user
		else if ((int) result.get("matched") == 1) {
			User existing_user = (User) result.get("user");
			if (existing_user != null) {
				response.put("user", existing_user);
				response.put("keys", result.get("keys"));
				response.put("created", false);
				response.put("status", Status.EXISTING_NOT_CREATED.getCode());
				response.put("message", Status.EXISTING_NOT_CREATED.getMessage());
			}
		}
		// If existing many users
		else {
			response.put("created", false);
			response.put("users", result.get("users"));
			response.put("status", Status.EXISTING_MANY_NOT_CREATED.getCode());
			response.put("message", Status.EXISTING_MANY_NOT_CREATED.getMessage());
		}

		return response;
	}

	// READ methods
	// ============
	@Override
	public Map<String, Object> getUser(User user) {
		Map<String, Object> map = new TreeMap<String, Object>();
		if (user.get_id() != null && !"".equals(user))
			map.put("userId", user.get_id());
		if (user.getUsername() != null && !"".equals(user))
			map.put("username", user.getUsername());
		if (user.getEmail() != null && !"".equals(user))
			map.put("email", user.getEmail());
		if (user.getMobile() != null && !"".equals(user))
			map.put("mobile", user.getMobile());
		if (user.getUuid() != null && !"".equals(user))
			map.put("uuid", user.getUuid());

		return getUser(map);
	}

	@Override
	public Map<String, Object> getConstrainedUser(User user) {
		Map<String, Object> map = new TreeMap<String, Object>();
		if (user.get_id() != null && !"".equals(user))
			map.put("userId", user.get_id());
		if (user.getUsername() != null && !"".equals(user))
			map.put("username", user.getUsername());
		if (user.getEmail() != null && !"".equals(user))
			map.put("email", user.getEmail());
		if (user.getMobile() != null && !"".equals(user))
			map.put("mobile", user.getMobile());

		return getUser(map, true);
	}

	@Override
	public Map<String, Object> getUser(Map<String, Object> user) {
		return getUser(user, false);
	}

	@Override
	public Map<String, Object> getConstrainedUser(Map<String, Object> user) {
		return getUser(user, true);
	}

	private Map<String, Object> getUser(Map<String, Object> user, boolean constrained) {

		Map<String, Object> response = new HashMap<String, Object>();
		User found_user = null;
		DBObject found = null;
		Map<User, TreeSet<String>> matchs = new TreeMap<User, TreeSet<String>>();

		if (constrained) {
			// TODO get User with constraining conditions
		} else {
			if (user.containsKey("userId") && user.get("userId") != null) {
				found = userCollection.findOne(new BasicDBObject("_id", user.get("userId")));
				if (found != null) {
					found_user = userToMap(found.toMap());
					
					TreeSet<String> list = matchs.get(found_user);
					if (list == null)
						list = new TreeSet<String>();

					list.add("userId");
					matchs.put(found_user, list);
				}
			}
			if (user.containsKey("uuid") && user.get("uuid") != null) {
				found = userCollection.findOne(new BasicDBObject("uuid", user.get("uuid")));
				
				if (found != null) {
					found_user = userToMap(found.toMap());

					TreeSet<String> list = matchs.get(found_user);
					if (list == null)
						list = new TreeSet<String>();

					list.add("uuid");
					matchs.put(found_user, list);
				}
			}
			if (user.containsKey("username") && user.get("username") != null) {
				found = userCollection.findOne(new BasicDBObject("username", user.get("username")));
				
				if (found != null) {
					found_user = userToMap(found.toMap());

					TreeSet<String> list = matchs.get(found_user);
					if (list == null)
						list = new TreeSet<String>();

					list.add("username");
					matchs.put(found_user, list);
				}
			}
			if (user.containsKey("email") && user.get("email") != null) {
				found = userCollection.findOne(new BasicDBObject("email", user.get("email")));
				
				if (found != null) {
					found_user = userToMap(found.toMap());

					TreeSet<String> list = matchs.get(found_user);
					if (list == null)
						list = new TreeSet<String>();

					list.add("email");
					matchs.put(found_user, list);
				}
			}
			if (user.containsKey("mobile") && user.get("mobile") != null) {
				found = userCollection.findOne(new BasicDBObject("mobile", user.get("mobile")));
				if (found != null) {
					found_user = userToMap(found.toMap());
					
					TreeSet<String> list = matchs.get(found_user);
					if (list == null)
						list = new TreeSet<String>();

					list.add("mobile");
					matchs.put(found_user, list);
				}
			}
			if (user.containsKey("firstName") && user.get("firstName") != null) {
				found = userCollection.findOne(new BasicDBObject("firstName", user.get("firstName")));
				if (found != null) {
					found_user = userToMap(found.toMap());
					
					TreeSet<String> list = matchs.get(found_user);
					if (list == null)
						list = new TreeSet<String>();

					list.add("firstName");
					matchs.put(found_user, list);
				}
			}
			if (user.containsKey("lastName") && user.get("lastName") != null) {
				found = userCollection.findOne(new BasicDBObject("lastName", user.get("lastName")));
				if (found != null) {
					found_user = userToMap(found.toMap());
					
					TreeSet<String> list = matchs.get(found_user);
					if (list == null)
						list = new TreeSet<String>();

					list.add("lastName");
					matchs.put(found_user, list);
				}
			}

			// Set response: number of matched users
			response.put("matched", matchs.size());

			// Set response details
			switch (matchs.size()) {
			case 0:
				response.put("found", false);
				response.put("status", Status.NOT_FOUND);
				response.put("message", Status.NOT_FOUND.getMessage());
				break;
			case 1:
				User key = (User) matchs.keySet().toArray()[0];
				response.put("found", true);
				response.put("user", key);
				response.put("keys", matchs.get(key));
				response.put("status", Status.FOUND);
				response.put("message", Status.FOUND.getMessage());
				break;
			default:
				response.put("found", true);
				response.put("users", matchs);
				response.put("status", Status.FOUND_MANY);
				response.put("message", Status.FOUND_MANY.getMessage());
			}
		}

		return response;
	}

	private User userToMap(Map mapUser) {
		User user = new User();
		String attribute = null;
		Map<String, Object> others = new TreeMap<String, Object>();
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
									listAtt.add(Attribute.attributeToMap(dbo.toMap()));
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

	@Override
	public List<User> getUsers() {
		JacksonDBCollection<User, Object> users = JacksonDBCollection.wrap(userCollection, User.class);
		DBCursor<User> cursor = users.find();

		List<User> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}

		return list;
	}

	private User getUserByKey(String key, String value) {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put(key, value);
		Map<String, Object> response = getUser(map);
		if (response.containsKey("user"))
			return (User) response.get("user");

		return null;
	}

	@Override
	public User getUserByUuid(String uuid) {
		return getUserByKey("uuid", uuid);
	}

	@Override
	public User getUserByUsername(String username) {
		return getUserByKey("username", username);
	}

	@Override
	public User getUserByEmail(String email) {
		return getUserByKey("email", email);
	}

	@Override
	public User getUserByMobile(String mobile) {
		return getUserByKey("mobile", mobile);
	}

	@Override
	public User getUserById(String userId) {
		return getUserByKey("userId", userId);
	}

	// UPDATE
	// ======

	@Override
	public Map<String, Object> updateUser(Map<String, Object> user) {
		User find_user = null;
		if (user.containsKey("update")) {
			if (user.get("update").equals("attribute")) {
				find_user = getUserByUuid((String) user.get("userUuid"));

				find_user.removeExtra("attributes");
				find_user.setExtra("attributes", user.get("attributes"));
				if (user.containsKey("businessUuid")) {
					List<String> users = (List<String>) find_user.getExtra("users");
					if (!users.contains(user.get("businessUuid"))) {
						users.add((String) user.get("businessUuid"));
						find_user.removeExtra("users");
						find_user.setExtra("users", users);
					}
				}
			}
			updateUser(find_user);
		}

		return null;
	}

	@Override
	public Map<String, Object> updateUser(User user) {
		Map<String, Object> response = new TreeMap<String, Object>();
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		User found_user = null;
		// Match user
		Map<String, Object> result = getUser(user);

		if (result.containsKey("user")) {
			if (user.getPassword() != null && !"".equals(user.getPassword()))
				try {
					user.setPassword(_passwordService.getSaltedHash(user.getPassword()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			String userId = ((User) result.get("user")).get_id();
			users.updateById(userId, user);
			found_user = users.findOne(new BasicDBObject("_id", userId));
			response.put("user", found_user);
			response.put("updated", true);
			response.put("keys", result.get("keys"));
			response.put("returnCode", 100);
		} else if (result.containsKey("users")) {
			response.put("updated", false);
			response.put("returnCode", 110);
			response.put("users", result.get("users"));
		} else if ((int) result.get("matched") == 0) {
			response.put("updated", false);
			response.put("errorCode", 115);
		}
		// TODO: Gestire bene la composizione della risposta (deve essere più
		// informativa possibile)

		return response;
	}

	// DELETE
	// ======
	@Override
	public Map<String, Object> deleteUser(User user) {
		Map<String, Object> response = new TreeMap<String, Object>();
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String, Object> result = getUser(user);

		if (result.containsKey("user")) {
			response.put("user", user);
			users.removeById(((User) result.get("user")).get_id());
			response.put("removed", true);
			response.put("returnCode", 100);
		} else if (result.containsKey("users")) {
			response.put("removed", false);
			response.put("returnCode", 110);
			response.put("users", result.get("users"));
		} else if ((int) result.get("matched") == 0) {
			response.put("removed", false);
			response.put("returnCode", 115);
		}
		// TODO: Gestire bene la composizione della risposta (deve essere pi�
		// informativa possibile)

		return response;
	}

	// LOGIN
	// =====
	@Override
	public Map<String, Object> login(Map<String, Object> user) {
		Map<String, Object> response = new HashMap<String, Object>();
		String password = (String) user.get("password");

		// Return ERROR if missing password
		if (password == null || "".equals(password)) {
			response.put("returnCode", 101); // 101: missing password
			return response;
		}

		// Search and get user
		Map<String, Object> result = getUser(user);

		// Get reference to user (if found)
		User userFound = (User) result.get("user");
		if (userFound != null) {
			try {
				if (_passwordService.check(password, userFound.getPassword())) {
					response.put("user", userFound);
					response.put("returnCode", 100);
					response.put("logged", true);
				} else
					response.put("returnCode", 102); // 102: mismatched password

			} catch (Exception e) {
				response.put("returnCode", 103); // 103: exception
				response.put("logged", false);
			}
		} else if (result.containsKey("users")) {
			response.put("returnCode", 110);
			response.put("logged", false);
			response.put("users", result.get("users"));
		} else if ((int) result.get("matched") == 0) {
			response.put("logged", false);
			response.put("returnCode", 115);
		}

		return response;
	}

	// LOGIN BY OAuth2
	// =====
	@Override
	public Map<String, Object> loginByOAuth2(Map<String, Object> user) {
		Map<String, Object> response = new TreeMap<String, Object>();

		Map<String, Object> result = getUser(user);
		if ((int) result.get("matched") == 1) {
			Map<String, Object> updateResult = updateUserbyOAuth2((User) result.get("user"), user);
			response.put("created", false);
			response.put("user", result.get("user"));
			response.put("returnCode", 106);
			response.put("logged", true);
			if (updateResult.containsKey("updatedFields"))
				response.put("updatedFields", updateResult.get("updatedFields"));
			if (updateResult.containsKey("addedFields"))
				response.put("addedFields", updateResult.get("addedFields"));

		} else if ((int) result.get("matched") == 0) {
			// il metodo createUser produce le chiavi "created" (boolean) e
			// "user" (User)
			response = addUser(user);
			response.put("returnCode", 107);
			response.put("logged", true);
		} else if (result.containsKey("users")) {
			response.put("created", false);
			response.put("users", result.get("users"));
			response.put("returnCode", 110);
			response.put("logged", false);
		}

		return response;
	}

	private Map<String, Object> updateUserbyOAuth2(User user, Map<String, Object> loggingData) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);

		Map<String, Object> response = new HashMap<String, Object>();
		List<String> addedFields = new ArrayList<String>();

		if (loggingData.containsKey("username") && user.getUsername() == null) {
			user.setUsername((String) loggingData.get("username"));
			addedFields.add("username");
		}
		if (loggingData.containsKey("email") && user.getEmail() == null) {
			user.setEmail((String) loggingData.get("email"));
			addedFields.add("email");
		}
		if (loggingData.containsKey("mobile") && user.getMobile() == null) {
			user.setMobile((String) loggingData.get("mobile"));
			addedFields.add("mobile");
		}
		if (loggingData.containsKey("firstName") && user.getFirstName() == null) {
			user.setFirstName((String) loggingData.get("firstName"));
			addedFields.add("firstName");
		}
		if (loggingData.containsKey("lastName") && user.getLastName() == null) {
			user.setLastName((String) loggingData.get("lastName"));
			addedFields.add("lastName");
		}

		if (addedFields.size() > 0)
			response.put("addedFields", addedFields);

		// ...
		// se decidiamo di aggiornare alcuni campi usermemo la chiave updated
		users.updateById(user.get_id(), user);

		return response;
	}

	// VALIDATE methods
	// ================
	@Override
	public Map<String, Object> validateUsername(String userId, String username) {
		// TODO ...
		// il metodo verifica la validità in termini di unicità dello username;
		// se userId NON è null lo username da validare è accettabile ANCHE se
		// coincide con l'attuale username dell'utente userId.
		// Se invece userId è null allora username è accettabile solo se NON già
		// associato a un utente.
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String, Object> map = new TreeMap<String, Object>();
		if (userId == null) {
			User user = users.findOne(new BasicDBObject("username", username));
			if (user != null) {
				if (user.getUsername() != null) {
					map.put("errorCode", 0);
					map.put("isValid", false);
				}
			} else {
				map.put("isValid", true);
				map.put("errorCode", 0);
			}
		} else {
			User userFound = users.findOneById(userId);
			if (userFound.getUsername().matches(username)) {
				map.put("isValid", true);
				map.put("errorCode", 0);
			} else {
				map.put("isValid", false);
				map.put("errorCode", 0);
			}
		}
		// map.put("isValid", true /*false*/);
		// map.put("errorCode", 0); // da settare con valori >0 in presenza di
		// situazioni di errore (problemi di accesso DB,...)
		return map;
	}

	@Override
	public Map<String, Object> validateEMail(String userId, String email) {

		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String, Object> map = new TreeMap<String, Object>();
		if (userId == null) {
			User user = users.findOne(new BasicDBObject("email", email));
			if (user != null) {
				if (user.getEmail() != null) {
					map.put("errorCode", 0);
					map.put("isValid", false);
				}
			} else {
				map.put("isValid", true);
				map.put("errorCode", 0);
			}
		} else {
			User userFound = users.findOneById(userId);
			if (userFound.getEmail().matches(email)) {
				map.put("isValid", true);
				map.put("errorCode", 0);
			} else {
				map.put("isValid", false);
				map.put("errorCode", 0);
			}
		}

		return map;

	}

	@Override
	public Map<String, Object> validateMobile(String userId, String mobile) {
		JacksonDBCollection<User, String> users = JacksonDBCollection.wrap(userCollection, User.class, String.class);
		Map<String, Object> map = new TreeMap<String, Object>();
		if (userId == null) {
			User user = users.findOne(new BasicDBObject("mobile", mobile));
			if (user != null) {
				if (user.getMobile() != null) {
					map.put("errorCode", 0);
					map.put("isValid", false);
				}
			} else {
				map.put("isValid", true);
				map.put("errorCode", 0);
			}
		} else {
			User userFound = users.findOneById(userId);
			if (userFound.getMobile().matches(mobile)) {
				map.put("isValid", true);
				map.put("errorCode", 0);
			} else {
				map.put("isValid", false);
				map.put("errorCode", 0);
			}
		}

		return map;

	}

	@Override
	public List<User> getUserDetails(User user) {

		JacksonDBCollection<User, Object> users = JacksonDBCollection.wrap(userCollection, User.class);
		DBCursor<User> cursor = users.find(user);

		List<User> list = new ArrayList<>();
		while (cursor.hasNext()) {
			list.add(cursor.next());
		}

		return list;
	}

	@Override
	public String getImplementation() {
		return "MONGO";
	}

	@Override
	public List<Attribute> getAttribute(String userUuid) {

		User found_user = getUserByUuid(userUuid);
		return (List<Attribute>) found_user.getExtra("attributes");
	}

}
