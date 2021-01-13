/**
 * Copyright (c) 2009-2012, Christer Sandberg
 * Thymesheet modifications Copyright (c) 2013 Adam Perry, Connect Group
 */
package com.connect_group.thymesheet.css.selectors.specifier;

import com.connect_group.thymesheet.css.selectors.Specifier;
import com.connect_group.thymesheet.css.util.Assert;

/**
 * An implementation of {@link Specifier} for pseudo-classes.
 * <p>
 * Note:
 * <br>
 * The negation pseudo-class specifier is implemented by {@link NegationSpecifier}, and
 * the {@code nth-*} pseudo-classes are implemented by {@link PseudoNthSpecifier}.
 * 
 * @see <a href="http://www.w3.org/TR/css3-selectors/#pseudo-classes">Pseudo-classes</a>
 * 
 * @author Christer Sandberg
 */
public class PseudoClassSpecifier implements Specifier {
    
    /** The pseudo-class value. */
    private final String value;
    
    /**
     * Create a new pseudo-class specifier with the specified value.
     * 
     * @param value The pseudo-class value.
     */
    public PseudoClassSpecifier(String value) {
        Assert.notNull(value, "value is null!");
        this.value = value;
    }
    
    /**
     * Get the pseudo-class value.
     * 
     * @return The pseudo-class value.
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
