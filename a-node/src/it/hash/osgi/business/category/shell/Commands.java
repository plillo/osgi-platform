package it.hash.osgi.business.category.shell;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.service.CategoryService;

public class Commands {
	private volatile CategoryService _category;
	
	public void addCategory(String name, String code, String description, String longDescription) {
		Category category = new Category();
		_category.createCategory(category);
	}
}
