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
import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.IInstanceFilter;
import org.jboss.tools.deltacloud.core.job.AbstractCloudJob;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class InstancesCategoryItem extends CloudElementCategoryItem<DeltaCloudInstance> {

	private static final String INSTANCE_CATEGORY_NAME = "InstanceCategoryName"; //$NON-NLS-1$

	protected InstancesCategoryItem(DeltaCloud model, DeltaCloudViewItem<?> parent, TreeViewer viewer) {
		super(model, parent, viewer);
	}

	public String getName() {
		return CVMessages.getString(INSTANCE_CATEGORY_NAME);
	}

	@Override
	protected void asyncAddCloudElements() {
		setLoadingIndicator();
		new AbstractCloudJob(
				MessageFormat.format("Get instances from cloud {0}", getModel().getName()), getModel()) {

			@Override
			protected IStatus doRun(IProgressMonitor monitor) throws DeltaCloudException {
				try {
					DeltaCloudInstance[] instances = getCloud().getInstances();
					clearChildren();
					addChildren(instances);
					expand();
					return Status.OK_STATUS;
				} catch(DeltaCloudException e) {
					clearChildren();
					throw e;
				}
			}
		}.schedule();

	}

	@Override
	protected DeltaCloudViewItem<?>[] getElements(DeltaCloudInstance[] modelElements, int startIndex, int stopIndex) {
		DeltaCloudViewItem<?>[] elements = new DeltaCloudViewItem[stopIndex - startIndex];
		for (int i = startIndex; i < stopIndex; ++i) {
			elements[i - startIndex] = new InstanceItem(modelElements[i], this, viewer);
		}
		return elements;
	}

	protected void addPropertyChangeListener(DeltaCloud cloud) {
		cloud.addPropertyChangeListener(DeltaCloud.PROP_INSTANCES, this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		DeltaCloud cloud = (DeltaCloud) event.getSource();
		DeltaCloudInstance[] newInstances = (DeltaCloudInstance[]) event.getNewValue();
		try {
			onCloudElementsChanged(cloud, newInstances);
		} catch (DeltaCloudException e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					MessageFormat.format("Could not display new instances from cloud \"{0}\"", cloud.getName()), e,
					viewer.getControl().getShell());
		}
	}

	@Override
	protected DeltaCloudInstance[] filter(DeltaCloudInstance[] instances) throws DeltaCloudException {
		DeltaCloud cloud = getModel();
		IInstanceFilter f = cloud.getInstanceFilter();
		return f.filter(instances).toArray(new DeltaCloudInstance[instances.length]);
	}
}
