package com.connect_group.thymesheet;

import java.io.IOException;

public class ThymesheetProcessorException extends IOException {

	private static final long serialVersionUID = 6752946320942025461L;
	
	public ThymesheetProcessorException() {
		super();
	}
	
	public ThymesheetProcessorException(String message) {
		super(message);
	}
	
	public ThymesheetProcessorException(String message, Throwable t) {
		super(message, t);
	}
	
}
