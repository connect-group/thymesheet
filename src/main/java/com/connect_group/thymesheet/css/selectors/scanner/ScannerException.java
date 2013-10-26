/**
 * Copyright (c) 2009-2012, Christer Sandberg
 * Thymesheet modifications Copyright (c) 2013 Adam Perry, Connect Group
 */
package com.connect_group.thymesheet.css.selectors.scanner;

/**
 * An exception thrown on {@link Scanner} errors.
 * 
 * @author Christer Sandberg
 */
public class ScannerException extends Exception {
	
    /** Serial version UID. */
	private static final long serialVersionUID = -1430921277275539691L;

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message The detail message.
     */
	public ScannerException(String message) {
		super(message);
	}

}
