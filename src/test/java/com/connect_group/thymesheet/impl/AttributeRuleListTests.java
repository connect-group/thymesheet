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

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;

import com.connect_group.thymesheet.css.selectors.NodeSelectorException;

public class AttributeRuleListTests {
	@Test
	public void handleRule() throws NodeSelectorException {
		//ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		AttributeRuleList list = new AttributeRuleList();
		Document document = ThymesheetPreprocessorTests.createDocWithLinks();
		LinkedHashMap<String,String> properties = new LinkedHashMap<String,String>();
		properties.put("data-th-text", "'tada'");
		properties.put("data-th-utext", "'w00t'");
		list.handleRule(document, "#someid", properties);
		
		
		List<Element> elements = document.getElementChildren();
		Element html = elements.get(0);
		Element body = html.getElementChildren().get(1);
		Element para = body.getElementChildren().get(2);
		
		assertEquals("'tada'", para.getAttributeValue("data-th-text"));
		assertEquals("'w00t'", para.getAttributeValue("data-th-utext"));
		
	}
	
}
