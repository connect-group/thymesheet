/**
 * Copyright (c) 2009-2012, Christer Sandberg
 * Thymesheet modifications Copyright (c) 2013 Adam Perry, Connect Group
 */
package com.connect_group.thymesheet.css.selectors.dom;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;

import com.connect_group.thymesheet.css.selectors.NodeSelector;
import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.connect_group.thymesheet.css.selectors.Selector;
import com.connect_group.thymesheet.css.selectors.Specifier;
import com.connect_group.thymesheet.css.selectors.dom.internal.AttributeSpecifierChecker;
import com.connect_group.thymesheet.css.selectors.dom.internal.NodeTraversalChecker;
import com.connect_group.thymesheet.css.selectors.dom.internal.PseudoClassSpecifierChecker;
import com.connect_group.thymesheet.css.selectors.dom.internal.PseudoNthSpecifierChecker;
import com.connect_group.thymesheet.css.selectors.dom.internal.TagChecker;
import com.connect_group.thymesheet.css.selectors.scanner.Scanner;
import com.connect_group.thymesheet.css.selectors.scanner.ScannerException;
import com.connect_group.thymesheet.css.selectors.specifier.AttributeSpecifier;
import com.connect_group.thymesheet.css.selectors.specifier.NegationSpecifier;
import com.connect_group.thymesheet.css.selectors.specifier.PseudoClassSpecifier;
import com.connect_group.thymesheet.css.selectors.specifier.PseudoNthSpecifier;
import com.connect_group.thymesheet.css.util.Assert;

/**
 * An implementation of a DOM based {@link NodeSelector}.
 * <p/>
 * <strong>Possible enhancements:</strong>
 * <br/>
 * When searching for an element by its {@code id} we traverse the whole
 * tree. An attribute with the name <strong>id</strong> is not of the type
 * id unless it's been defined that way by a DTD etc, and we can't assume that
 * this is the case. If it's possible to make this work somehow we could speed
 * this up a bit. Maybe!?
 */
public class DOMNodeSelector implements NodeSelector<Node> {

    /** The root node (document or element). */
    private final Node root;

    /**
     * Create a new instance.
     * 
     * @param root The root node. Must be a document or element node.
     */
    public DOMNodeSelector(Node root) {
        Assert.notNull(root, "root is null!");
        Assert.isTrue(root instanceof Document || root instanceof Element, "root must be a document or element node!");
        this.root = root;
    }
    
    /**
     * {@inheritDoc}
     */
    public Node querySelector(String selectors) throws NodeSelectorException {
        Set<Node> result = querySelectorAll(selectors);
        if (result.isEmpty()) {
            return null;
        }
        
        return result.iterator().next();
    }
    
    /**
     * {@inheritDoc}
     */
    public Set<Node> querySelectorAll(String selectors) throws NodeSelectorException {
        Assert.notNull(selectors, "selectors is null!");
        List<List<Selector>> groups;
        try {
            Scanner scanner = new Scanner(selectors);
            groups = scanner.scan();
        } catch (ScannerException e) {
            throw new NodeSelectorException(e);
        }

        Set<Node> results = new LinkedHashSet<Node>();
        for (List<Selector> parts : groups) {
            Set<Node> result = check(parts);
            if (!result.isEmpty()) {
                results.addAll(result);
            }
        }

        return results;
    }
    
    /**
     * Check the list of selector <em>parts</em> and return a set of nodes with the result.
     * 
     * @param parts A list of selector <em>parts</em>.
     * @return A set of nodes.
     * @throws NodeSelectorException In case of an error.
     */
    private Set<Node> check(List<Selector> parts) throws NodeSelectorException {
        Set<Node> result = new LinkedHashSet<Node>();
        result.add(root);
        for (Selector selector : parts) {
            NodeTraversalChecker checker = new TagChecker(selector);
            result = checker.check(result, root);
            if (selector.hasSpecifiers()) {
                for (Specifier specifier : selector.getSpecifiers()) {
                    switch (specifier.getType()) {
                    case ATTRIBUTE:
                        checker = new AttributeSpecifierChecker((AttributeSpecifier) specifier);
                        break;
                    case PSEUDO:
                        if (specifier instanceof PseudoClassSpecifier) {
                            checker = new PseudoClassSpecifierChecker((PseudoClassSpecifier) specifier);
                        } else if (specifier instanceof PseudoNthSpecifier) {
                            checker = new PseudoNthSpecifierChecker((PseudoNthSpecifier) specifier);
                        }
                        
                        break;
                    case NEGATION:
                        final Set<Node> negationNodes = checkNegationSpecifier((NegationSpecifier) specifier);
                        checker = new NodeTraversalChecker() {
                            @Override
                            public Set<Node> check(Set<Node> nodes, Node root) throws NodeSelectorException {
                                Set<Node> set = new LinkedHashSet<Node>(nodes);
                                set.removeAll(negationNodes);
                                return set;
                            }
                        };
                        
                        break;
                    }
                    
                    result = checker.check(result, root);
                    if (result.isEmpty()) {
                        // Bail out early.
                        return result;
                    }
                }
            }
        }
        
        return result;
    }
    
    /**
     * Check the {@link NegationSpecifier}.
     * <p/>
     * This method will add the {@link Selector} from the specifier in
     * a list and invoke {@link #check(List)} with that list as the argument.
     *  
     * @param specifier The negation specifier.
     * @return A set of nodes after invoking {@link #check(List)}.
     * @throws NodeSelectorException In case of an error.
     */
    private Set<Node> checkNegationSpecifier(NegationSpecifier specifier) throws NodeSelectorException {
        List<Selector> parts = new ArrayList<Selector>(1);
        parts.add(specifier.getSelector());
        return check(parts);
    }
    
}
