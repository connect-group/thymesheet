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

import java.util.Map;

import org.thymeleaf.dom.Element;

public abstract class ElementRule {
	private final PseudoClass pseudoClass;
	private final Map<String,String> properties;
	private final String tagName;
	
	protected ElementRule(PseudoClass pseudoClass, Map<String,String> properties) {
		this.pseudoClass = pseudoClass;
		this.properties = properties;
		
		String tagName = properties.get("data-tagname");
		properties.remove("data-tagname");
		if(tagName==null || tagName.length()==0) {
			tagName="th:block";
		}
		
 		this.tagName = tagName;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public String getSelector() {
		return pseudoClass.getSelector();
	}
	
	public String getModificationMethod() {
		return pseudoClass.getName();
	}
	
	public String getModificationArgs() {
		return pseudoClass.getArgs();
	}
	
	public Map<String,String> getProperties() {
		return properties;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder("<");
		builder.append(tagName);
		for(Map.Entry<String,String> entry : properties.entrySet()) {
			String name=entry.getKey();
			String value=entry.getValue();
			builder.append(" ").append(name).append("=\"").append(value).append("\"");
		}
		builder.append("></").append(tagName).append(">");
		return builder.toString();
	}
	
	public final void applyTo(Element target) {
		Element newElement = new Element(this.getTagName());
		
        for (Map.Entry<String,String> rule : getProperties().entrySet()) {
        	newElement.setAttribute(rule.getKey(), rule.getValue());
        }

        injectNewElement(target, newElement);
	}

	protected abstract void injectNewElement(Element target, Element newElement);
	
	
}
