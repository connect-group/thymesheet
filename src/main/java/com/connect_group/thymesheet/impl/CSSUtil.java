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

import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.css.CSSStyleDeclaration;

public class CSSUtil {
	private CSSUtil() {}
	
	public static Map<String, String> asMap(CSSStyleDeclaration styles) {
		LinkedHashMap<String,String> result = new LinkedHashMap<String,String>();
		
		for (int j = 0; j < styles.getLength(); j++) {
        	String property = getDataAttributeName(styles.item(j));
        	String value = getDataAttributeValue(styles.getPropertyCSSValue(styles.item(j)).getCssText());
        	result.put(property, value);
        }
		return result;
	}
	
	private static String getDataAttributeValue(String value) {
		if(value==null) return "";
		
		if(value.startsWith("\"") && value.endsWith("\"")) {
			value = value.substring(1, value.length()-1);
		}
		return value;
	}

	private static String getDataAttributeName(String propertyName) {
		if(propertyName==null) return "";
		
		propertyName = propertyName.replaceAll(":", "-");
		
		if(!propertyName.startsWith("data-")) {
			propertyName = "data-" + propertyName;
		}
		return propertyName;
	}
}
