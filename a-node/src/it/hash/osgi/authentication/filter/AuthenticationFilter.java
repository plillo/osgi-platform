package it.hash.osgi.authentication.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements Filter {
   
    public void init(FilterConfig config)
        throws ServletException
    {
        doLog("Init with config [" + config.getClass() + "]");
    }

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
		if(req instanceof HttpServletRequest) {
        	HttpServletRequest httpServletRequest = (HttpServletRequest)req;
        	
            String origin = httpServletRequest.getHeader("Origin");
            String headers = httpServletRequest.getHeader("Access-Control-Request-Headers");
            String methods = httpServletRequest.getHeader("Access-Control-Request-Method");

            // Handling of browser's HTTP preflight request
            if("OPTIONS".equals(httpServletRequest.getMethod())){
                if (origin!=null)
                    ((HttpServletResponse) res).addHeader("Access-Control-Allow-Origin", origin);
                if (headers!=null)
                    ((HttpServletResponse) res).addHeader("Access-Control-Allow-Headers", headers);
                if (methods!=null)
                    ((HttpServletResponse) res).addHeader("Access-Control-Allow-Methods", methods);
                if (methods!=null)
                    ((HttpServletResponse) res).addHeader("Access-Control-Allow-Credentials", "true");
            }
        }
        
		// CHAIN
        chain.doFilter(req, res);
    }

	@SuppressWarnings({ "rawtypes", "unused" })
	private void logRequest(HttpServletRequest httpServletRequest) {
		// LOG method
		doLog("Method: " + httpServletRequest.getMethod());
		
		// LOG headers
		for(Enumeration e = httpServletRequest.getHeaderNames();e.hasMoreElements();){
		    String header = (String)e.nextElement();
			doLog("Header: " + header + "=" + httpServletRequest.getHeader(header));
		}
		
		// LOG properties
		for(Enumeration e = httpServletRequest.getParameterNames();e.hasMoreElements();){
		    String property = (String)e.nextElement();
			doLog("Property: " + property + "=" + httpServletRequest.getParameter(property));
		}
	}
	
	@SuppressWarnings("unused")
	private void logResponse(HttpServletResponse httpServletResponse) {

	}

    public void destroy()
    {
        doLog("Destroyed filter");
    }

    private void doLog(String message)
    {
        System.out.println("## [" + this.getClass() + "] " + message);
    }
}
