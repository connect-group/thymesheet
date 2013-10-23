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
    private final ThymesheetLocator thymesheetLocator;

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
