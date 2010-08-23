package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.ui.views.properties.IPropertySource;

public abstract class CVCategoryElement extends CloudViewElement {

	public final static int INSTANCES = 0;
	public final static int IMAGES = 1;
	
	protected int type;
	protected boolean initialized;
	
	public CVCategoryElement(Object element, String name, int type) {
		super(element, name);
		this.type = type;
	}

	@Override
	public Object[] getChildren() {
		return super.getChildren();
	}
	
	@Override
	public boolean hasChildren() {
		return true;
	}
	
	@Override
	public IPropertySource getPropertySource() {
		return null;
	}

}
