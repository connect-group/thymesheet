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
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;

import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.connect_group.thymesheet.css.selectors.dom.DOMNodeSelector;

public class AttributeRuleList extends ArrayList<CSSStyleRule> {

	private static final long serialVersionUID = -9023109371091063376L;

	public AttributeRuleList() {
		super();
	}
	
	public AttributeRuleList(int initialCapacity) {
		super(initialCapacity);
	}
	
	public AttributeRuleList(Collection<? extends CSSStyleRule> c) {
		super(c);
	}
	
	public AttributeRuleList(CSSRuleList ruleList) {
		super(ruleList.getLength());
		
		for (int i = 0; i < ruleList.getLength(); i++) {
			CSSRule rule = ruleList.item(i);
			if(rule instanceof CSSStyleRule) {
				this.add((CSSStyleRule)rule);
			}
		}

	}
	
	public void applyTo(Document document) throws NodeSelectorException {
		for (CSSStyleRule styleRule : this) {
			handleRule(document, styleRule.getSelectorText(), CSSUtil.asMap(styleRule.getStyle()));
		}
	}
	
	protected void handleRule(Document document, String selectorText, Map<String,String> styles) throws NodeSelectorException {

		DOMNodeSelector selector = new DOMNodeSelector(document);
		Set<Node> matches = selector.querySelectorAll(selectorText);
		for(Node matchedNode : matches) {
			if(matchedNode instanceof Element) {
				Element element = (Element)matchedNode;
		        for (Map.Entry<String,String> rule : styles.entrySet()) {
		        	element.setAttribute(rule.getKey(), rule.getValue());
		        }
			}
		}
	}
}
