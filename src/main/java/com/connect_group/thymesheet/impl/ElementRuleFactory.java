package com.connect_group.thymesheet.impl;

import java.util.Arrays;
import java.util.Map;

public class ElementRuleFactory {
	private ElementRuleFactory() {}
	
	private static final String[] DOM_MODIFIERS={
		"before", 
		"after", 
		"wrap",
		"nest"
	};

	public static boolean isDOMModifierPsuedoElement(String pseudoClassName) {
		return Arrays.asList(DOM_MODIFIERS).contains(pseudoClassName);
	}
	
	public static ElementRule createElementRule(PseudoClass pseudoModifier, Map<String,String> properties) {
		ElementRule rule = null;
		String ruleName = pseudoModifier.getName();
		
		if(ruleName.equals("before")) {
			rule = new BeforeElementRule(pseudoModifier, properties);
		} else if(ruleName.equals("after")) {
			rule = new AfterElementRule(pseudoModifier, properties);
		} else if(ruleName.equals("wrap")) {
			rule = new WrapElementRule(pseudoModifier, properties);
		} else if(ruleName.equals("nest")) {
			rule = new NestElementRule(pseudoModifier, properties);
		}
		
		return rule;
	}
}
