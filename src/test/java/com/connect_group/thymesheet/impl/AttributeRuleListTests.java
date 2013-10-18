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
