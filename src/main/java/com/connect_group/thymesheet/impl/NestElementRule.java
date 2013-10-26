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
