/**
 * Copyright (c) 2009-2012, Christer Sandberg
 * Thymesheet modifications Copyright (c) 2013 Adam Perry, Connect Group
 */
package com.connect_group.thymesheet.css.selectors.dom;

import java.util.ArrayList;
import java.util.List;

import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.CDATASection;
import org.thymeleaf.dom.Comment;
import org.thymeleaf.dom.DocType;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;

import com.connect_group.thymesheet.css.selectors.Selector;

/**
 * Helper methods for DOM operations.
 * 
 * @author Christer Sandberg
 */
public class DOMHelper {

    /**
     * Private CTOR.
     */
    private DOMHelper() {
    }

    /**
     * Get the first child node that is an element node.
     * 
     * @param node The node whose children should be iterated.
     * @return The first child element or {@code null}.
     */
    public static Element getFirstChildElement(Node node) {
    	
    	if(node instanceof NestableNode) {
    		return ((NestableNode) node).getFirstElementChild();
    	}
        
        return null;
    }
    
    /**
     * Get the next sibling element.
     * 
     * @param node The start node.
     * @return The next sibling element or {@code null}.
     */
    public static final Element getNextSiblingElement(Node node) {
    	
    	List<Node> siblings = node.getParent().getChildren();
    	Node n = null;
    	
    	int index = siblings.indexOf(node) + 1;
    	if(index>0 && index<siblings.size()) {
	    	n = siblings.get(index);
	    	while(!(n instanceof Element) && ++index < siblings.size()) {
	    		n = siblings.get(index);
	    	}
	    	
	    	if(index==siblings.size()) {
	    		n = null;
	    	}
    	}
    	
        return (Element) n;
    }
    
    /**
     * Get the previous sibling element.
     * 
     * @param node The start node.
     * @return The previous sibling element or {@code null}.
     */
    public static final Element getPreviousSiblingElement(Node node) {
    	
    	List<Node> siblings = node.getParent().getChildren();
    	Node n = null;
    	
    	int index = siblings.indexOf(node) - 1;

    	if(index>=0) {
	    	n = siblings.get(index);
	    	while(!(n instanceof Element) && --index >= 0) {
	    		n = siblings.get(index);
	    	}
	    	
	    	if(index<0) {
	    		n = null;
	    	}
    	}
        
        return (Element) n;
    }
    
    public static final String getNodeName(Object node) {
    	if(node==null) {
    		return "";
    	} if(node instanceof Element) {
    		return ((Element) node).getNormalizedName();
    	} else if(node instanceof Text) {
    		return "#text";
    	} else if(node instanceof Attribute) {
    		return ((Attribute) node).getNormalizedName();
    	} else if (node instanceof Macro || node instanceof CDATASection) {
    		return "#cdata-section";
    	} else if (node instanceof Comment) {
    		return "#comment";
    	} else if(node instanceof Document) {
    		return "#document";
    	} else if(node instanceof DocType) {
    		return ((DocType) node).getPublicId();
    	} 
    	
    	return "";
    }
    
    public static final Document getOwnerDocument(Node node) {
    	Document doc = null;
    	
    	if(node instanceof Document) {
    		doc = (Document) node;
    	} else {
    		
    		Node parent = node;
    		while(parent.hasParent()) {
    			parent = parent.getParent();
    			
    			if(parent instanceof Document) {
    				doc = (Document)parent;
    				break;
    			}
    		}
    		
    		
    	}
    	return doc;
    }
    
    public static final List<Node> getElementsByTagName(Node root, String tagName) {
    	ArrayList<Node> elements = new ArrayList<Node>(100);
    	getChildElementsByTagName(root, tagName, elements);
    	return elements;
    }
    
    private static final void getChildElementsByTagName(Node node, String tagName, ArrayList<Node> elements) {
    	if(node instanceof NestableNode) {
    		List<Element> children = ((NestableNode) node).getElementChildren();
    		
    		for(Element child : children) {
    			getElementsByTagName(child, tagName, elements);
    		}
    	}

    }
    
    private static final void getElementsByTagName(Node node, String tagName, ArrayList<Node> elements) {
    	
    	
    	if(node instanceof Element && (Selector.UNIVERSAL_TAG.equals(tagName) || ((Element)node).getNormalizedName().equalsIgnoreCase(tagName))) {
    		elements.add(node);
    	}
    	
    	getChildElementsByTagName(node, tagName, elements);
    	
    }
    
}
