package com.connect_group.thymesheet;

import java.util.List;

import org.thymeleaf.dom.Document;

public interface ThymesheetLocator {
	List<String> getThymesheetPaths(Document document);
	void removeThymesheetLinks(Document document);
}
