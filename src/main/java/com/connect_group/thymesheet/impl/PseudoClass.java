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

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PseudoClass {
	public static final PseudoClass NO_PSEUDO_CLASS = new PseudoClass("","","");
	
	private static Pattern classNamePattern = Pattern.compile("^()([^\\(]*)(\\((\\d*|\\*|-\\*)?\\))?$");
	private static Pattern lastPseudoClassPattern = Pattern.compile("(.*):([^\\(]*)(\\((\\d*|\\*|-\\*)?\\))?$");
	
	
	private final String name;
	private final String args;
	private final String selector;
	
	private PseudoClass(String name, String args, String selector) {
		this.name=name;
		this.args=args;
		this.selector=selector;
	}
	
	public String getName() {
		return name;
	}
	
	public String getArgs() {
		return args;
	}
	
	public String getSelector() {
		return selector;
	}
	
	public boolean exists() {
		return name!=null && name.length()>0;
	}
	
	public static PseudoClass fromPseudoClass(String psuedoClassString) {
		return fromPatternMatch(psuedoClassString, classNamePattern);
	}
	
	public static PseudoClass lastPseudoClassFromSelector(String selector) {
		return fromPatternMatch(selector, lastPseudoClassPattern);
	}
	
	private static PseudoClass fromPatternMatch(final String string, final Pattern pattern) {
		Matcher matcher = pattern.matcher(string);
		if(matcher.matches() && matcher.groupCount()==4) {
			String selector=matcher.group(1);
			String name = matcher.group(2);
			String args = matcher.group(4);
			if(args==null) args="";
			if(selector==null) selector="";
		
			return new PseudoClass(name, args, selector);
		}
		
		return NO_PSEUDO_CLASS;
	}
}
