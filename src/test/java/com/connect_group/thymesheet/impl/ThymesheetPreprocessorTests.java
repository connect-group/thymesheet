package com.connect_group.thymesheet.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;

import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.connect_group.thymesheet.impl.ThymesheetPreprocessor;

public class ThymesheetPreprocessorTests {

	@Test
	public void isThymesheetLink_WithNull_ShouldReturnFalse() {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		assertFalse(preprocess.isThymesheetLink(null));
	}
	
	@Test
	public void isThymesheetLink_WithNonLinkElement_ShouldReturnFalse() {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		Element el = new Element("title");
		assertFalse(preprocess.isThymesheetLink(el));
	}

	@Test
	public void isThymesheetLink_WithNonThymesheetLinkElement_ShouldReturnFalse() {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		Element el = new Element("link");
		el.setAttribute("rel", "stylesheet");
		assertFalse(preprocess.isThymesheetLink(el));
	}

	@Test
	public void isThymesheetLink_WithThymesheetLinkElement_ShouldReturnTrue() {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		Element el = new Element("thymesheet");
		el.setAttribute("rel", "thymesheet");
		assertFalse(preprocess.isThymesheetLink(el));
	}
	
	@Test
	public void getHead_WithHead_ShouldReturnHeadElement() {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		Document doc = new Document("docName");
		Element html = new Element("html");
		doc.addChild(html);
		Element head = new Element("head");
		html.addChild(head);
		Element body = new Element("body");
		html.addChild(body);
		
		Element hd = preprocess.getHead(doc);
		
		assertNotNull(hd);
		assertEquals(hd.getNormalizedName(), "head");
	}
	
	@Test
	public void getHead_WithoutHead_ShouldReturnNull() {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		Document doc = new Document("docName");
		Element html = new Element("html");
		doc.addChild(html);
		Element body = new Element("body");
		html.addChild(body);
		
		Element hd = preprocess.getHead(doc);
		
		assertNull(hd);
	}
	
	@Test
	public void getThymesheetLinks_WithValidLinks() {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		
		Document doc = createDocWithLinks();
		
		List<Element> links = new ArrayList<Element>(2);
		preprocess.getThymesheetLinkElementsFromParent(preprocess.getHead(doc), links);
		
		assertEquals(2, links.size());
		assertEquals("style.ts", links.get(0).getAttributeValue("href"));
		assertEquals("style2.ts", links.get(1).getAttributeValue("href"));
		
	}
	
	@Test
	public void getThymesheetLinks_WithNoValidLinks() {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		
		Document doc = new Document("docName");
		Element html = new Element("html");
		doc.addChild(html);
		Element head = new Element("head");
		html.addChild(head);
		Element body = new Element("body");
		html.addChild(body);
		head.addChild(new Element("title"));
		head.addChild(createLink("stylesheet", "style.css", null));
		head.addChild(createLink(null,null,null));
		Element artificial = new Element("block");
		head.addChild(artificial);
		artificial.addChild(createLink("icon", "style2.ico", null));
		
		List<Element> links = new ArrayList<Element>(2);
		preprocess.getThymesheetLinkElementsFromParent(head, links);
		
		assertEquals(0, links.size());	
	}
	
	@Test
	public void getThymesheetFilePaths_WithNoValidLinks() {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		Document doc = createDocWithLinks();
		List<String> filePaths = preprocess.getThymesheetFilePaths(doc);
		
		assertEquals(2, filePaths.size());
		assertEquals("style.ts", filePaths.get(0));
		assertEquals("style2.ts", filePaths.get(1));

	}
	
	@Test 
	public void loadSingleThymesheetFile() throws IOException {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		List<String> filePaths = new ArrayList<String>();
		filePaths.add("/test.ts");
		InputStream thymesheetInputStream = preprocess.getInputStream(filePaths);
		AttributeRuleList attributeRules = preprocess.getRuleList(thymesheetInputStream);
		
		
		List<SingleRule> rules = getSingleRules(attributeRules);
		
		assertEquals(3, rules.size());
		assertEquals(new SingleRule("th:p", "th-utext", "\"'apple'\""), rules.get(0));
		assertEquals(new SingleRule("p#someid", "th-utext", "\"#{banana}\""), rules.get(1));
		assertEquals(new SingleRule("*.fresh", "th-remove", "\"all-but-first\""), rules.get(2));


	}
	
