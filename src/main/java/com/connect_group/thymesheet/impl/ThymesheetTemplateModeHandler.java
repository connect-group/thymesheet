package com.connect_group.thymesheet.impl;

import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templateparser.ITemplateParser;
import org.thymeleaf.templatewriter.ITemplateWriter;

import com.connect_group.thymesheet.ServletContextURLFactory;

public abstract class ThymesheetTemplateModeHandler implements ITemplateModeHandler {
	
    private final String templateModeName;
    private ThymesheetTemplateParser parser;
    private final ITemplateParser decoratedParser;
    private final ITemplateWriter templateWriter;

    private ServletContextURLFactory urlFactory = null;
    
    protected ThymesheetTemplateModeHandler(String templateModeName, ITemplateParser templateParser, ITemplateWriter templateWriter) {
    	this.templateModeName = templateModeName;
    	this.decoratedParser = templateParser;
    	this.templateWriter = templateWriter;
    }
    
    public String getTemplateModeName() {
        return this.templateModeName;
    }

    public ITemplateParser getTemplateParser() {
    	if(this.parser==null) {
    		this.parser = new ThymesheetTemplateParser(decoratedParser, urlFactory);
    	}
        return this.parser;
    }

    public ITemplateWriter getTemplateWriter() {
        return this.templateWriter;
    }
    
	protected static int getPoolSize() {
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		int poolSize = 
                Math.min(
                        (availableProcessors <= 2? availableProcessors : availableProcessors - 1),
                        24);
		
		return poolSize;
	}

	public ServletContextURLFactory getUrlFactory() {
		return urlFactory;
	}

	public void setUrlFactory(ServletContextURLFactory urlContainer) {
		this.urlFactory = urlContainer;
	}
}
