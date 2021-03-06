/*
 * =============================================================================
 *
 *   Copyright (c) 2013, Connect Group (http://www.connect-group.com)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package com.connect_group.thymesheet.templatemode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.thymeleaf.templatemode.ITemplateModeHandler;
import org.thymeleaf.templateparser.html.LegacyHtml5TemplateParser;
import org.thymeleaf.templateparser.xmlsax.XhtmlAndHtml5NonValidatingSAXTemplateParser;
import org.thymeleaf.templateparser.xmlsax.XhtmlValidatingSAXTemplateParser;
import org.thymeleaf.templateparser.xmlsax.XmlNonValidatingSAXTemplateParser;
import org.thymeleaf.templateparser.xmlsax.XmlValidatingSAXTemplateParser;
import org.thymeleaf.templatewriter.XhtmlHtml5TemplateWriter;
import org.thymeleaf.templatewriter.XmlTemplateWriter;

import com.connect_group.thymesheet.impl.HtmlThymesheetLocator;
import com.connect_group.thymesheet.impl.LookupTableThymesheetLocator;

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
        
        Properties props = null;
        InputStream is = ThymesheetStandardTemplateModeHandlers.class.getResourceAsStream("/thymesheet.properties");
        if(is!=null) {
        	props = new Properties();
        	try {
				props.load(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        LookupTableThymesheetLocator lookupLocator = new LookupTableThymesheetLocator(props);
        HtmlThymesheetLocator htmlLocator = new HtmlThymesheetLocator(props);
        
        XML = new ThymesheetTemplateModeHandler(
                "XML",
                new XmlNonValidatingSAXTemplateParser(poolSize),
                new XmlTemplateWriter(),
                lookupLocator);
        VALIDXML = new ThymesheetTemplateModeHandler(
                "VALIDXML", 
                new XmlValidatingSAXTemplateParser(poolSize),
                new XmlTemplateWriter(),
                lookupLocator);
        XHTML = new ThymesheetTemplateModeHandler(
                "XHTML", 
                new XhtmlAndHtml5NonValidatingSAXTemplateParser(poolSize),
                new XhtmlHtml5TemplateWriter(),
                htmlLocator);
        VALIDXHTML = new ThymesheetTemplateModeHandler(
                "VALIDXHTML", 
                new XhtmlValidatingSAXTemplateParser(poolSize),
                new XhtmlHtml5TemplateWriter(),
                htmlLocator);
        HTML5 = new ThymesheetTemplateModeHandler(
                "HTML5", 
                new XhtmlAndHtml5NonValidatingSAXTemplateParser(poolSize),
                new XhtmlHtml5TemplateWriter(),
                htmlLocator);
        LEGACYHTML5 = new ThymesheetTemplateModeHandler(
                "LEGACYHTML5", 
                new LegacyHtml5TemplateParser("LEGACYHTML5", poolSize),
                new XhtmlHtml5TemplateWriter(),
                htmlLocator);
        
        ALL_TEMPLATE_MODE_HANDLERS =
                new HashSet<ITemplateModeHandler>(
                        Arrays.asList(
                                new ITemplateModeHandler[] { XML, VALIDXML, XHTML, VALIDXHTML, HTML5, LEGACYHTML5 }));
        
    }

}
