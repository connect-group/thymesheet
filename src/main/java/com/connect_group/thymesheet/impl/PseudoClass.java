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
