package org.jboss.tools.smooks.ui.gef.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class AbstractStructuredDataConnectionModel implements PropertyChangeListener {
	public static final String CONNECTION_PROPERTY_CHANGE = "__connection_property_change";
	protected IConnectableModel source;
	protected IConnectableModel target;
	protected List<PropertyModel> properties = new ArrayList<PropertyModel>();
	
	private PropertyChangeSupport support =new PropertyChangeSupport(this);
	
	public AbstractStructuredDataConnectionModel(IConnectableModel source,
			IConnectableModel target) {
		this();
		this.setSource(source);
		this.setTarget(target);
		attachSource();
		attachTarget();
	}
	
	public AbstractStructuredDataConnectionModel(){
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener){
		support.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener){
		support.removePropertyChangeListener(listener);
	}

	/**
	 * t�ӵ���ʼ����ӵ�source
	 */
	public void attachSource() {
		if (!source.getModelSourceConnections().contains(this)) {
			source.addSourceConnection(this);
		}
	}

	/**
	 * t�ӵ�β����ӵ�target
	 */
	public void attachTarget() {
		if (!target.getModelTargetConnections().contains(this)) {
			target.addTargetConnection(this);
		}
	}

	public void detachSource() {
		source.removeSourceConnection(this);
	}

	public void detachTarget() {
		target.removeTargetConnection(this);
	}

	public IConnectableModel getSource() {
		return source;
	}

	public void setSource(IConnectableModel source) {
		this.source = source;
	}

	public IConnectableModel getTarget() {
		return target;
	}

	public void setTarget(IConnectableModel target) {
		this.target = target;
	}
	
	public Object[] getPropertyArray(){
		return  properties.toArray();
	}

	protected List<PropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyModel> properties) {
		this.properties = properties;
	}
	
	public void addPropertyModel(PropertyModel property){
		this.getProperties().add(property);
		property.addPropertyChangeListener(this);
		support.firePropertyChange(CONNECTION_PROPERTY_CHANGE, null, property);
	}
	
	public void removePropertyModel(PropertyModel property){
		property.removePropertyChangeListener(this);
		this.getProperties().remove(property);
		support.firePropertyChange(CONNECTION_PROPERTY_CHANGE, property, null);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		if(CONNECTION_PROPERTY_CHANGE.equals(name)){
			support.firePropertyChange(evt);
		}
	}
}
