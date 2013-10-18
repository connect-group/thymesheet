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
