package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.ui.views.properties.IPropertySource;

public class CVInstanceElement extends CloudViewElement {

	public CVInstanceElement(Object element, String name) {
		super(element, name);
	}
	
	@Override
	public IPropertySource getPropertySource() {
		return new InstancePropertySource(this, getElement());
	}

}
