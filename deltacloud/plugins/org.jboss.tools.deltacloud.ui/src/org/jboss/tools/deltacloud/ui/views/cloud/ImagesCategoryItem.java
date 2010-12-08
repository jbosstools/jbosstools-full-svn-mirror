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

import java.text.MessageFormat;

import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.GetImagesCommand;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.deltacloud.core.IImageListListener;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.views.CVMessages;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class ImagesCategoryItem extends CloudElementCategoryItem<DeltaCloudImage> implements IImageListListener {

	private static final String IMAGE_CATEGORY_NAME = "ImageCategoryName"; //$NON-NLS-1$

	protected ImagesCategoryItem(Object model, DeltaCloudViewItem parent, TreeViewer viewer) {
		super(model, parent, viewer);
	}

	public String getName() {
		return CVMessages.getString(IMAGE_CATEGORY_NAME);
	}

	@Override
	protected void asyncGetCloudElements() {
		setLoadingIndicator();
		new GetImagesCommand(getCloud()){

			@Override
			protected void asyncGetImages() throws DeltaCloudException {
				try {
					super.asyncGetImages();
				} catch(DeltaCloudException e) {
					clearChildren();
					throw e;
				}
			}

		}.execute();
	}

	@Override
	protected DeltaCloudViewItem[] getElements(Object[] modelElements, int startIndex, int stopIndex) {
		DeltaCloudViewItem[] elements = new DeltaCloudViewItem[stopIndex - startIndex];
		for (int i = startIndex; i < stopIndex; ++i) {
			elements[i - startIndex] = new ImageItem(modelElements[i], this, viewer);
		}
		return elements;
	}

	@Override
	public synchronized void listChanged(DeltaCloud cloud, DeltaCloudImage[] newImages) {
		try {
			onListChanged(cloud, newImages);
		} catch (DeltaCloudException e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					MessageFormat.format("Could not get images from cloud \"{0}\"", cloud.getName()), e,
					viewer.getControl().getShell());
		}
	}

	@Override
	protected DeltaCloudImage[] filter(DeltaCloudImage[] images) throws DeltaCloudException {
		DeltaCloud cloud = (DeltaCloud) getModel();
		IImageFilter f = cloud.getImageFilter();
		return f.filter(images).toArray(new DeltaCloudImage[images.length]);
	}

	protected void addCloudElementListener(DeltaCloud cloud) {
		if (cloud != null) {
			cloud.addImageListListener(this);
		}
	}

	protected void removeCloudElementListener(DeltaCloud cloud) {
		if (cloud != null) {
			cloud.removeImageListListener(this);
		}
	}

}
