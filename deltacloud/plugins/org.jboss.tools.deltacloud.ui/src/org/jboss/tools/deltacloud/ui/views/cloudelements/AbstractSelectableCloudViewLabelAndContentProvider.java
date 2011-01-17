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
import java.text.MessageFormat;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.core.IDeltaCloudElement;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.views.Columns;
import org.jboss.tools.deltacloud.ui.views.Columns.Column;

/**
 * A common superclass for content- and label-providers that operate on
 * IDeltaCloudElements (currently DeltaCloudImage and DeltaCloudInstance)
 * 
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class AbstractSelectableCloudViewLabelAndContentProvider<CLOUDELEMENT extends IDeltaCloudElement> extends
		AbstractCloudElementViewLabelAndContentProvider<CLOUDELEMENT> implements PropertyChangeListener {

	private DeltaCloud currentCloud;
	private ICloudElementFilter<CLOUDELEMENT> localFilter;
	private TableViewer viewer;
	private AtomicReference<CLOUDELEMENT[]> elementsReference = new AtomicReference<CLOUDELEMENT[]>();

	@Override
	public Object[] getElements(Object input) {
		try {
			return filter(getFilter(currentCloud), elementsReference.get());
		} catch (DeltaCloudException e) {
			ErrorUtils.handleError(
					"Error", MessageFormat.format(
							"Could not filter the elements for cloud \"{0}\"", currentCloud.getName()),
					e, viewer.getControl().getDisplay().getActiveShell());
			return new Object[] {};
		}
	}

	@Override
	public void inputChanged(final Viewer viewer, Object oldInput, Object newInput) {
		if (!(newInput instanceof DeltaCloud)) {
			return;
		}
		Assert.isLegal(viewer instanceof TableViewer);
		this.viewer = (TableViewer) viewer;
		removeListener(currentCloud);
		this.currentCloud = (DeltaCloud) newInput;
		addPropertyChangeListener(currentCloud);
		asyncGetCloudElements(currentCloud);
	}

	protected void setCloudElements(CLOUDELEMENT[] elements) {
		this.elementsReference.set(elements);
		refreshViewer();
	}

	private void refreshViewer() {
		viewer.getControl().getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				viewer.refresh();
			}
		});
	}

	protected boolean isCurrentCloud(final DeltaCloud cloud) {
		return cloud != null
				&& currentCloud != null
				&& cloud.getName().equals(currentCloud.getName());
	}

	public void setFilter(ICloudElementFilter<CLOUDELEMENT> filter) {
		this.localFilter = filter;
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
		viewer.setInput(Collections.emptyList());
	}

	protected abstract ICloudElementFilter<CLOUDELEMENT> getCloudFilter(DeltaCloud cloud);

	protected abstract void asyncGetCloudElements(DeltaCloud cloud);

	protected abstract void addPropertyChangeListener(DeltaCloud cloud);

	@SuppressWarnings("unchecked")
	@Override
	public String getColumnText(Object element, int columnIndex) {
		Columns<CLOUDELEMENT> columns = getColumns();
		Column<CLOUDELEMENT> c = columns.getColumn(columnIndex);
		if (c == null) {
			return null;
		}

		return c.getColumnText((CLOUDELEMENT) element);
	}
}
