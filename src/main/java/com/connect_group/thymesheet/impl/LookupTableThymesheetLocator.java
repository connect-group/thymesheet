package com.connect_group.thymesheet.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.thymeleaf.dom.Document;

import com.connect_group.thymesheet.ThymesheetLocator;

public class LookupTableThymesheetLocator implements ThymesheetLocator {

	Map<? extends Object, ? extends Object> lookup = null;
	
	public LookupTableThymesheetLocator() {}
	
	public LookupTableThymesheetLocator(Map<String,String> lookupTable) {
		this.lookup = lookupTable;
	}
	
	public LookupTableThymesheetLocator(Properties lookupTable) {
		this.lookup = lookupTable;
	}

	
	public List<String> getThymesheetPaths(Document document) {
		
		if(lookup==null || lookup.isEmpty()) {
			return Collections.emptyList();
		}
		
		String name=document.getDocumentName();
		
		Object value = lookup.get(name);
		
		return asList(value);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<String> asList(Object value) {
		if(value instanceof String) {
			return Collections.singletonList((String)value);
		}
		
		if(value instanceof List && !((List) value).isEmpty() && ((List) value).get(0) instanceof String) {
			return (List<String>)value;
		}
		
		if(value instanceof Iterable) {
			return asList((Iterable)value);
		}
		
		if(value!=null) {
			return Collections.singletonList(value.toString());
		}
		
		return Collections.emptyList();
	}
	
	private List<String> asList(Iterable<?> collection) {
		ArrayList<String> result = new ArrayList<String>();
		for(Object o : collection) {
			if(o instanceof String) {
				result.add((String)o);
			} else if(o!=null) {
				result.add(o.toString());
			}
			
		}
		return result;
	}

	public void removeThymesheetLinks(Document document) {}

}
