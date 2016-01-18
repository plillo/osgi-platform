package it.hash.osgi.user.attribute.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.util.StringUtils;

import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;

public class AttributeServiceImpl implements AttributeService{
	private volatile AttributeServicePersistence _persistence;
	private volatile UUIDService _uuid;

	@Override
	public List<Attribute> getAttributesByCategories(String[] categories) {
		// TODO Auto-generated method stub
		return _persistence.getAttributesByCategories(categories);
	}

	@Override
	public Map<String, Object> createAttribute(Attribute attribute) {
		String uuid = _uuid.createUUID("app/user/attribute");
		if (!StringUtils.isNullOrEmpty(uuid)) {
			attribute.setUuid(uuid);
			return _persistence.createAttribute(attribute);
		} else {
			Map<String, Object> response = new HashMap<String, Object>();
			response.put("created", false);
			response.put("returnCode", 630);
			return response;
		}
	}

}
