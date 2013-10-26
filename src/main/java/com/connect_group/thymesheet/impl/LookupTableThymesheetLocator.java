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
