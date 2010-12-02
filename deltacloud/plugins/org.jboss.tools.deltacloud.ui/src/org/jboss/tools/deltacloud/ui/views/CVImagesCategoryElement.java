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

import java.util.ArrayList;

import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.deltacloud.core.IImageListListener;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CVImagesCategoryElement extends CVCategoryElement implements IImageListListener {

	private static final String IMAGE_CATEGORY_NAME = "ImageCategoryName"; //$NON-NLS-1$

	public CVImagesCategoryElement(Object element, TreeViewer viewer) {
		super(element, viewer);
		DeltaCloud cloud = (DeltaCloud) getElement();
		cloud.addImageListListener(this);
	}

	public String getName() {
		return CVMessages.getString(IMAGE_CATEGORY_NAME);
	}

	@Override
	public synchronized Object[] getChildren() {
		if (!initialized) {
			DeltaCloud cloud = (DeltaCloud) getElement();
			try {
				cloud.removeImageListListener(this);
				DeltaCloudImage[] images = filter(cloud.getImages());
				addChildren(images);
				initialized = true;
			} catch (Exception e) {
				ErrorUtils.handleError(
						"Error",
						"Colud not get images from cloud " + cloud.getName(),
						e, getViewer().getControl().getShell());
			} finally {
				cloud.addImageListListener(this);
			}
		}
		return super.getChildren();
	}

	@Override
	protected CloudViewElement[] getElements(Object[] modelElements, int startIndex, int stopIndex) {
		CloudViewElement[] elements = new CloudViewElement[stopIndex - startIndex];
		for (int i = startIndex; i < stopIndex; ++i) {
			elements[i - startIndex] = new CVImageElement(modelElements[i]);
		}
		return elements;
	}
	
	@Override
	public synchronized void listChanged(DeltaCloud cloud, DeltaCloudImage[] newImages) {
		clearChildren();
		initialized = false;
		DeltaCloudImage[] images = filter(newImages);
		addChildren(images);
		initialized = true;
		// refresh();
	}

	public DeltaCloudImage[] filter(DeltaCloudImage[] input) {
		ArrayList<DeltaCloudImage> array = new ArrayList<DeltaCloudImage>();
		DeltaCloud cloud = (DeltaCloud) getElement();
		IImageFilter f = cloud.getImageFilter();
		for (int i = 0; i < input.length; ++i) {
			DeltaCloudImage image = input[i];
			if (f.isVisible(image))
				array.add(image);
		}
		return array.toArray(new DeltaCloudImage[array.size()]);

	}

	protected void dispose() {
		DeltaCloud cloud = (DeltaCloud) getElement();
		cloud.removeImageListListener(this);
	}
}
