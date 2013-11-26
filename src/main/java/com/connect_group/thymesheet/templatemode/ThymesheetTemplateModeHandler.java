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
package com.connect_group.thymesheet.templatemode;

import org.thymeleaf.templateparser.ITemplateParser;
import org.thymeleaf.templatewriter.ITemplateWriter;

import com.connect_group.thymesheet.ServletContextURLFactory;
import com.connect_group.thymesheet.ThymesheetLocator;
import com.connect_group.thymesheet.impl.ThymesheetTemplateParser;

public class ThymesheetTemplateModeHandler implements IThymesheetTemplateModeHandler {
	
    private final String templateModeName;
    private ThymesheetTemplateParser parser;
    private final ITemplateParser decoratedParser;
    private final ITemplateWriter templateWriter;
    protected final ThymesheetLocator thymesheetLocator;

    private ServletContextURLFactory urlFactory = null;
    
    public ThymesheetTemplateModeHandler(
    		String templateModeName, 
    		ITemplateParser templateParser, 
    		ITemplateWriter templateWriter,
    		ThymesheetLocator thymesheetLocator) {
    	this.templateModeName = templateModeName;
    	this.decoratedParser = templateParser;
    	this.templateWriter = templateWriter;
		this.thymesheetLocator = thymesheetLocator;
    }
    
    public String getTemplateModeName() {
        return this.templateModeName;
    }

    public ITemplateParser getTemplateParser() {
    	if(this.parser==null) {
    		this.parser = new ThymesheetTemplateParser(decoratedParser, urlFactory, thymesheetLocator);
    	}
        return this.parser;
    }

    public ITemplateWriter getTemplateWriter() {
        return this.templateWriter;
    }

	public ServletContextURLFactory getUrlFactory() {
		return urlFactory;
	}

	public void setUrlFactory(ServletContextURLFactory urlContainer) {
		this.urlFactory = urlContainer;
	}
}
