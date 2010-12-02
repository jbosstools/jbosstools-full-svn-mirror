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

import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.IImageListListener;
import org.jboss.tools.deltacloud.ui.IDeltaCloudPreferenceConstants;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class ImageView extends AbstractCloudChildrenTableView<DeltaCloudImage> implements IImageListListener {

	@Override
	protected String getSelectedCloudPrefsKey() {
		return IDeltaCloudPreferenceConstants.LAST_CLOUD_IMAGE_VIEW;
	}

	@Override
	protected String getViewID() {
		return "org.jboss.tools.deltacloud.ui.views.ImageView";
	}

	@Override
	protected ITableContentAndLabelProvider getContentAndLabelProvider() {
		return new ImageViewLabelAndContentProvider();
	}

	@Override
	protected void refreshToolbarCommandStates() {
		// do nothing
	}

	@Override
	protected void addListener(DeltaCloud currentCloud) {
		if (currentCloud != null) {
			currentCloud.removeImageListListener(this);
			currentCloud.addImageListListener(this);
		}
	}

	@Override
	protected void removeListener(DeltaCloud currentCloud) {
		if (currentCloud != null) {
			currentCloud.removeImageListListener(this);
			currentCloud.addImageListListener(this);
		}
	}
}
