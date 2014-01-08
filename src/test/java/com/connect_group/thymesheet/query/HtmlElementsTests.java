package com.connect_group.thymesheet.query;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.thymeleaf.dom.Comment;
import org.thymeleaf.dom.Element;

import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.connect_group.thymesheet.query.HtmlElements;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.junit.Assert.*;

public class HtmlElementsTests {

	@Test
	public void shouldAddAllElementsOfACollection_WhenConstructedUsingACollection() throws NodeSelectorException {
		Element[] testArray = new Element[] {
				new Element("banana"),
				new Element("apple")
		};
		
		HtmlElements tags = new HtmlElements(Arrays.asList(testArray));
		assertThat(tags.get(0).tagName(), is("banana"));
		assertThat(tags.get(1).tagName(), is("apple"));
	}
	
	@Test
	public void shouldReturnEmptyElementsObject_WhenMatchingCriteriaNotMet() throws NodeSelectorException {
		HtmlElements tags = getSimpleElements();
		assertThat(tags.matching("banana"), instanceOf(HtmlElements.class));
		assertThat(tags.matching("banana").size(), is(0));
	}
	
	@Test
	public void shouldImplementCollectionOfTag() {
		@SuppressWarnings("unused")
		Collection<HtmlElement> collection = new HtmlElements();
	}
	
	@Test
	public void shouldReturnNthElement_WhenElementAtIndexIsRequested() {
		HtmlElements tags = getSimpleElements();
		assertThat(tags.get(0).tagName(), is("body"));
		
	}
	
	@Test
	public void shouldReturnExpectedElements_WhenMatchingCriteriaOnDocument() throws NodeSelectorException {
		HtmlElements tags = getSimpleElements();
		
		assertThat(tags.matching("p").size(), is(1));
		assertThat(tags.matching("p").get(0).tagName(), is("p"));
	}

	@Test
	public void shouldReturnExpectedElements_WhenMatchingCriteriaOnElement() throws NodeSelectorException {
		Element p = new Element("p");
		p.addChild(new Element("a"));
		HtmlElements tags = new HtmlElements(p);
		
		assertThat(tags.matching("a").size(), is(1));
		assertThat(tags.matching("a").get(0).tagName(), is("a"));
	}

	
	private HtmlElements getSimpleElements() {
		Element h1=new Element("h1");
		Element p=new Element("p");
		Element body=new Element("body");
		Comment banana=new Comment("banana");
		
		body.addChild(h1);
		body.addChild(p);
		body.addChild(banana);
		HtmlElements elements = new HtmlElements(body);
		return elements;
	}
	
}
