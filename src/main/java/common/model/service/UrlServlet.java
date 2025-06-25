package common.model.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Clase que permite en base a una Request HTTP obtener los datos de la URL originaria de la misma
 * @author pmaseda
 *
 */
public class UrlServlet {
	private String scheme ;
	private String serverName ;
	private int serverPort ;
	private String contextPath ;
	private String servletPath ;
	private String pathInfo ;
	private String queryString ;

	
	public UrlServlet(HttpServletRequest req){
		scheme = req.getScheme();             // http
	    serverName = req.getServerName();     // hostname.com
	    serverPort = req.getServerPort();        // 80
	    contextPath = req.getContextPath();   // /mywebapp
	    servletPath = req.getServletPath();   // /servlet/MyServlet
	    pathInfo = req.getPathInfo();         // /a/b;c=123
	    queryString = req.getQueryString();          // d=789
	}
	
	/**
	 * Retorna URL de origen completa con el formato 
	 * http://hostname.com:80/mywebapp/servlet/MyServlet/a/b;c=123?d=789
	 */
	public String getFullUrl() {	    

	    // Reconstruct original requesting URL
	    String url = scheme+"://"+serverName+":"+serverPort+contextPath+servletPath;
	    if (pathInfo != null) {
	        url += pathInfo;
	    }
	    if (queryString != null) {
	        url += "?"+queryString;
	    }
	    return url;
	}

	/**
	 * Retorna URL de origen parcial con el formato , solo con la info "desde" el servlet con el formato 
	 * /servlet/MyServlet/a/b;c=123?d=789
	 */
	public String getServletUrl() {	    

	    // Reconstruct original requesting URL
	    String url = servletPath;
	    if (pathInfo != null) {
	        url += pathInfo;
	    }
	    if (queryString != null) {
	        url += "?"+queryString;
	    }
	    return url;
	}
	
	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		this.servletPath = servletPath;
	}

	public String getPathInfo() {
		return pathInfo;
	}

	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	

}


