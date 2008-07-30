package org.jboss.tools.flow.common.core;

import java.util.HashMap;

public class DefaultConnection implements Connection {
	
	private HashMap<String, Object> metaData = new HashMap<String, Object>();
	private Node from, to;

	public DefaultConnection(Node from, Node to) {
		this.from = from;
		this.to = to;
	}

	public Object getMetaData(String key) {
		return metaData.get(key);
	}

	public void setMetaData(String key, Object value) {
		metaData.put(key, value);
	}
	
	public Node getFrom() {
		return from;
	}
	
	public Node getTo() {
		return to;
	}

	public String getFromType() {
		return null;
	}

	public String getToType() {
		return null;
	}

}
