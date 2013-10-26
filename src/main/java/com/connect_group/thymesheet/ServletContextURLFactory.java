/*
 * =============================================================================
 *
 *   Copyright (c) 2013, Connect Group (http://www.connect-group.com)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
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
