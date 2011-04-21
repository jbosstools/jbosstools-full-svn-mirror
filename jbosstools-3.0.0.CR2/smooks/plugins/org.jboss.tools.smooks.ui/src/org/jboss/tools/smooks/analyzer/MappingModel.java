/**
 * 
 */
package org.jboss.tools.smooks.analyzer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.tools.smooks.ui.gef.model.PropertyModel;

/**
 * @author Dart
 * 
 */
public class MappingModel {
	protected Object source;
	protected Object target;

	protected List<PropertyModel> properties = new ArrayList<PropertyModel>();

	public MappingModel(Object source, Object target) {
		setSource(source);
		setTarget(target);
	}
	
	public Object getPropertyValue(String name){
		for (Iterator iterator = properties.iterator(); iterator.hasNext();) {
			PropertyModel property = (PropertyModel) iterator.next();
			if(name.equals(property.getName())){
				return property.getValue();
			}
		}
		return null;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof MappingModel)) {
			return false;
		}

		Object es = ((MappingModel) obj).getSource();
		Object et = ((MappingModel) obj).getTarget();

		if (source != null && target != null) {
			return (source == es && target == et );
		} else {
			return false;
		}
	}
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if (source != null)
			buffer.append(Messages.getString("MappingModel.Source") + source); //$NON-NLS-1$
		else
			buffer.append(Messages.getString("MappingModel.SourceIsNull")); //$NON-NLS-1$
		if (target != null)
			buffer.append(Messages.getString("MappingModel.Target") + target); //$NON-NLS-1$
		else
			buffer.append(Messages.getString("MappingModel.TargetIsNull")); //$NON-NLS-1$
		return buffer.toString();
	}

	public List<PropertyModel> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyModel> properties) {
		this.properties = properties;
	}
}
