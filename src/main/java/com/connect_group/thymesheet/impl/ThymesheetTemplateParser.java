package com.connect_group.thymesheet.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Node;
import org.thymeleaf.templateparser.ITemplateParser;

import com.connect_group.thymesheet.ServletContextURLFactory;

public class ThymesheetTemplateParser implements ITemplateParser {
	private final ITemplateParser decoratedParser;
	private final ThymesheetPreprocessor preprocessor; //= new ThymesheetPreprocessor();
	
    public ThymesheetTemplateParser(ITemplateParser parser, ServletContextURLFactory urlFactory) {
    	this.decoratedParser = parser;
    	this.preprocessor = new ThymesheetPreprocessor(urlFactory);
    }

	public Document parseTemplate(Configuration configuration,
			String documentName, Reader source) {
		Document doc = decoratedParser.parseTemplate(configuration, documentName, source);
		try {
			preprocessor.preprocess(doc);
		} catch (IOException e) {
			throw new UnsupportedOperationException(e.getMessage(), e);
		}
		return doc;
	}

	public List<Node> parseFragment(Configuration configuration, String fragment) {
		return decoratedParser.parseFragment(configuration, fragment);
	}



}
