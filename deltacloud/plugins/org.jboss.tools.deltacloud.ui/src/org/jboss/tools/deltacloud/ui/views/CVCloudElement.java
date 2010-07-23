package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.ui.views.properties.IPropertySource;

public class CVCloudElement extends CloudViewElement {

	public CVCloudElement(Object element, String name) {
		super(element, name);
	}

	@Override
	public IPropertySource getPropertySource() {
		return new CloudPropertySource(getElement());
	}

}
