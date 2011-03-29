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
package org.jboss.tools.internal.deltacloud.ui.wizards;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.Viewer;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.ui.views.Columns;
import org.jboss.tools.deltacloud.ui.views.DeltaCloudImageColumns;
import org.jboss.tools.deltacloud.ui.views.cloudelements.AbstractCloudElementViewLabelAndContentProvider;
import org.jboss.tools.deltacloud.ui.views.cloudelements.ITableContentAndLabelProvider;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class FindImagePageLabelAndContentProvider extends AbstractCloudElementViewLabelAndContentProvider<DeltaCloudImage> implements ITableContentAndLabelProvider<DeltaCloudImage> {

	@Override
	public Columns<DeltaCloudImage> createColumns() {
		return new DeltaCloudImageColumns();
	}

	protected ICloudElementFilter<DeltaCloudImage> getCloudFilter(DeltaCloud cloud) {
		return cloud.getImageFilter();
	}

	@Override
	public Object[] getElements(Object input) {
		Assert.isTrue(input instanceof DeltaCloudImage[]);
		return (DeltaCloudImage[]) input;
	}

	@Override
	public void dispose() {
		// do nothing
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// do nothing
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// do nothing
	}
}
