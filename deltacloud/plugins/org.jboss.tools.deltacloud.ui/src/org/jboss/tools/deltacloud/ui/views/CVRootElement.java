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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudManager;
import org.jboss.tools.deltacloud.core.ICloudManagerListener;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CVRootElement extends CloudViewElement implements ICloudManagerListener {

	private boolean initialized;

	public CVRootElement(TreeViewer viewer) {
		super(DeltaCloudManager.getDefault(), viewer); //$NON-NLS-1$
		DeltaCloudManager.getDefault().addCloudManagerListener(this);
	}

	@Override
	public String getName() {
		return "root";
	}

	@Override
	public IPropertySource getPropertySource() {
		// no property source for the root element
		return null;
	}

	@Override
	public Object[] getChildren() {
		if (!initialized) {
			DeltaCloudManager m = DeltaCloudManager.getDefault();
			m.removeCloudManagerListener(this);
			try {
				addChildren(m.getClouds());
			} catch (DeltaCloudException e) {
				// TODO: internationalize strings
				ErrorUtils.handleError("Error", "Could not get all clouds", e, Display.getDefault().getActiveShell());
			} finally {
				initialized = true;
				m.addCloudManagerListener(this);
			}
		}
		return super.getChildren();
	}

	private void addChildren(DeltaCloud[] clouds) {
		for (int i = 0; i < clouds.length; ++i) {
			DeltaCloud cloud = clouds[i];
			CVCloudElement e = new CVCloudElement(cloud, cloud.getName(), getViewer());
			addChild(e);
		}
	}

	@Override
	public void dispose() {
		DeltaCloudManager.getDefault().removeCloudManagerListener(this);
	}

	public void cloudsChanged(int type) {
		DeltaCloudManager m = DeltaCloudManager.getDefault();
		try {
			m.removeCloudManagerListener(this);
			DeltaCloud[] clouds = m.getClouds();
			addChildren(clouds);
			initialized = true;
			m.addCloudManagerListener(this);
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					getViewer().refresh(this, false);
				}
			});
		} catch (DeltaCloudException e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					"Could not get all clouds",
					e, Display.getDefault().getActiveShell());
		}
	}

}
