/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.ui.views.cloud;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * A tree element that shows the user that the tree is currently loading
 * elements
 */
public class LoadingItem extends DeltaCloudViewItem<Object> {

	protected LoadingItem(DeltaCloudViewItem<?> parent, TreeViewer viewer) {
		super(null, parent, viewer);
	}

	@Override
	public IPropertySource getPropertySource() {
		// no property source for this element
		return null;
	}

	@Override
	public String getName() {
		// TODO: internationalize strings
		return "Loading...";
	}

	@Override
	protected void addPropertyChangeListener(Object object) {
		// do nothing
	}
}
