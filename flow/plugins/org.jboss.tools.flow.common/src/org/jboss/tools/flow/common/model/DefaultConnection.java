package org.jboss.tools.flow.common.model;

import java.util.HashMap;

public class DefaultConnection implements Connection {
	
	private HashMap<String, Object> metaData = new HashMap<String, Object>();
	private Node from, to;
	
	public DefaultConnection() {
		this(null, null);
	}

	public DefaultConnection(Node from, Node to) {
		setFrom(from);
		setTo(to);
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
	
	public void setFrom(Node node) {
		if (from != null) {
			from.removeOutgoingConnection(null, this);
		}
		from = node;
		if (from != null) {
			from.addOutgoingConnection(null, this);
		}
	}
	
	public Node getTo() {
		return to;
	}
	
	public void setTo(Node node) {
		if (to != null) {
			to.removeIncomingConnection(null, this);
		}
		to = node;
		if (to != null) {
			to.addIncomingConnection(null, this);
		}
	}

	public String getFromType() {
		return null;
	}

	public String getToType() {
		return null;
	}

}
