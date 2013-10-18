package com.connect_group.thymesheet.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

import com.connect_group.thymesheet.ServletContextURLFactory;
import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;

public class ThymesheetPreprocessor {
	private static final String LINK_ELEMENT_NAME = "link";
	private static final String LINK_REL_ATTRIBUTE_VALUE = "thymesheet";
	private final ServletContextURLFactory urlFactory;
	
	public ThymesheetPreprocessor() {
		super();
		urlFactory = null;
	}
	
	public ThymesheetPreprocessor(ServletContextURLFactory urlFactory) {
		super();
		this.urlFactory = urlFactory;
	}
	
	
	
	public void preprocess(Document document) throws IOException {
		List<String> filePaths = getThymesheetFilePaths(document);
		InputStream thymesheetInputStream = getInputStream(filePaths);
		AttributeRuleList attributeRules = getRuleList(thymesheetInputStream);
		ElementRuleList elementRules=extractDOMModifications(attributeRules);
		
		try {
			attributeRules.applyTo(document);
			elementRules.applyTo(document);
		} catch (NodeSelectorException e) {
			throw new IOException("Invalid CSS Selector", e);
		}
		removeThymesheetLinks(document);
	}

	private ElementRuleList extractDOMModifications(List<CSSStyleRule> ruleList) {
		ElementRuleList modifierRules = new ElementRuleList();
		
		Iterator<CSSStyleRule> it = ruleList.iterator();
		while(it.hasNext()) {
			CSSStyleRule rule = it.next();
			
			PseudoClass pseudoModifier = getDOMModifier(rule.getSelectorText());
			if(pseudoModifier!=null) {
				ElementRule modifierRule = ElementRuleFactory.createElementRule(pseudoModifier, CSSUtil.asMap(rule.getStyle()));
				modifierRules.add(modifierRule);
				it.remove();
			}
		}
		
		return modifierRules;
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

	protected PseudoClass getDOMModifier(String selectorText) {
		
		PseudoClass pseudoClass = PseudoClass.lastPseudoClassFromSelector(selectorText);
		if(ElementRuleFactory.isDOMModifierPsuedoElement(pseudoClass.getName())) {
			return pseudoClass;
		}
		return null;
	}
	
	AttributeRuleList getRuleList(InputStream stream) throws IOException {
		InputSource source = new InputSource(new InputStreamReader(stream));
        CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
        CSSStyleSheet stylesheet = parser.parseStyleSheet(source, null, null);
        CSSRuleList ruleList = stylesheet.getCssRules();
        
        return new AttributeRuleList(ruleList);
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
			InputStream stream;
			if(isClassPath(filePath)) {
				stream = getClass().getResourceAsStream(fixClassPath(filePath));
			} else {
				try {
					stream = getFileFromWebapp(filePath);
				} catch (MalformedURLException e) {
					throw new FileNotFoundException("Unable to open " + filePath + " - " + e.getMessage());
				} catch (IOException e) {
					throw new FileNotFoundException("Unable to open " + filePath + " - " + e.getMessage());
				}

			}
			
			
			
			if(stream==null) {
				throw new FileNotFoundException("Thymesheet file \""+filePath+"\" not found.");
			}
			
			opened.add(stream);
		}
	}

	private String fixClassPath(String filePath) {
		return filePath.replace("classpath:", "");
	}

	private boolean isClassPath(String filePath) {
		return urlFactory == null || filePath.startsWith("classpath:");
	}
	
	private InputStream getFileFromWebapp(String filepath) throws IOException {
		URL thymesheetUrl = urlFactory.getURL(filepath);
		if(thymesheetUrl==null) {
			throw new FileNotFoundException("File \"" + filepath + "\" not found.");
		}
		return  thymesheetUrl.openStream();
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
