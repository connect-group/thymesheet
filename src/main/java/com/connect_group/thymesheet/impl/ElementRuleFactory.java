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
