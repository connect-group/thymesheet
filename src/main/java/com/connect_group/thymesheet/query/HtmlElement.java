package com.connect_group.thymesheet.query;

import java.util.List;

import org.thymeleaf.dom.CDATASection;
import org.thymeleaf.dom.Comment;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;

public class HtmlElement {
	private final Element element;
	
	public HtmlElement(Element element) {
		this.element=element;
	}
	
	public Element getElement() {
		return element;
	}
	
	public String tagName() {
		return element.getNormalizedName();
	}
	
	public String attr(String attributeName) {
		return element.getAttributeValue(attributeName);
	}
	
	public String cssClass() {
		return element.getAttributeValue("class");
	}
	
	public String text() {
		return text(element);
	}
	
	private static String text(Element el) {
		StringBuilder text = new StringBuilder();
		
		List<Node> nodes = el.getChildren();
		for(Node node : nodes) {
			String str = null;
			if(node instanceof Element) {
				str = text((Element)node);
			}
			else if(node instanceof Text) {
				str = ((Text)node).getContent();
			}
			
			if(str!=null) {
				
				str = str.replaceAll("\\s+", " ");
				text.append(str);
			}
		}
		
		return text.toString();
	}

	public String html() {
		return htmlFor(element, new StringBuilder()).toString();
	}
	
	private static StringBuilder htmlFor(Element element, StringBuilder builder) {
		startTag(element, builder);
		
		for(Node node : element.getChildren()) {
			if(node instanceof Element) {
				builder = htmlFor((Element)node, builder);
			} else if(node instanceof Text) {
				builder.append(((Text)node).getContent());
			} else if(node instanceof Comment) {
				builder.append("<!--");
				builder.append(((Comment)node).getContent());
				builder.append("-->");
			} else if(node instanceof CDATASection) {
				builder.append("<![CDATA[");
				builder.append(((CDATASection)node).getContent());
				builder.append("]]>");
			}
		}
		
		endTag(element, builder);
		
		return builder;
	}
	
	private static void startTag(Element element, StringBuilder builder) {
		builder.append("<");
		builder.append(element.getNormalizedName());
		
		for(String key : element.getAttributeMap().keySet()) {
			builder.append(" ");
			builder.append(key).append("=\"");
			builder.append(element.getAttributeValue(key));
			builder.append("\"");
		}
		builder.append(">");
	}
	
	private static void endTag(Element element, StringBuilder builder) {
		builder.append("</").append(element.getNormalizedName()).append(">");
	}
	
	@Override
	public String toString() {
		return html();
	}
	
}
