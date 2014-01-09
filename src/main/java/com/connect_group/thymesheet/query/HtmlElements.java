package com.connect_group.thymesheet.query;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.thymeleaf.dom.Document;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;

import com.connect_group.thymesheet.css.selectors.NodeSelectorException;
import com.connect_group.thymesheet.css.selectors.dom.DOMNodeSelector;

public class HtmlElements extends LinkedHashSet<HtmlElement> {

	private static final long serialVersionUID = 284598876009291447L;

	public HtmlElements() {}
	
	public HtmlElements(Document document) {
		HtmlElement documentPseudoElement = new HtmlElement(new Element("#document"));
		List<Element> rootElements = document.getElementChildren();
		for(Element child : rootElements) {
			documentPseudoElement.getElement().addChild(child);
		}
		this.add(documentPseudoElement);

	}
	public HtmlElements(Element element) {
		this.add(new HtmlElement(element));
	}

	public HtmlElements(Collection<Element> elements) {
		for(Element e : elements) {
			this.add(new HtmlElement(e));
		}
	}
	
	public HtmlElements matching(String criteria) throws NodeSelectorException {
		HtmlElements matchedElements = new HtmlElements();
		
		for(HtmlElement tag : this) {
			DOMNodeSelector selector = new DOMNodeSelector(tag.getElement());
			Set<Node> nodes = selector.querySelectorAll(criteria);
			for(Node node : nodes) {
				matchedElements.add(new HtmlElement((Element)node));
			}
		}
		
		return matchedElements;
	}

	public HtmlElement get(int index) {
		if(index>=size() || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
		}
		
		Iterator<HtmlElement> it = iterator();
		while(index>0) {
			it.next();
			index--;
		}
		
		return it.next();
	}
	
	public String text() {
		StringBuilder builder = new StringBuilder();
		for(HtmlElement tag : this) {
			builder.append(tag.text());
		}
		return builder.toString();
	}

	public String html() {
		StringBuilder builder = new StringBuilder();
		for(HtmlElement tag : this) {
			builder.append(tag.html());
		}
		return builder.toString();
	}
	
	@Override
	public String toString() {
		return html();
	}
	
	// assertThat(elements.matching("span").size(), is(2));
	// assertThat(elements.matching("h4 span[data-alt]").text(), is("hello"));
	// Hamcrest:
	//      hasAttr("")  --> Applies to Element.
	

}
