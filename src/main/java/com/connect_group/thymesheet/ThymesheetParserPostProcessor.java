package com.connect_group.thymesheet;

import org.thymeleaf.dom.Document;

/**
 * When a source file has been loaded and the Thymesheet script merged with the 
 * source HTML, a PostProcessor may optionally modify the resultant document.
 * 
 * @author adam
 *
 */
public interface ThymesheetParserPostProcessor {
	void postProcess(String documentName, Document document) throws Exception;
}
