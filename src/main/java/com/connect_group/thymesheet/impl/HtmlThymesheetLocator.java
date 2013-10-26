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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;

import com.connect_group.thymesheet.ThymesheetLocator;

public class HtmlThymesheetLocator extends LookupTableThymesheetLocator implements ThymesheetLocator {
	private static final String LINK_ELEMENT_NAME = "link";
	private static final String LINK_REL_ATTRIBUTE_VALUE = "thymesheet";

	public HtmlThymesheetLocator() {}
	
	public HtmlThymesheetLocator(Map<String,String> lookupTable) {
		super(lookupTable);
	}
	
	public HtmlThymesheetLocator(Properties lookupTable) {
		super(lookupTable);
	}

	
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
		List<String> filePaths = new ArrayList<String>();
		
		NestableNode head = getHead(document);
		if(head==null) {
			head = document;
		}
		
		List<Element> links = new ArrayList<Element>(10);
		getThymesheetLinkElementsFromParent(head, links);
		if(!links.isEmpty()) {
			for(Element link : links) {
				String href = link.getAttributeValue("href");
				if(href!=null && href.length() > 0) {
					filePaths.add(href);
				}
			}
		}
		
		filePaths.addAll(super.getThymesheetPaths(document));
		
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
			} else if("head".equalsIgnoreCase(child.getNormalizedName())) {
				head = child;
				break;
			}
		}
		
		return head;
	}
	
	void getThymesheetLinkElementsFromParent(NestableNode parent, List<Element> links) {
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
