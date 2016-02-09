package it.hash.osgi.business.category.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.persistence.api.CategoryPersistence;
import it.hash.osgi.resource.uuid.api.UUIDService;
import it.hash.osgi.user.attribute.Attribute;
import it.hash.osgi.user.attribute.service.AttributeService;

public class CategoryServiceImpl implements CategoryService {
	// Injected services
	private volatile CategoryPersistence _persistenceSrv;
	private volatile UUIDService _uuidSrv;
	private volatile AttributeService _attributeSrv;

	@Override
	public Category getCategory(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> createCategory(Category category) {
		category.setUUID(_uuidSrv.createUUID("app/profiler/business-category"));

		return _persistenceSrv.createCategory(category);
	}

	@Override
	public Map<String, Object> updateCategory(Category category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteCategory(String uuid) {
		return _persistenceSrv.deleteCategory(uuid);
	}

	@Override
	public Map<String, Object> createCategory(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> updateCategory(Map<String, Object> pars) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> retrieveCategories(String criterion) {
		return _persistenceSrv.retrieveCategories();
	}

	// ATTRIBUTES
	// ==========
	@Override
	public List<Attribute> getAttributes(String ctgUuid) {
		return _attributeSrv.getAttributesByCategories(new String[] { ctgUuid });
	}

	@Override
	public Map<String, Object> createAttribute(String ctgUuid, Attribute attribute) {
		if (attribute.getContext() == null)
			attribute.setContext(new ArrayList<String>());

		List<String> context = attribute.getContext();
		if (!context.contains("busctg:" + ctgUuid))
			context.add("busctg:" + ctgUuid);

		return _attributeSrv.createAttribute(attribute);
	}

	@Override
	public Map<String, Object> updateAttribute(String ctgUuid, Attribute attr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> deleteAttribute(String ctgUuid, String attrUuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createCollectionByCsv(String url, String fileName) {
		URL path = null;
		fileName=url+"\\"+fileName;
		boolean response = true;
		try {
			path = new URL(fileName);
			URLConnection urlConn = path.openConnection();

			String outputString = null;
			
			String line;

			outputString = "";
			BufferedReader readerFile = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

			/*
			 * BufferedReader readerFile; try { readerFile = new
			 * BufferedReader(new FileReader(fileName));
			 */
			Map<String, Object> createC;

			String[] doc;

			line = readerFile.readLine();

			while (line != null) {

				doc = line.split(";");
				Category cat = new Category();
				cat.setCode(doc[0]);
				cat.setName(doc[1]);
				createC = createCategory(cat);
				// basta che una non vada a buon fine ....si dovrebbe
				// considerare tutta la transazione fallita
				if (createC.get("created").equals(false))
					response = false;
				line = readerFile.readLine();

			}
			readerFile.close();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		return response;
	}

}
