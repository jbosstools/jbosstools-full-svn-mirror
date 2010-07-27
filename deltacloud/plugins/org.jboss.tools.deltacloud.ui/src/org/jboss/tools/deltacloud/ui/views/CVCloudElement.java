package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.views.properties.IPropertySource;

public class CVCloudElement extends CloudViewElement {

	private Viewer viewer;
	
	public CVCloudElement(Object element, String name, Viewer viewer) {
		super(element, name);
		this.viewer = viewer;
	}

	public Viewer getViewer() {
		return viewer;
	}
	
	@Override
	public IPropertySource getPropertySource() {
		return new CloudPropertySource(getElement());
	}

}
