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

import java.beans.PropertyChangeEvent;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.views.cloud.property.CloudPropertySource;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CloudItem extends DeltaCloudViewItem<DeltaCloud> {

	private TreeViewer viewer;

	protected CloudItem(DeltaCloud model, DeltaCloudViewItem<?> parent, TreeViewer viewer) {
		super(model, parent, viewer);
		this.viewer = viewer;
	}

	public String getName() {
		return getModel().getName();
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public synchronized Object[] getChildren() {
		if (!initialized.get()) {
			DeltaCloud cloud = (DeltaCloud) getModel();
			children.add(new InstancesCategoryItem(cloud, this, viewer));
			children.add(new ImagesCategoryItem(cloud, this, viewer));
		}
		initialized.set(true);
		return super.getChildren();
	}

	@Override
	public IPropertySource getPropertySource() {
		return new CloudPropertySource(getModel());
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		viewer.update(this, new String[]{DeltaCloud.PROP_NAME});
	}

	@Override
	protected void addPropertyChangeListener(DeltaCloud cloud) {
		cloud.addPropertyChangeListener(DeltaCloud.PROP_NAME, this);
	}
}
