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
package com.connect_group.thymesheet.impl;

import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Set;

import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Node;
import org.thymeleaf.templateparser.ITemplateParser;

import com.connect_group.thymesheet.ServletContextURLFactory;
import com.connect_group.thymesheet.ThymesheetLocator;
import com.connect_group.thymesheet.ThymesheetParserPostProcessor;

public class ThymesheetTemplateParser implements ITemplateParser {
	private final ITemplateParser decoratedParser;
	private final ThymesheetPreprocessor preprocessor; //= new ThymesheetPreprocessor();
	
    public ThymesheetTemplateParser(ITemplateParser parser, ServletContextURLFactory urlFactory, ThymesheetLocator thymesheetLocator, Set<ThymesheetParserPostProcessor> postProcessors) {
    	this.decoratedParser = parser;
    	this.preprocessor = new ThymesheetPreprocessor(urlFactory, thymesheetLocator, postProcessors);
    }

	public Document parseTemplate(Configuration configuration,
			String documentName, Reader source) {
		Document doc = decoratedParser.parseTemplate(configuration, documentName, source);
		try {
			preprocessor.preProcess(documentName, doc);
		} catch (IOException e) {
			throw new UnsupportedOperationException(e.getMessage(), e);
		}
		return doc;
	}

	public List<Node> parseFragment(Configuration configuration, String fragment) {
		return decoratedParser.parseFragment(configuration, fragment);
	}



}
