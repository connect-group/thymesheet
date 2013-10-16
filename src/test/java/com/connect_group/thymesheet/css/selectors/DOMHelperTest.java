package com.connect_group.thymesheet.css.selectors;

import static org.junit.Assert.*;

import org.junit.Test;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;

import com.connect_group.thymesheet.css.selectors.dom.DOMHelper;

public class DOMHelperTest {

	
	
	@Test
	public void testGetElementsByTagName() {
		Document doc = new Document();
		Element html = new Element("html");
		doc.addChild(html);
		Element body = new Element("body");
		html.addChild(body);
		Element tag = new Element("a");
		body.addChild(tag);
		tag = new Element("div");
		body.addChild(tag);
		tag.addChild(new Element("span"));
		tag.addChild(new Element("div"));
		
		assertEquals(2, DOMHelper.getElementsByTagName(doc, "div").size());
		assertEquals(1, DOMHelper.getElementsByTagName(doc, "a").size());
		assertEquals(0, DOMHelper.getElementsByTagName(doc, "pre").size());
		assertEquals(6, DOMHelper.getElementsByTagName(doc, "*").size());
	}
}
