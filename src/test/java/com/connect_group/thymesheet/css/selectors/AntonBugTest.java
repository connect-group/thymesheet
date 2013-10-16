package com.connect_group.thymesheet.css.selectors;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Node;
import org.thymeleaf.messageresolver.StandardMessageResolver;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateparser.xmlsax.XmlNonValidatingSAXTemplateParser;
import org.thymeleaf.templateresolver.UrlTemplateResolver;

import com.connect_group.thymesheet.css.selectors.dom.DOMNodeSelector;

public class AntonBugTest {
    
    private final DOMNodeSelector nodeSelector;
    
    public AntonBugTest() throws Exception {
    	
    	XmlNonValidatingSAXTemplateParser parser = new XmlNonValidatingSAXTemplateParser(1);
    	Reader reader = new InputStreamReader(getClass().getResourceAsStream("/selector/anton-bug.xml"));
    	Configuration configuration = new Configuration();
    	configuration.setTemplateResolver(new UrlTemplateResolver());
    	configuration.setMessageResolver(new StandardMessageResolver());
    	configuration.setDefaultTemplateModeHandlers(StandardTemplateModeHandlers.ALL_TEMPLATE_MODE_HANDLERS);
    	configuration.initialize();
    	Document document = parser.parseTemplate(configuration, "anton-bug", reader);
        nodeSelector = new DOMNodeSelector(document);
    }
    
    @Test
    public void checkAdjacentSiblings() throws Exception {
        Set<Node> result = nodeSelector.querySelectorAll("token[tag^=l] + token");
        Assert.assertEquals(3, result.size());
    }
    
    @Test
    public void checkGeneralSiblings() throws Exception {
        Set<Node> result = nodeSelector.querySelectorAll("token[tag^=l] ~ token");
        Assert.assertEquals(6, result.size());
    }
    
}
