package org.jboss.tools.common.properties;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.jboss.tools.flow.common.wrapper.DefaultNodeWrapper;

public class DefaultNodePropertySource implements IPropertySource {
	
	private static final String NAME = "org.jboss.tools.flow.common.model.DefaultNode.name";

	private DefaultNodeWrapper wrapper = null;
	private IPropertyDescriptor[] propertyDescriptors;
	
	public DefaultNodePropertySource(DefaultNodeWrapper wrapper) {
		this.wrapper = wrapper;
	}
	public Object getEditableValue() {
		return null;
	}
	public IPropertyDescriptor[] getPropertyDescriptors() {
		if (propertyDescriptors == null) {
			propertyDescriptors = new IPropertyDescriptor[] {
				new TextPropertyDescriptor(NAME, "Name") {}
			};
		}
		return propertyDescriptors;
	}
	public Object getPropertyValue(Object id) {
		if (NAME.equals(id)) {
			return wrapper.getName() != null ? wrapper.getName() : "";
		}
		return null;
	}
	public boolean isPropertySet(Object id) {
		if (NAME.equals(id)) {
			return wrapper.getName() != null;
		}
		return false;
	}
	public void resetPropertyValue(Object id) {
	}
	public void setPropertyValue(Object id, Object value) {
		if (NAME.equals(id)) {
			if (value instanceof String) {
				wrapper.setName((String)value);
			}
		}
	}
}
