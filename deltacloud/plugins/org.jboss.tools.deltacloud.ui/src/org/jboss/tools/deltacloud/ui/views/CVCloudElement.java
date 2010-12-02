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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.Activator;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CVCloudElement extends CloudViewElement {

	private TreeViewer viewer;
	private boolean initialized;

	public CVCloudElement(Object element, String name, TreeViewer viewer) {
		super(element);
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
	
	public Viewer getViewer() {
		return viewer;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public synchronized Object[] getChildren() {
		if (!initialized) {
			DeltaCloud cloud = (DeltaCloud) getElement();
			CVCategoryElement c1 = new CVInstancesCategoryElement(cloud, viewer);
			CVCategoryElement c2 = new CVImagesCategoryElement(cloud, viewer);
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
		DeltaCloud cloud = (DeltaCloud) getElement();
		try {
			cloud.loadChildren();
		} catch (Exception e) {
			IStatus status = StatusFactory.getInstance(
					IStatus.ERROR,
					Activator.PLUGIN_ID,
					e.getMessage(),
					e);
			// TODO: internationalize strings
			ErrorDialog.openError(Display.getDefault().getActiveShell(),
					"Error",
					"Cloud not get load children for " + cloud.getName(), status);
		}
	}
}
