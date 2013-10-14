package com.connect_group.thymesheet;

import org.thymeleaf.templateparser.xmlsax.XhtmlAndHtml5NonValidatingSAXTemplateParser;
import org.thymeleaf.templatewriter.XhtmlHtml5TemplateWriter;

import com.connect_group.thymesheet.impl.ThymesheetTemplateModeHandler;

public class ThymesheetXhtmlModeHandler extends ThymesheetTemplateModeHandler {

	public ThymesheetXhtmlModeHandler() {
		super("THYMESHEET_XHTML", 
				new XhtmlAndHtml5NonValidatingSAXTemplateParser(getPoolSize()),
				new XhtmlHtml5TemplateWriter());
	}

}
