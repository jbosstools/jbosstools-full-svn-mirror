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

import java.text.MessageFormat;

import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.GetImagesCommand;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.deltacloud.core.IImageListListener;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CVImagesCategoryElement extends CVCloudElementCategoryElement implements IImageListListener {

	private static final String IMAGE_CATEGORY_NAME = "ImageCategoryName"; //$NON-NLS-1$

	public CVImagesCategoryElement(Object element, TreeViewer viewer) {
		super(element, viewer);
		DeltaCloud cloud = getCloud();
		cloud.addImageListListener(this);
	}

	public String getName() {
		return CVMessages.getString(IMAGE_CATEGORY_NAME);
	}

	@Override
	public synchronized Object[] getChildren() {
		if (!initialized.get()) {
			new GetImagesCommand(getCloud()).execute();
			initialized.set(true);
		}
		return super.getChildren();
	}

	@Override
	protected CloudViewElement[] getElements(Object[] modelElements, int startIndex, int stopIndex) {
		CloudViewElement[] elements = new CloudViewElement[stopIndex - startIndex];
		for (int i = startIndex; i < stopIndex; ++i) {
			elements[i - startIndex] = new CVImageElement(modelElements[i], getViewer());
		}
		return elements;
	}

	@Override
	public synchronized void listChanged(DeltaCloud cloud, DeltaCloudImage[] newImages) {
		try {
			clearChildren();
			initialized.set(false);
			DeltaCloudImage[] images = filter(newImages);
			addChildren(images);
		} catch (DeltaCloudException e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					MessageFormat.format("Could not get images from cloud \"{0}\"", cloud.getName()), e,
					getViewer().getControl().getShell());
		} finally {
			initialized.set(true);
		}
		// refresh();
	}

	public DeltaCloudImage[] filter(DeltaCloudImage[] images) throws DeltaCloudException {
		DeltaCloud cloud = (DeltaCloud) getElement();
		IImageFilter f = cloud.getImageFilter();
		return f.filter(images).toArray(new DeltaCloudImage[images.length]);
	}

	@Override
	protected void dispose() {
		DeltaCloud cloud = (DeltaCloud) getElement();
		if (cloud != null) {
			cloud.removeImageListListener(this);
		}
	}
}
