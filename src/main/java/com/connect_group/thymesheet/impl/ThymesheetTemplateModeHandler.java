package com.connect_group.thymesheet.impl;

import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templateparser.ITemplateParser;
import org.thymeleaf.templatewriter.ITemplateWriter;

public abstract class ThymesheetTemplateModeHandler implements ITemplateModeHandler {
	
    private final String templateModeName;
    private final ThymesheetTemplateParser parser;
    private final ITemplateWriter templateWriter;

    
    protected ThymesheetTemplateModeHandler(String templateModeName, ITemplateParser templateParser, ITemplateWriter templateWriter) {
    	this.templateModeName = templateModeName;
    	this.parser = new ThymesheetTemplateParser(templateParser);
    	this.templateWriter = templateWriter;
    }
    
    public String getTemplateModeName() {
        return this.templateModeName;
    }

    public ITemplateParser getTemplateParser() {
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
}
