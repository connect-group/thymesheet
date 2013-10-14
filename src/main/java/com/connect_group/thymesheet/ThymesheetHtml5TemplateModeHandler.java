package com.connect_group.thymesheet;

import org.thymeleaf.templateparser.xmlsax.XhtmlAndHtml5NonValidatingSAXTemplateParser;
import org.thymeleaf.templatewriter.XhtmlHtml5TemplateWriter;

import com.connect_group.thymesheet.impl.ThymesheetTemplateModeHandler;

public class ThymesheetHtml5TemplateModeHandler extends ThymesheetTemplateModeHandler {

	protected ThymesheetHtml5TemplateModeHandler() {
		super("HTML5", 
                new XhtmlAndHtml5NonValidatingSAXTemplateParser(getPoolSize()),
                new XhtmlHtml5TemplateWriter());

	}

}
