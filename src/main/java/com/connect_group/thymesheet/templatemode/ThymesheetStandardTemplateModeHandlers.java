package com.connect_group.thymesheet.templatemode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templatemode.TemplateModeHandler;
import org.thymeleaf.templateparser.html.LegacyHtml5TemplateParser;
import org.thymeleaf.templateparser.xmlsax.XhtmlAndHtml5NonValidatingSAXTemplateParser;
import org.thymeleaf.templateparser.xmlsax.XhtmlValidatingSAXTemplateParser;
import org.thymeleaf.templateparser.xmlsax.XmlNonValidatingSAXTemplateParser;
import org.thymeleaf.templateparser.xmlsax.XmlValidatingSAXTemplateParser;
import org.thymeleaf.templatewriter.XhtmlHtml5TemplateWriter;
import org.thymeleaf.templatewriter.XmlTemplateWriter;

public class ThymesheetStandardTemplateModeHandlers {
	private static final int MAX_PARSERS_POOL_SIZE = 24;
	
    public static final ITemplateModeHandler XML;
    public static final ITemplateModeHandler VALIDXML;
    public static final IThymesheetTemplateModeHandler XHTML;
    public static final IThymesheetTemplateModeHandler VALIDXHTML;
    public static final IThymesheetTemplateModeHandler HTML5;
    public static final IThymesheetTemplateModeHandler LEGACYHTML5;

    public static final Set<ITemplateModeHandler> ALL_TEMPLATE_MODE_HANDLERS;
    
	private ThymesheetStandardTemplateModeHandlers() {
        super();
    }
	
    static {

        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        final int poolSize = 
                Math.min(
                        (availableProcessors <= 2? availableProcessors : availableProcessors - 1),
                        MAX_PARSERS_POOL_SIZE);
        
        XML = new TemplateModeHandler(
                "XML",
                new XmlNonValidatingSAXTemplateParser(poolSize),
                new XmlTemplateWriter());
        VALIDXML = new TemplateModeHandler(
                "VALIDXML", 
                new XmlValidatingSAXTemplateParser(poolSize),
                new XmlTemplateWriter());
        XHTML = new ThymesheetTemplateModeHandler(
                "XHTML", 
                new XhtmlAndHtml5NonValidatingSAXTemplateParser(poolSize),
                new XhtmlHtml5TemplateWriter());
        VALIDXHTML = new ThymesheetTemplateModeHandler(
                "VALIDXHTML", 
                new XhtmlValidatingSAXTemplateParser(poolSize),
                new XhtmlHtml5TemplateWriter());
        HTML5 = new ThymesheetTemplateModeHandler(
                "HTML5", 
                new XhtmlAndHtml5NonValidatingSAXTemplateParser(poolSize),
                new XhtmlHtml5TemplateWriter());
        LEGACYHTML5 = new ThymesheetTemplateModeHandler(
                "LEGACYHTML5", 
                new LegacyHtml5TemplateParser("LEGACYHTML5", poolSize),
                new XhtmlHtml5TemplateWriter());
        
        ALL_TEMPLATE_MODE_HANDLERS =
                new HashSet<ITemplateModeHandler>(
                        Arrays.asList(
                                new ITemplateModeHandler[] { XML, VALIDXML, XHTML, VALIDXHTML, HTML5, LEGACYHTML5 }));
        
    }

}
