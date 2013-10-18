package com.connect_group.thymesheet.impl;

import java.util.Map;

import org.thymeleaf.dom.Element;

public class BeforeElementRule extends ElementRule {

	public BeforeElementRule(PseudoClass pseudoClass, Map<String, String> properties) {
		super(pseudoClass, properties);
	}

	@Override
	public void injectNewElement(Element target, Element newElement) {
		target.insertChild(0, newElement);
	}

}
