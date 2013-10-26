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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.dom.Node;

public class WrapElementRule extends ElementRule {

	protected WrapElementRule(PseudoClass pseudoClass, Map<String, String> properties) {
		super(pseudoClass, properties);
	}

	@Override
	protected void injectNewElement(Element target, Element newElement) {

		if(!target.hasParent()) {
			throw new UnsupportedOperationException("WrapElementRule Cannot wrap root node");
		} 

		List<Node> toWrap = getNodesToWrap(target);
		wrapNodes(toWrap, newElement);
		
	}
	
	private void wrapNodes(List<Node> toWrap, Element newElement) {
		if(toWrap.size()>0) {
			NestableNode parent = toWrap.get(0).getParent();
			
			parent.insertBefore(toWrap.get(0), newElement);
			for(Node child : toWrap) {
				parent.removeChild(child);
				newElement.addChild(child);
			}
		}		
	}

	private List<Node> getNodesToWrap(Element target) {
		NestableNode parent = target.getParent();
		List<Node> children = parent.getChildren();

		List<Node> toWrap = new ArrayList<Node>();

		final int maxElementCount = getNumberOfElementsToWrap();
		final int startIndex = indexOf(children, target);
		final int step = getStepDirection();
		int offset=0;
		int elementCount=0;

		
		while(elementCount<maxElementCount && startIndex+offset >=0 && startIndex+offset < children.size()) {
			Node child = children.get(startIndex+offset);
			offset+=step;
			if(child instanceof Element) {
				elementCount++;
			}
			
			if(step>0) {
				toWrap.add(child);
			} else {
				toWrap.add(0, child);
			}
		}	
		
		return toWrap;
	}
	
	private int getNumberOfElementsToWrap() {
		int count = 1;
		
		String args = getModificationArgs();
		if(args==null || args.length()==0) {
			count = 1;
		} else if("*".equals(args) || "-*".equals(args)) {
			count = Integer.MAX_VALUE;
		} else {
			try {
				count = Math.abs(Integer.parseInt(args));
			} catch(NumberFormatException ex) {}
		}
		
		return count;
	}
	
	private int getStepDirection() {
		int step = 1;
		
		String args = getModificationArgs();
		if(args!=null && args.length()>0 && args.charAt(0)=='-') {
			step =-1;
		}
		
		return step;
	}
	
	private int indexOf(List<Node> children, Element target) {
		int i;
		for(i=0; i<children.size(); i++) {
			Node node = children.get(i);
			if(node.equals(target)) {
				break;
			}
		}
		
		if(i>=children.size()) {
			throw new IllegalArgumentException("WrapElementRule::indexOf - child was not found in parent!");
		}
		
		return i;
	}

}
