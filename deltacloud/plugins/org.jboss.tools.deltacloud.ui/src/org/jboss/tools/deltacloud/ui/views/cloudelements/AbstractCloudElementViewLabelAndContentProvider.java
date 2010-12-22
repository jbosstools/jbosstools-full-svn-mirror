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
package org.jboss.tools.deltacloud.ui.views.cloudelements;

import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.core.IDeltaCloudElement;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * A common superclass for content- and label-providers that operate on
 * IDeltaCloudElements (currently DeltaCloudImage and DeltaCloudInstance)
 * 
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class AbstractCloudElementViewLabelAndContentProvider<CLOUDELEMENT extends IDeltaCloudElement> extends
		BaseLabelProvider implements ITableContentAndLabelProvider, PropertyChangeListener {

	private DeltaCloud currentCloud;
	private ICloudElementFilter<CLOUDELEMENT> localFilter;
	private TableViewer viewer;

	@Override
	public Object[] getElements(Object input) {
		/*
		 * items are added in asynchronous manner.
		 * 
		 * @see #inputChanged
		 * 
		 * @see #asyncAddCloudElements
		 */
		return new Object[] {};
	}

	public void setFilter(ICloudElementFilter<CLOUDELEMENT> filter) {
		this.localFilter = filter;
	}

	@Override
	public void inputChanged(final Viewer viewer, Object oldInput, Object newInput) {
		if (!(newInput instanceof DeltaCloud || newInput != null)) {
			return;
		}
		Assert.isLegal(viewer instanceof TableViewer);
		this.viewer = (TableViewer) viewer;
		removeListener(currentCloud);
		this.currentCloud = (DeltaCloud) newInput;
		addPropertyChangeListener(currentCloud);
		asyncAddCloudElements(currentCloud);
	}

	protected void updateCloudElements(CLOUDELEMENT[] elements, DeltaCloud cloud) {
		if (isCurrentCloud(cloud)) {
			addToViewer(elements);
		}
	}

	private boolean isCurrentCloud(final DeltaCloud cloud) {
		return cloud != null
				&& currentCloud != null
				&& cloud.getName().equals(currentCloud.getName());
	}

	protected void addToViewer(final CLOUDELEMENT[] cloudElements) {
		viewer.getControl().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				try {
					clearTableViewer();
					Object[] elements = filter(getFilter(currentCloud), cloudElements);
					viewer.add(elements);
				} catch (DeltaCloudException e) {
					// TODO: internationalize strings
					ErrorUtils.handleError(
							"Error", "Could not filter the elements for cloud " + currentCloud.getName(),
							e, Display.getDefault().getActiveShell());

				}
			}
		});
	}

	private ICloudElementFilter<CLOUDELEMENT> getFilter(DeltaCloud cloud) {
		if (localFilter != null) {
			return localFilter;
		} else {
			return getCloudFilter(cloud);
		}
	}

	protected Object[] filter(ICloudElementFilter<CLOUDELEMENT> filter, CLOUDELEMENT[] cloudElements)
			throws DeltaCloudException {
		if (cloudElements == null) {
			return new Object[] {};
		}
		if (filter == null) {
			return cloudElements;
		} else {
			return filter.filter(cloudElements).toArray();
		}
	}

	@Override
	public void dispose() {
		removeListener(currentCloud);
	}

	protected void removeListener(DeltaCloud cloud) {
		if (cloud != null) {
			cloud.removePropertyChangeListener(this);
		}
	}

	protected void clearTableViewer() {
		viewer.refresh();
	}

	protected abstract ICloudElementFilter<CLOUDELEMENT> getCloudFilter(DeltaCloud cloud);

	protected abstract void asyncAddCloudElements(DeltaCloud cloud);

	protected abstract void addPropertyChangeListener(DeltaCloud cloud);
}
