package org.jboss.tools.flow.common.properties;

import org.jboss.tools.flow.common.wrapper.DefaultLabelWrapper;

public class DefaultLabelWrapperPropertySource extends WrapperPropertySource implements IPropertyId {
	
	private DefaultLabelWrapper wrapper = null;
	
	public DefaultLabelWrapperPropertySource(DefaultLabelWrapper wrapper) {
		super(wrapper);
		this.wrapper = wrapper;
	}
	
	public void setPropertyValue(Object id, Object value) {
		if (NAME.equals(id)) {
			if (value instanceof String) {
				wrapper.setText((String)value);
			}
		} 
		super.setPropertyValue(id, value);
	}
}
