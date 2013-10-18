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
