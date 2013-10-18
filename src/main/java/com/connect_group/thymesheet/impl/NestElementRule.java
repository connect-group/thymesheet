package com.connect_group.thymesheet.impl;

import java.util.Map;

import org.thymeleaf.dom.Element;

public class NestElementRule extends ElementRule {

	protected NestElementRule(PseudoClass pseudoClass, Map<String, String> properties) {
		super(pseudoClass, properties);
	}

	@Override
	protected void injectNewElement(Element target, Element newElement) {
		target.moveAllChildren(newElement);
		target.addChild(newElement);
	}

}
