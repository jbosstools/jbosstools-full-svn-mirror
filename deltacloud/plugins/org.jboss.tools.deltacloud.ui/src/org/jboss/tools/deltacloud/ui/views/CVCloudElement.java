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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CVCloudElement extends CloudViewElement {

	private TreeViewer viewer;

	public CVCloudElement(Object element, String name, TreeViewer viewer) {
		super(element, viewer);
		this.viewer = viewer;
	}

	public String getName() {
		Object element = getElement();
		if (element instanceof DeltaCloud) {
			return ((DeltaCloud) element).getName();
		} else {
			return "";
		}
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public synchronized Object[] getChildren() {
		if (!initialized.get()) {
			DeltaCloud cloud = (DeltaCloud) getElement();
			CVCloudElementCategoryElement instances = new CVInstancesCategoryElement(cloud, viewer);
			addChild(instances);
			CVCloudElementCategoryElement images = new CVImagesCategoryElement(cloud, viewer);
			addChild(images);
		}
		initialized.set(true);
		return super.getChildren();
	}

	@Override
	public IPropertySource getPropertySource() {
		return new CloudPropertySource(getElement());
	}
}
