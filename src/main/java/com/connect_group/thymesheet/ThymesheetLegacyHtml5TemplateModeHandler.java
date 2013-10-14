package com.connect_group.thymesheet;

import org.thymeleaf.templateparser.html.LegacyHtml5TemplateParser;
import org.thymeleaf.templatewriter.XhtmlHtml5TemplateWriter;

import com.connect_group.thymesheet.impl.ThymesheetTemplateModeHandler;

public class ThymesheetLegacyHtml5TemplateModeHandler extends ThymesheetTemplateModeHandler {

	public static final String MODENAME = "THYMESHEET_LEGACYHTML5";
	
	public ThymesheetLegacyHtml5TemplateModeHandler() {
		super(MODENAME, new LegacyHtml5TemplateParser(MODENAME, getPoolSize()), new XhtmlHtml5TemplateWriter());
	}

}
