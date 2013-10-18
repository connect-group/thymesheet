package com.connect_group.thymesheet.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;

import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.connect_group.thymesheet.css.selectors.dom.DOMNodeSelector;

public class ElementRuleList extends ArrayList<ElementRule> {

	private static final long serialVersionUID = -6309591703813336467L;
	
	
	public ElementRuleList() {
		super();
	}
	
	public ElementRuleList(int initialCapacity) {
		super(initialCapacity);
	}
	
	public ElementRuleList(Collection<? extends ElementRule> c) {
		super(c);
	}

	public void applyTo(Document document) throws NodeSelectorException {
		List<Set<Node>> elements = getMatchedElements(document);

		for (int i=0; i<this.size(); i++) {
			handleRule(this.get(i), elements.get(i));
		}
	}
	
	List<Set<Node>> getMatchedElements(Document document) throws NodeSelectorException {
		List<Set<Node>> elements = new ArrayList<Set<Node>>(this.size());
		
		DOMNodeSelector selector = new DOMNodeSelector(document);
		for (ElementRule rule : this) {
			Set<Node> matches = selector.querySelectorAll(rule.getSelector());
			elements.add(matches);
		}
		return elements;
	}
	
	protected void handleRule(ElementRule elementRule, Set<Node> matches) throws NodeSelectorException {
		
		for(Node matchedNode : matches) {
			if(matchedNode instanceof Element) {
				Element element = (Element)matchedNode;
				elementRule.applyTo(element);
			}
		}
	}


}
