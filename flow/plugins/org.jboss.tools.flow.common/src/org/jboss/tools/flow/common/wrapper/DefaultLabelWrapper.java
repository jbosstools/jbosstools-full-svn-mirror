package org.jboss.tools.flow.common.wrapper;

import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.properties.IPropertyId;

public class DefaultLabelWrapper extends AbstractLabelWrapper {
	
	private IPropertySource propertySource;
	
	public DefaultLabelWrapper(Element element) {
		if (element != null) {
			propertySource = (IPropertySource)element.getMetaData("propertySource");
		}
	}
	
	public void setText(String text) {
		if (propertySource != null) {
			propertySource.setPropertyValue(IPropertyId.LABEL, text);
		}
	}
	
	public String getText() {
		String result = null;
		if (propertySource != null) {
			result = (String)propertySource.getPropertyValue(IPropertyId.LABEL);
		}
		return result;
	}
	
}
