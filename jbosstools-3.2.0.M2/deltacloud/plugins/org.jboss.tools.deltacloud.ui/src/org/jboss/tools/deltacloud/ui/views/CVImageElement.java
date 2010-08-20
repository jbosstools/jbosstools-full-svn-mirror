package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.ui.views.properties.IPropertySource;

public class CVImageElement extends CloudViewElement {

	public CVImageElement(Object element, String name) {
		super(element, name);
	}

	@Override
	public IPropertySource getPropertySource() {
		return new ImagePropertySource(getElement());
	}

}
