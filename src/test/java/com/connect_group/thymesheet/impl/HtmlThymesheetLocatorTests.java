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

import java.util.List;

import org.junit.Test;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;

public class HtmlThymesheetLocatorTests {
	
	@Test
	public void getThymesheetLinks_WithNoValidLinks() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		
		Document doc = new Document("docName");
		Element html = new Element("html");
		doc.addChild(html);
		Element head = new Element("head");
		html.addChild(head);
		Element body = new Element("body");
		html.addChild(body);
		head.addChild(new Element("title"));
		head.addChild(ThymesheetPreprocessorTests.createLink("stylesheet", "style.css", null));
		head.addChild(ThymesheetPreprocessorTests.createLink(null,null,null));
		Element artificial = new Element("block");
		head.addChild(artificial);
		artificial.addChild(ThymesheetPreprocessorTests.createLink("icon", "style2.ico", null));
		
		List<String> filePaths = locator.getThymesheetPaths(doc);
		
		assertEquals(0, filePaths.size());	
	}
	
	@Test
	public void getThymesheetFilePaths_WithValidLinks() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		Document doc = ThymesheetPreprocessorTests.createDocWithLinksInHead();
		List<String> filePaths = locator.getThymesheetPaths(doc);
		
		assertEquals(2, filePaths.size());
		assertEquals("style.ts", filePaths.get(0));
		assertEquals("style2.ts", filePaths.get(1));

	}
	
	@Test
	public void getThymesheetLinks_WithValidLinksOutsideHead() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		
		Document doc = new Document("docName");
		Element html = new Element("html");
		doc.addChild(html);
		Element head = new Element("head");
		html.addChild(head);
		Element body = new Element("body");
		html.addChild(body);
		head.addChild(new Element("title"));
		body.addChild(ThymesheetPreprocessorTests.createLink("stylesheet", "style.css", null));
		body.addChild(ThymesheetPreprocessorTests.createLink(null,null,null));
		Element artificial = new Element("block");
		head.addChild(artificial);
		artificial.addChild(ThymesheetPreprocessorTests.createLink("icon", "style2.ico", null));
		
		List<String> filePaths = locator.getThymesheetPaths(doc);
		
		assertEquals(0, filePaths.size());	
	}

}
