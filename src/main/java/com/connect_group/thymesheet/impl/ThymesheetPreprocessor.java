package com.connect_group.thymesheet.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.thymeleaf.dom.DOMSelector;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.dom.Node;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.steadystate.css.parser.CSSOMParser;

public class ThymesheetPreprocessor {
	private static final String LINK_ELEMENT_NAME = "link";
	private static final String LINK_REL_ATTRIBUTE_VALUE = "thymesheet";
	
	public ThymesheetPreprocessor() {}
	
	public void preprocess(Document document) throws IOException {
		List<String> filePaths = getThymesheetFilePaths(document);
		InputStream thymesheetInputStream = getInputStream(filePaths);
		CSSRuleList ruleList = getRuleList(thymesheetInputStream);
		applyRules(document, ruleList);
		removeThymesheetLinks(document);
	}

	private void removeThymesheetLinks(Document document) {
		Element head = getHead(document);
		if(head!=null) {
			List<Element> links = new ArrayList<Element>(10);
			getThymesheetLinkElementsFromParent(head, links);
			if(!links.isEmpty()) {
				for(Element link : links) {
					NestableNode parent = link.getParent();
					parent.removeChild(link);
				}
			}
		}
	}

	private void applyRules(Document document, CSSRuleList ruleList) {
		for (int i = 0; i < ruleList.getLength(); i++) {
			CSSRule rule = ruleList.item(i);
			if (rule instanceof CSSStyleRule) {
				CSSStyleRule styleRule=(CSSStyleRule)rule;
				handleRule(document, styleRule.getSelectorText(), asMap(styleRule.getStyle()));
			}
		}
	}

	private Map<String, String> asMap(CSSStyleDeclaration styles) {
		LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
		
		for (int j = 0; j < styles.getLength(); j++) {
        	String property = getDataAttributeName(styles.item(j));
        	String value = styles.getPropertyCSSValue(styles.item(j)).getCssText();
        	result.put(property, value);
        }
		return result;
	}

	protected void handleRule(Document document, String selectorText, Map<String,String> styles) {

		DOMSelector selector = new DOMSelector(selectorText);
		List<Node> matches = selector.select(document);
		for(Node matchedNode : matches) {
			if(matchedNode instanceof Element) {
				Element element = (Element)matchedNode;
		        for (Map.Entry<String,String> rule : styles.entrySet()) {
		        	String property = getDataAttributeName(rule.getKey());
		        	element.setAttribute(property, getDataAttributeValue(rule.getValue()));
		        }
			}
		}
	}
	
	private String getDataAttributeValue(String value) {
		if(value==null) return "";
		
		if(value.startsWith("\"") && value.endsWith("\"")) {
			value = value.substring(1, value.length()-1);
		}
		return value;
	}

	private String getDataAttributeName(String propertyName) {
		if(propertyName==null) return "";
		
		propertyName = propertyName.replaceAll(":", "-");
		
		if(!propertyName.startsWith("data-")) {
			propertyName = "data-" + propertyName;
		}
		return propertyName;
	}

	CSSRuleList getRuleList(InputStream stream) throws IOException {
		InputSource source = new InputSource(new InputStreamReader(stream));
        CSSOMParser parser = new CSSOMParser();
        CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);
        CSSRuleList ruleList = stylesheet.getCssRules();
        
        return ruleList;
	}

	protected InputStream getInputStream(List<String> filePaths) throws IOException {
		List<InputStream> opened = new ArrayList<InputStream>(filePaths.size());
		
		try {
			openFiles(filePaths, opened);
		} catch (IOException ex) {
			closeInputStreams(opened);
			throw ex;
		}
		
		InputStream is = new SequenceInputStream(Collections.enumeration(opened));
		return is;
	}
	
	private void closeInputStreams(Collection<InputStream> opened) {
		for(InputStream is : opened) {
			try { 
				is.close();
			} catch(IOException ix) {}
		}
	}
	
	protected void openFiles(List<String> filePaths, List<InputStream> opened) throws FileNotFoundException {
		for(String filePath : filePaths) {
			InputStream stream = getClass().getResourceAsStream(filePath);
			if(stream==null) {
				throw new FileNotFoundException("Thymesheet file \""+filePath+"\" not found.");
			}
			
			opened.add(stream);
		}
	}

	protected List<String> getThymesheetFilePaths(Document document) {
		List<String> filePaths = null;
		
		Element head = getHead(document);
		if(head!=null) {
			List<Element> links = new ArrayList<Element>(10);
			getThymesheetLinkElementsFromParent(head, links);
			if(!links.isEmpty()) {
				filePaths = new ArrayList<String>(links.size());
				for(Element link : links) {
					String href = link.getAttributeValue("href");
					if(href!=null && href.length() > 0) {
						filePaths.add(href);
					}
				}
			}
		}
		
		if(filePaths==null) {
			filePaths = Collections.emptyList();
		}
		
		return filePaths;
	}

	Element getHead(Document document) {
		Element head = null;
		
		List<Element> children = document.getElementChildren();
		for(Element child : children) {
			if("html".equalsIgnoreCase(child.getNormalizedName())) {
				
				Element html = child;
				children = html.getElementChildren();
				for(Element htmlChild : children) {
					if("head".equalsIgnoreCase(htmlChild.getNormalizedName())) {
						head=htmlChild;
						break;
					}
				}
				
				break;
			}
		}
		
		return head;
	}
	
	void getThymesheetLinkElementsFromParent(Element parent, List<Element> links) {
		// Link elements should be an immediate child of the head,
		// however in Thymeleaf there could be a th:block in there.
		List<Element> elements = parent.getElementChildren();
		if(elements!=null) {
			for(Element child : elements) {
				if(isThymesheetLink(child)) {
					links.add(child);
				} else {
					getThymesheetLinkElementsFromParent(child, links);
				}
			}
		}
	}
	
	boolean isThymesheetLink(Element element) {
		boolean result = false;
		
		if(element!=null && LINK_ELEMENT_NAME.equalsIgnoreCase(element.getNormalizedName())) {
			String relValue = element.getAttributeValue("rel");
			result = LINK_REL_ATTRIBUTE_VALUE.equalsIgnoreCase(relValue);
		}
		
		return result;
	}
}
