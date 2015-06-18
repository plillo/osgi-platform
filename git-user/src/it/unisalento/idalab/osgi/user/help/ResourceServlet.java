package it.unisalento.idalab.osgi.user.help;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.osgi.framework.Bundle;

public class ResourceServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3769686606124648377L;
	private final Bundle bundle;
	
	public ResourceServlet(Bundle bundle) {
		this.bundle = bundle;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
	
		String reqPath = req.getPathInfo();
			
		//Object resourcesPath = bundle.getHeaders().get("X-HelpResource");
		String b_resource = /*resourcesPath*/ "/help" + (reqPath==null ?"/index.html":reqPath);
		System.out.println("Help Resource: " + b_resource);
		URL resource = bundle.getResource(b_resource);
		if(resource!=null){
			try(InputStream is = resource.openStream()) {
				IOUtils.copy(is, resp.getOutputStream());
			}
		}
		else {
			String text = "<html><h1>Missing resource: "+b_resource+"</h1></html>";
			try(InputStream is = new ByteArrayInputStream(text.getBytes())) {
				IOUtils.copy(is, resp.getOutputStream());
			}
		}
		

	}
}
