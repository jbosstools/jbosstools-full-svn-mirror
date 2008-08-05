package org.jboss.tools.flow.common.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultNode implements Node {
	
	private long id;
	private String name;
	private Container container;
	
	private HashMap<String, Object> metaData = new HashMap<String, Object>();

	public long getId() {
		return id;
	}

	public void setId(long l) {
		id = l;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMetaData(String key, Object value) {
		metaData.put(key, value);
	}

	public Object getMetaData(String key) {
		return metaData.get(key);
	}

	public Container getNodeContainer() {
		return container;
	}
	
	public void setNodeContainer(Container container) {
		this.container = container;
	}

	public void addIncomingConnection(String type, Connection connection) {
		// TODO Auto-generated method stub
		
	}

	public void addOutgoingConnection(String type, Connection connection) {
		// TODO Auto-generated method stub
		
	}

	public Map<String, List<Connection>> getIncomingConnections() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Connection> getIncomingConnections(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, List<Connection>> getOutgoingConnections() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Connection> getOutgoingConnections(String type) {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeIncomingConnection(String type, Connection connection) {
		// TODO Auto-generated method stub
		
	}

	public void removeOutgoingConnection(String type, Connection connection) {
		// TODO Auto-generated method stub
		
	}

}
