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
import org.thymeleaf.dom.NestableNode;

import com.connect_group.thymesheet.ThymesheetLocator;
import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.connect_group.thymesheet.query.HtmlElement;
import com.connect_group.thymesheet.query.HtmlElements;

public class HtmlThymesheetLocator extends LookupTableThymesheetLocator implements ThymesheetLocator {

	public HtmlThymesheetLocator() {}
	
	public HtmlThymesheetLocator(Map<String,String> lookupTable) {
		super(lookupTable);
	}
	
	public HtmlThymesheetLocator(Properties lookupTable) {
		super(lookupTable);
	}

	
	public void removeThymesheetLinks(Document document) {
		HtmlElements thymesheetLinks = getThymesheetLinks(document);
		for(HtmlElement link : thymesheetLinks) {
			NestableNode parent = link.getElement().getParent();
			parent.removeChild(link.getElement());
		}
	}
	
	public List<String> getThymesheetPaths(Document document) {
		List<String> filePaths = new ArrayList<String>();
		
		HtmlElements thymesheetLinks = getThymesheetLinks(document);
		for(HtmlElement link : thymesheetLinks) {
			String href = link.attr("href");
			if(href!=null && href.length() > 0) {
				filePaths.add(href);
			}
		}
		
		filePaths.addAll(super.getThymesheetPaths(document));
		
		return filePaths;
	}
	
	private HtmlElements getThymesheetLinks(Document document) {
		HtmlElements elements = new HtmlElements(document);
		try {
			return  elements.matching("link[rel=thymesheet]");
		} catch(NodeSelectorException e) {
			throw new IllegalArgumentException("Invalid document or css query link[rel=thymesheet]",e);
		}
	}

	
}
