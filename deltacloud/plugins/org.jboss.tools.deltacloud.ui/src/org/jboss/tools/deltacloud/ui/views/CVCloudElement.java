/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;

public class CVCloudElement extends CloudViewElement {

	private static final String INSTANCE_CATEGORY_NAME = "InstanceCategoryName"; //$NON-NLS-1$
	private static final String IMAGE_CATEGORY_NAME = "ImageCategoryName"; //$NON-NLS-1$

	private Viewer viewer;
	private boolean initialized;
	
	public CVCloudElement(Object element, String name, Viewer viewer) {
		super(element, name);
		this.viewer = viewer;
	}

	public Viewer getViewer() {
		return viewer;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}
	
	@Override
	public Object[] getChildren() {
		if (!initialized) {
			DeltaCloud cloud = (DeltaCloud)getElement();
			CVCategoryElement c1 = new CVInstancesCategoryElement(cloud, CVMessages.getString(INSTANCE_CATEGORY_NAME),
					viewer);
			CVCategoryElement c2 = new CVImagesCategoryElement(cloud, CVMessages.getString(IMAGE_CATEGORY_NAME),
					viewer);
			addChild(c1);
			addChild(c2);
		}
		initialized = true;
		return super.getChildren();
	}
	
	@Override
	public IPropertySource getPropertySource() {
		return new CloudPropertySource(getElement());
	}

	public void loadChildren() {
		DeltaCloud cloud = (DeltaCloud)getElement();
		cloud.loadChildren();
	}
}
