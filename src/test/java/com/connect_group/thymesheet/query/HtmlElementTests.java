package com.connect_group.thymesheet.query;

import org.junit.Test;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Text;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class HtmlElementTests {

	@Test
	public void shouldReturnText_WhenElementHasTextChild() {
		Element el = new Element("p");
		el.addChild(new Text("Hello"));
		
		assertThat(new HtmlElement(el).text(), is("Hello"));
	}
	
	@Test
	public void shouldReturnText_WhenNestedElementHasTextChild() {
		Element el = new Element("p");
		Element span = new Element("span");
		span.addChild(new Text("Hello"));
		el.addChild(span);
		
		assertThat(new HtmlElement(el).text(), is("Hello"));
	}
	
	@Test
	public void shouldReturnText_WhenElementAndNestedElementHasTextChild() {
		Element el = new Element("p");
		el.addChild(new Text("Hello"));
		Element span = new Element("span");
		span.addChild(new Text("Goodbye"));
		el.addChild(span);
		
		assertThat(new HtmlElement(el).text(), is("HelloGoodbye"));
	}
	
	@Test
	public void shouldReturnStringContainingExpectedHtml_WhenHtmlRequested() {
		Element el = new Element("p");
		el.addChild(new Text("Hello"));
		Element span = new Element("span");
		span.addChild(new Text("Goodbye"));
		el.addChild(span);
		
		assertThat(new HtmlElement(el).html(), is("<p>Hello<span>Goodbye</span></p>"));
		
	}


}
