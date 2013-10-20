package com.connect_group.thymesheet.templatemode;

import org.thymeleaf.templatemode.ITemplateModeHandler;

import com.connect_group.thymesheet.ServletContextURLFactory;

public interface IThymesheetTemplateModeHandler extends ITemplateModeHandler {
	ServletContextURLFactory getUrlFactory();
	void setUrlFactory(ServletContextURLFactory urlContainer);
}
