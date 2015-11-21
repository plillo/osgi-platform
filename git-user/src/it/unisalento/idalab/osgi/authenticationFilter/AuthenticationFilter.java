package it.unisalento.idalab.osgi.authenticationFilter;

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
    private final String name;

    public AuthenticationFilter(String name)
    {
        this.name = name;
    }
    
    public void init(FilterConfig config)
        throws ServletException
    {
        doLog("Init with config [" + config + "]");
    }

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException
    {
        doLog("Filter request [" + req + "]");
        
        if(req instanceof HttpServletRequest) {
        	HttpServletRequest httpServletRequest = (HttpServletRequest)req;

        	logRequest(httpServletRequest);
        	
            String origin = httpServletRequest.getHeader("Origin");
            String headers = httpServletRequest.getHeader("Access-Control-Request-Headers");
            String methods = httpServletRequest.getHeader("Access-Control-Request-Method");

            // ECHO without control
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
           
            // AUTHENTICATION
            // ==============
			String authCredentials = httpServletRequest.getHeader("Authorization");
			if(authCredentials!=null){
				AuthenticationService authenticationService = new AuthenticationService();
				boolean authenticationStatus = authenticationService.authenticate(authCredentials);

				if (authenticationStatus) {
					chain.doFilter(req, res);
					return;
				} else {
					if (res instanceof HttpServletResponse) {
						((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					}
					return;
				}
			}

           
        } else {
        } 
        
        chain.doFilter(req, res);

        /*
        if(res instanceof HttpServletResponse) {
        	HttpServletResponse httpServletResponse = (HttpServletResponse)res;
        	httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        	httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        	httpServletResponse.setHeader("Access-Control-Max-Age", "1728000");
        	httpServletResponse.setHeader("Content-Length", "0");
        	httpServletResponse.setHeader("Content-Type", "text/plain");
        } else {
        } 
        */
    }

	@SuppressWarnings("rawtypes")
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
        System.out.println("## [" + this.name + "] " + message);
    }
}
