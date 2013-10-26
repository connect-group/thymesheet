/**
 * Copyright (c) 2009-2012, Christer Sandberg
 * Thymesheet modifications Copyright (c) 2013 Adam Perry, Connect Group
 */
package com.connect_group.thymesheet.css.selectors.dom.internal;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.thymeleaf.dom.Attribute;
import org.thymeleaf.dom.NestableAttributeHolderNode;
import org.thymeleaf.dom.Node;

import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.connect_group.thymesheet.css.selectors.specifier.AttributeSpecifier;
import com.connect_group.thymesheet.css.util.Assert;

/**
 * A {@link NodeTraversalChecker} that check if a node's attribute
 * matches the {@linkplain AttributeSpecifier attribute specifier} set.
 * 
 * @author Christer Sandberg
 */
public class AttributeSpecifierChecker extends NodeTraversalChecker {
    
    /** The attribute specifier to check against. */
    private final AttributeSpecifier specifier;
   
    /**
     * Create a new instance.
     * 
     * @param specifier The attribute specifier to check against. 
     */
    public AttributeSpecifierChecker(AttributeSpecifier specifier) {
        Assert.notNull(specifier, "specifier is null!");
        this.specifier = specifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
        Assert.notNull(nodes, "nodes is null!");
        Set<Node> result = new LinkedHashSet<Node>();
        for (Node node : nodes) {
        	if(node instanceof NestableAttributeHolderNode) {
	        	
	        	Map<String,Attribute> map = ((NestableAttributeHolderNode) node).getAttributeMap();
	            if (map == null || map.isEmpty()) {
	                continue;
	            }
	            
	            Attribute attr = map.get(specifier.getName());
	            if (attr == null) {
	                continue;
	            }
	            
	            // It just have to be present.
	            if (specifier.getValue() == null) {
	                result.add(node);
	                continue;
	            }
	            
	            String value = attr.getValue().trim();
	            if (value.length() != 0) {
	                String val = specifier.getValue();
	                switch (specifier.getMatch()) {
	                case EXACT:
	                    if (value.equals(val)) {
	                        result.add(node);
	                    }
	                    
	                    break;
	                case HYPHEN:
	                    if (value.equals(val) || value.startsWith(val + '-')) {
	                        result.add(node);
	                    }
	                    
	                    break;
	                case PREFIX:
	                    if (value.startsWith(val)) {
	                        result.add(node);
	                    }
	                    
	                    break;
	                case SUFFIX:
	                    if (value.endsWith(val)) {
	                        result.add(node);
	                    }
	                    
	                    break;
	                case CONTAINS:
	                    if (value.contains(val)) {
	                        result.add(node);
	                    }
	                    
	                    break;
	                case LIST:
	                    for (String v : value.split("\\s+")) {
	                        if (v.equals(val)) {
	                            result.add(node);
	                        }
	                    }
	                    
	                    break;
	                }
	            }
        	}
        }
        
        return result;
    }

}
