package com.connect_group.thymesheet.impl;

import java.util.Map;

import org.thymeleaf.dom.Element;

public class AfterElementRule extends ElementRule {

	public AfterElementRule(PseudoClass pseudoClass, Map<String, String> properties) {
		super(pseudoClass, properties);
	}

	@Override
	protected void injectNewElement(Element target, Element newElement) {
		target.addChild(newElement);
	}

}
