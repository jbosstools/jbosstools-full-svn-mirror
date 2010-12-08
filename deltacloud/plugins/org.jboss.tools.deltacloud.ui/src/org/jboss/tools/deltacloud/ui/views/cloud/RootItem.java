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
package org.jboss.tools.deltacloud.ui.views.cloud;

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
public class RootItem extends DeltaCloudViewItem implements ICloudManagerListener {

	protected RootItem(TreeViewer viewer) {
		super(DeltaCloudManager.getDefault(), null, viewer); //$NON-NLS-1$
		DeltaCloudManager.getDefault().addCloudManagerListener(this);
	}

	@Override
	public String getName() {
		return "root"; //$NON-NLS-1$
	}

	@Override
	public IPropertySource getPropertySource() {
		// no property source for the root element
		return null;
	}

	@Override
	public Object[] getChildren() {
		if (!initialized.get()) {
			DeltaCloudManager m = DeltaCloudManager.getDefault();
			try {
				addClouds(m.getClouds());
			} catch (DeltaCloudException e) {
				// TODO: internationalize strings
				ErrorUtils.handleError("Error", "Could not get clouds", e, Display.getDefault().getActiveShell());
			} finally {
				initialized.set(true);
			}
		}
		return super.getChildren();
	}

	private void addClouds(DeltaCloud[] clouds) {
		for (DeltaCloud cloud : clouds) {
			addCloud(cloud);
		}
	}

	private void addCloud(DeltaCloud cloud) {
		CloudItem e = new CloudItem(cloud, this, viewer);
		children.add(e);
	}

	private DeltaCloudViewItem getCloudViewElement(DeltaCloud cloudToMatch) {
		for (DeltaCloudViewItem cloudElement : children) {
			DeltaCloud cloud = (DeltaCloud) cloudElement.getModel();
			if (cloudToMatch.equals(cloud)) {
				return cloudElement;
			}
		}
		return null;
	}

	@Override
	public void dispose() {
		DeltaCloudManager.getDefault().removeCloudManagerListener(this);
	}

	public void cloudsChanged(int type, DeltaCloud cloud) {
		switch (type) {
		case ICloudManagerListener.ADD_EVENT:
			addChild(new CloudItem(cloud, this, viewer));
			break;
		case ICloudManagerListener.REMOVE_EVENT:
			removeChild(getCloudViewElement(cloud));
			break;
		case ICloudManagerListener.RENAME_EVENT:
			DeltaCloudViewItem cloudViewElement = getCloudViewElement(cloud);
			viewer.refresh(cloudViewElement);
			break;
		}
		initialized.set(true);
	}
}
