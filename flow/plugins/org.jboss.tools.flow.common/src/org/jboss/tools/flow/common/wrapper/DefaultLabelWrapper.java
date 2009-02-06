package org.jboss.tools.flow.common.wrapper;

import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.properties.IPropertyId;

public class DefaultLabelWrapper extends AbstractLabelWrapper {
	
	private IPropertySource propertySource;
	
	public DefaultLabelWrapper(Wrapper owner) {
		if (owner != null) {
			propertySource = (IPropertySource)owner.getAdapter(IPropertySource.class);
			setElement(owner.getElement());
		}
	}
	
	public void setText(String text) {
		if (propertySource != null) {
			propertySource.setPropertyValue(IPropertyId.LABEL, text);
			notifyListeners(CHANGE_VISUAL, text);
		}
	}
	
	public String getText() {
		String result = null;
		if (propertySource != null) {
			result = (String)propertySource.getPropertyValue(IPropertyId.LABEL);
		}
		return result;
	}
	
    @SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
    	if (adapter == IPropertySource.class && propertySource != null) {
    		return propertySource;
    	}
    	return super.getAdapter(adapter);
    }
    
	
}
