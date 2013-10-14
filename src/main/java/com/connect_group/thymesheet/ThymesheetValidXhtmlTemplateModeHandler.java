package com.connect_group.thymesheet;

import org.thymeleaf.templateparser.xmlsax.XhtmlValidatingSAXTemplateParser;
import org.thymeleaf.templatewriter.XhtmlHtml5TemplateWriter;

import com.connect_group.thymesheet.impl.ThymesheetTemplateModeHandler;

public class ThymesheetValidXhtmlTemplateModeHandler extends ThymesheetTemplateModeHandler {

	protected ThymesheetValidXhtmlTemplateModeHandler() {
		super("VALIDXHTML", 
                new XhtmlValidatingSAXTemplateParser(getPoolSize()),
                new XhtmlHtml5TemplateWriter());
	}

}
