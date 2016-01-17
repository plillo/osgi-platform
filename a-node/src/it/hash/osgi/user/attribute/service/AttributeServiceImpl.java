package it.hash.osgi.user.attribute.service;

import java.util.List;

import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.persistence.api.AttributeServicePersistence;

public class AttributeServiceImpl implements AttributeService{
	private volatile AttributeServicePersistence _persistence;

	@Override
	public List<Attribute> getAttributesByCategories(String[] categories) {
		// TODO Auto-generated method stub
		return _persistence.getAttributesByCategories(categories);
	}

}