	@Test 
	public void loadTwoThymesheetFiles() throws IOException {
		ThymesheetPreprocessor preprocess = new ThymesheetPreprocessor();
		List<String> filePaths = new ArrayList<String>();
		filePaths.add("/test.ts");
		filePaths.add("/test2.ts");
		InputStream thymesheetInputStream = preprocess.getInputStream(filePaths);
		AttributeRuleList ruleList = preprocess.getRuleList(thymesheetInputStream);
		List<SingleRule> rules = getSingleRules(ruleList);
		
		assertEquals(4, rules.size());
		assertEquals(new SingleRule("th:p", "th-utext", "\"'apple'\""), rules.get(0));
		assertEquals(new SingleRule("p#someid", "th-utext", "\"#{banana}\""), rules.get(1));
		assertEquals(new SingleRule("*.fresh", "th-remove", "\"all-but-first\""), rules.get(2));
		assertEquals(new SingleRule("*#someid", "th-text", "\"#a.b.c(${x})\""), rules.get(3));

	}
	

	
	private List<SingleRule> getSingleRules(AttributeRuleList ruleList) {
		ArrayList<SingleRule> result = new ArrayList<SingleRule>();
		
		for (CSSStyleRule styleRule : ruleList) {
			String selectorText = styleRule.getSelectorText();
			CSSStyleDeclaration styles = styleRule.getStyle();
			
	        for (int j = 0; j < styles.getLength(); j++) {
	             String property = styles.item(j);
	             result.add(new SingleRule(selectorText, property, styles.getPropertyCSSValue(property).getCssText()));
	        }
		}
		
		return result;
	}
	
	private static Element createLink(String rel, String href, String type) {
		Element link = new Element("link");
		
		if(rel!=null) {
			link.setAttribute("rel", rel);
		}
		
		if(href!=null) {
			link.setAttribute("href", href);
		}
		
		if(type!=null) {
			link.setAttribute("type", type);
		}
		
		return link;
	}
	
	public static Document createDocWithLinks() {
		Document doc = new Document("docName");
		Element html = new Element("html");
		doc.addChild(html);
		Element head = new Element("head");
		html.addChild(head);
		Element body = new Element("body");
		html.addChild(body);
		head.addChild(new Element("title"));
		head.addChild(createLink("stylesheet", "style.css", null));
		head.addChild(createLink(null,null,null));
		head.addChild(createLink("thymesheet", "style.ts", null));
		
		
		Element artificial = new Element("block");
		head.addChild(artificial);
		artificial.addChild(createLink("thymesheet", "style2.ts", null));
		
		
		Element h1 = new Element("h1");
		h1.addChild(new Text("Heading"));
		body.addChild(h1);
		
		Element para = new Element("p");
		para.addChild(new Text("Hello, this is a test document"));
		body.addChild(para);
		
		para = new Element("p");
		para.setAttribute("id", "someid");
		para.addChild(new Text("Second paragraph with an id."));
		body.addChild(para);
		
		return doc;
	}

	class SingleRule {
		public SingleRule(String selector, String property, String value) {
			this.selector = selector==null? "" : selector;
			this.property = property==null? "" : property;
			this.value = value==null? "" : value;
		}
		
		@Override
		public boolean equals(Object o) {
			boolean result = false;
			
			if(o instanceof SingleRule) {
				SingleRule other = (SingleRule)o;
				result = selector.equals(other.selector) && property.equals(other.property) && value.equals(other.value);
			}
			
			return result;
		}
		
		@Override
		public String toString() {
			return selector + " { " + property + ": \"" + value + "\" }";
		}
		
		public final String selector;
		public final String property;
		public final String value;
	}
}
