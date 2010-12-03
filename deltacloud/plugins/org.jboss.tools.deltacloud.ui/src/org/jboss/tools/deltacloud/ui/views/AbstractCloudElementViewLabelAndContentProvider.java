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
import java.util.Collections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
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

	private DeltaCloud cloud;
	private Collection<CLOUDELEMENT> cloudElements;
	private ICloudElementFilter<CLOUDELEMENT> localFilter;
	
	@Override
	public Object[] getElements(Object inputElement) {
		if (cloudElements == null) {
			return new DeltaCloudImage[] {};
		}
		return cloudElements.toArray();
	}

	public void setFilter(ICloudElementFilter<CLOUDELEMENT> filter) {
		this.localFilter = filter;
	}
	
	@Override
	public void dispose() {
		this.cloud = null;
		this.cloudElements = null;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			try {
				Assert.isLegal(newInput instanceof DeltaCloud);
				this.cloud = (DeltaCloud) newInput;
				this.cloudElements = filter(cloud);
			} catch (DeltaCloudException e) {
				this.cloudElements = Collections.emptyList();
				// TODO: internationalize strings
				ErrorUtils.handleError(
						"Error",
						"Could not display elements for cloud " + cloud.getName(),
						e, Display.getDefault().getActiveShell());
			}
		}
	}

	protected Collection<CLOUDELEMENT> filter(DeltaCloud cloud) throws DeltaCloudException {
		if (cloud == null) {
			return null;
		}
		
		ICloudElementFilter<CLOUDELEMENT> filter = null;
		filter = getCloudFilter(cloud);
		
		if (filter == null) {
			return Arrays.asList(getCloudElements(cloud));
		}

		return filter.filter(getCloudElements(cloud));
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
