package com.connect_group.thymesheet;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Spring cannot initialise a bean with ServletContext at startup.
 * The Spring interface ServletContextAware requires the Spring libraries as a dependancy.
 * 
 * To be able to load files (such as stylesheets/thymesheets) from /webapp requires
 * access to ServletContext.
 * 
 * This interface provides a way to access the ServletContext by configuring the
 * applicationContext.xml
 * 
 * @author adam
 *
 */
public interface ServletContextURLFactory {
	URL getURL(String filePath) throws MalformedURLException;
}
