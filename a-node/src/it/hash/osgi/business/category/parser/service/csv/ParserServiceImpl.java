package it.hash.osgi.business.category.parser.service.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import it.hash.osgi.business.category.Category;
import it.hash.osgi.business.category.service.CategoryService;
import it.hash.osgi.parser.service.ParserService;

public class ParserServiceImpl implements ParserService {
	private volatile CategoryService _ctgService;

	@Override
	public String getAppCode() {
		// TODO Auto-generated method stub
		return "ctg-prs-csv";
	}

	@Override
	public boolean createCollectionBy(String url, String fileName) {
			URL path = null;
			fileName=url+"\\"+fileName;
			boolean response = true;
			try {
				path = new URL(fileName);
				URLConnection urlConn = path.openConnection();

				String outputString = null;
				
				String line;

				outputString = "";
				BufferedReader readerFile = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF8"));

				Map<String, Object> createC;

				String[] doc;

				line = readerFile.readLine();

				while (line != null) {

					doc = line.split(";");
					Category cat = new Category();
					cat.setCode(doc[0]);
					cat.setName(doc[1]);
					createC =_ctgService.createCategory(cat);
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
		// TODO Auto-generated method stub
		
	}

}
