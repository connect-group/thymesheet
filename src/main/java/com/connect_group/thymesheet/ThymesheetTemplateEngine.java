package com.connect_group.thymesheet;

import org.thymeleaf.TemplateEngine;

import com.connect_group.thymesheet.templatemode.ThymesheetStandardTemplateModeHandlers;

public class ThymesheetTemplateEngine extends TemplateEngine {

	public ThymesheetTemplateEngine() {
		super();
		setDefaultTemplateModeHandlers(ThymesheetStandardTemplateModeHandlers.ALL_TEMPLATE_MODE_HANDLERS);
	}
}
