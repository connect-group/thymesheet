package com.connect_group.thymesheet.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;

import com.connect_group.thymesheet.ThymesheetLocator;

public class HtmlThymesheetLocator implements ThymesheetLocator {
	private static final String LINK_ELEMENT_NAME = "link";
	private static final String LINK_REL_ATTRIBUTE_VALUE = "thymesheet";
	
	public void removeThymesheetLinks(Document document) {
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
	
	public List<String> getThymesheetPaths(Document document) {
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
