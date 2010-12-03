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

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jface.viewers.BaseLabelProvider;
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
		BaseLabelProvider implements ITableContentAndLabelProvider {

	private ICloudElementFilter<CLOUDELEMENT> localFilter;

	@Override
	public Object[] getElements(Object input) {
		Object[] elements = new Object[] {};
		if (input instanceof DeltaCloud) {
			DeltaCloud cloud = (DeltaCloud) input;
			try {
				CLOUDELEMENT[] cloudElements = getCloudElements(cloud);
				ICloudElementFilter<CLOUDELEMENT> filter = getCloudFilter(cloud);
				elements = filter(filter, cloudElements).toArray();
			} catch (DeltaCloudException e) {
				// TODO: internationalize strings
				ErrorUtils.handleError(
						"Error",
						"Could not display elements for cloud " + cloud.getName(),
						e, Display.getDefault().getActiveShell());
			}
		}
		return elements;
	}

	public void setFilter(ICloudElementFilter<CLOUDELEMENT> filter) {
		this.localFilter = filter;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	protected Collection<CLOUDELEMENT> filter(ICloudElementFilter<CLOUDELEMENT> filter, CLOUDELEMENT[] cloudElements)
			throws DeltaCloudException {
		if (filter == null && cloudElements != null) {
			return Arrays.asList(cloudElements);
		} else {
			return filter.filter(cloudElements);
		}
	}

	private ICloudElementFilter<CLOUDELEMENT> getFilter(DeltaCloud cloud) {
		if (localFilter != null) {
			return localFilter;
		} else {
			return getFilter(cloud);
		}
	}

	protected abstract ICloudElementFilter<CLOUDELEMENT> getCloudFilter(DeltaCloud cloud);

	protected abstract CLOUDELEMENT[] getCloudElements(DeltaCloud cloud) throws DeltaCloudException;
}
