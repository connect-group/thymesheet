/**
 * Copyright (c) 2009-2012, Christer Sandberg
 * Thymesheet modifications Copyright (c) 2013 Adam Perry, Connect Group
 */
package com.connect_group.thymesheet.css.selectors.specifier;

import com.connect_group.thymesheet.css.selectors.Specifier;
import com.connect_group.thymesheet.css.util.Assert;

/**
 * An implementation of {@link Specifier} for pseudo-elements.
 * 
 * @see <a href="http://www.w3.org/TR/css3-selectors/#pseudo-elements">Pseudo-elements</a>
 * 
 * @author Christer Sandberg
 */
public class PseudoElementSpecifier implements Specifier {
    
    /** The pseudo-element value. */
    private final String value;
    
    /**
     * Create a new pseudo-element specifier with the specified value.
     * 
     * @param value The pseudo-element value.
     */
    public PseudoElementSpecifier(String value) {
        Assert.notNull(value, "value is null!");
        this.value = value;
    }
    
    /**
     * Get the pseudo-element value.
     * 
     * @return The pseudo-element value.
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public Type getType() {
        return Type.PSEUDO;
    }

}
