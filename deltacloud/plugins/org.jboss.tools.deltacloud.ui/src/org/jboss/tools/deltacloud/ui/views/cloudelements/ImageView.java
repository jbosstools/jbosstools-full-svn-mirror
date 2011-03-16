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

import java.beans.PropertyChangeEvent;

import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;

/**
 * A view that displays images of a DeltaCloud.
 * 
 * @see DeltaCloud#getImages()
 * 
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class ImageView extends AbstractCloudElementTableView<DeltaCloudImage> {

	@Override
	protected String getSelectedCloudPrefsKey() {
		return IDeltaCloudPreferenceConstants.LAST_CLOUD_IMAGE_VIEW;
	}

	@Override
	protected String getViewID() {
		return "org.jboss.tools.deltacloud.ui.views.ImageView";
	}

	@Override
	protected ITableContentAndLabelProvider<DeltaCloudImage> getContentAndLabelProvider() {
		return new ImageViewLabelAndContentProvider();
	}

	@Override
	protected void refreshToolbarCommandStates() {
		// do nothing
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		super.propertyChange(event);
		if (DeltaCloud.PROP_IMAGES.equals(event.getPropertyName())) {
			updateFilteredLabel();
		}
	}

	@Override
	protected ICloudElementFilter<DeltaCloudImage> getFilter(DeltaCloud cloud) {
		return cloud.getImageFilter();
	}
}
