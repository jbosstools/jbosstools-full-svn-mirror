package org.jboss.tools.smooks.ui.gef.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AbstractStructuredDataConnectionModel implements
		PropertyChangeListener {
	public static final String CONNECTION_PROPERTY_REMOVE = "__connection_property_remove";

	public static final String CONNECTION_PROPERTY_UPDATE = "__connection_property_update";

	public static final String CONNECTION_PROPERTY_ADD = "__connection_property_add";
	protected IConnectableModel source;
	protected IConnectableModel target;
	protected List<PropertyModel> properties = new ArrayList<PropertyModel>();

	private PropertyChangeSupport support = new PropertyChangeSupport(this);

	public AbstractStructuredDataConnectionModel(IConnectableModel source,
			IConnectableModel target) {
		this();
		this.setSource(source);
		this.setTarget(target);
		attachSource();
		attachTarget();
	}

	// public void updateAndAddProperty(String propertyName,Object value){
	// for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
	// PropertyModel property = (PropertyModel) iterator.next();
	// if(property.getName().equals(propertyName)){
	// property.setValue(value);
	// return;
	// }
	// }
	//		
	// PropertyModel model = new PropertyModel(propertyName,value);
	// properties.add(model);
	// }

	public Object getProperty(String propertyName) {
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			PropertyModel property = (PropertyModel) iterator.next();
			if (property.getName().equals(propertyName)) {
				return property.getValue();
			}
		}
		return null;
	}

	public AbstractStructuredDataConnectionModel() {
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	/**
	 */
	public void attachSource() {
		if (!source.getModelSourceConnections().contains(this)) {
			source.addSourceConnection(this);
		}
	}

	/**
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

	public Object[] getPropertyArray() {
		return properties.toArray();
	}

	public List<PropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyModel> properties) {
		this.properties = properties;
	}
	
	public void addPropertyModel(String name,Object value){
		addPropertyModel(new PropertyModel(name,value));
	}

	public void addPropertyModel(PropertyModel property) {
		if (properties != null) {
			boolean updated = false;
			for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
				PropertyModel p = (PropertyModel) iterator.next();
				if (p.getName().equals(property.getName())) {
					p.setValue(property.getValue());
					updated = true;
				}
			}
			if (!updated) {
				this.getProperties().add(property);
				property.addPropertyChangeListener(this);
				support.firePropertyChange(CONNECTION_PROPERTY_ADD, null,
						property);
				return;
			}
			support.firePropertyChange(CONNECTION_PROPERTY_UPDATE, null,
					property);
		}
	}

	public void removePropertyModel(String propertyName) {
		List<PropertyModel> list = this.getProperties();
		for (Iterator<PropertyModel> iterator = list.iterator(); iterator
				.hasNext();) {
			PropertyModel propertyModel = (PropertyModel) iterator.next();
			if (propertyModel.getName().equals(propertyName)) {
				list.remove(propertyModel);
				support.firePropertyChange(CONNECTION_PROPERTY_REMOVE,
						propertyModel, null);
				break;
			}
		}

	}

	public void removePropertyModel(PropertyModel property) {
		property.removePropertyChangeListener(this);
		this.getProperties().remove(property);
		support.firePropertyChange(CONNECTION_PROPERTY_REMOVE, property, null);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String name = evt.getPropertyName();
		support.firePropertyChange(evt);
	}
}
