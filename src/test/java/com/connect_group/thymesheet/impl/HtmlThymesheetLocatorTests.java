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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;

public class HtmlThymesheetLocatorTests {
	@Test
	public void isThymesheetLink_WithNull_ShouldReturnFalse() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		assertFalse(locator.isThymesheetLink(null));
	}

	@Test
	public void isThymesheetLink_WithNonLinkElement_ShouldReturnFalse() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		Element el = new Element("title");
		assertFalse(locator.isThymesheetLink(el));
	}

	@Test
	public void isThymesheetLink_WithNonThymesheetLinkElement_ShouldReturnFalse() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		Element el = new Element("link");
		el.setAttribute("rel", "stylesheet");
		assertFalse(locator.isThymesheetLink(el));
	}

	@Test
	public void isThymesheetLink_WithThymesheetLinkElement_ShouldReturnTrue() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		Element el = new Element("thymesheet");
		el.setAttribute("rel", "thymesheet");
		assertFalse(locator.isThymesheetLink(el));
	}



	@Test
	public void getHead_WithHead_ShouldReturnHeadElement() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		Document doc = new Document("docName");
		Element html = new Element("html");
		doc.addChild(html);
		Element head = new Element("head");
		html.addChild(head);
		Element body = new Element("body");
		html.addChild(body);

		Element hd = locator.getHead(doc);

		assertNotNull(hd);
		assertEquals(hd.getNormalizedName(), "head");
	}

	@Test
	public void getHead_WithoutHead_ShouldReturnNull() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		Document doc = new Document("docName");
		Element html = new Element("html");
		doc.addChild(html);
		Element body = new Element("body");
		html.addChild(body);

		Element hd = locator.getHead(doc);

		assertNull(hd);
	}

	@Test
	public void getThymesheetLinks_WithValidLinks() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();

		Document doc = ThymesheetPreprocessorTests.createDocWithLinks();

		List<Element> links = new ArrayList<Element>(2);
		locator.getThymesheetLinkElementsFromParent(locator.getHead(doc), links);

		assertEquals(2, links.size());
		assertEquals("style.ts", links.get(0).getAttributeValue("href"));
		assertEquals("style2.ts", links.get(1).getAttributeValue("href"));

	}

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

		List<Element> links = new ArrayList<Element>(2);
		locator.getThymesheetLinkElementsFromParent(head, links);

		assertEquals(0, links.size());
	}

	@Test
	public void getThymesheetFilePaths_WithNoValidLinks() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		Document doc = ThymesheetPreprocessorTests.createDocWithLinks();
		List<String> filePaths = locator.getThymesheetPaths(doc);

		assertEquals(2, filePaths.size());
		assertEquals("style.ts", filePaths.get(0));
		assertEquals("style2.ts", filePaths.get(1));

	}

	@Test
	public void removeThymesheetLinks_WithLinksInHead() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		Document doc = ThymesheetPreprocessorTests.createDocWithLinks();

		locator.removeThymesheetLinks(doc);
		List<String> paths = locator.getThymesheetPaths(doc);

		assertEquals(Collections.emptyList(), paths);
	}

	@Test
	public void removeThymesheetLinks_WithLinksOutsideHead() {
		HtmlThymesheetLocator locator = new HtmlThymesheetLocator();
		Document doc = ThymesheetPreprocessorTests.createDocWithLinkOutsideHead();

		locator.removeThymesheetLinks(doc);
		List<String> paths = locator.getThymesheetPaths(doc);

		assertEquals(Collections.emptyList(), paths);
	}
}
