package org.jboss.tools.flow.common.model;

import java.util.HashMap;

public class DefaultLabel implements Label {
	
	private String text;
	
	private HashMap<String, Object> metaData = new HashMap<String, Object>();

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setMetaData(String key, Object value) {
		metaData.put(key, value);
	}

	public Object getMetaData(String key) {
		return metaData.get(key);
	}

}
