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

	private void addImages(DeltaCloudImage[] images) {
		if (images.length > CVNumericFoldingElement.FOLDING_SIZE) {
			int min = 0;
			int max = CVNumericFoldingElement.FOLDING_SIZE;
			int length = images.length;
			while (length > CVNumericFoldingElement.FOLDING_SIZE) {
				CVNumericFoldingElement f =
						new CVNumericFoldingElement(null, "[" + min + ".." + (max - 1) + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addChild(f);
				for (int i = min; i < max; ++i) {
					DeltaCloudImage d = images[i];
					CVImageElement element = new CVImageElement(d);
					f.addChild(element);
				}
				min += CVNumericFoldingElement.FOLDING_SIZE;
				max += CVNumericFoldingElement.FOLDING_SIZE;
				length -= CVNumericFoldingElement.FOLDING_SIZE;
			}
			if (length > 0) {
				CVNumericFoldingElement f = new CVNumericFoldingElement(null,
						"[" + min + ".." + (max - 1) + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addChild(f);
				for (int i = min; i < min + length; ++i) {
					DeltaCloudImage d = images[i];
					CVImageElement element = new CVImageElement(d);
					f.addChild(element);
				}
			}
		} else {
			for (int i = 0; i < images.length; ++i) {
				DeltaCloudImage d = images[i];
				CVImageElement element = new CVImageElement(d);
				addChild(element);
			}
		}
	}

	@Override
	public Object[] getChildren() {
		if (!initialized) {
			DeltaCloud cloud = (DeltaCloud) getElement();
			try {
				DeltaCloudImage[] images = filter(cloud.getImages());
				cloud.removeImageListListener(this);
				addImages(images);
				initialized = true;
				cloud.addImageListListener(this);
			} catch (Exception e) {
				ErrorUtils.handleError(
						"Error",
						"Colud not get images from cloud " + cloud.getName(),
						e, getViewer().getControl().getShell());
			}
		}
		return super.getChildren();
	}

	@Override
	public void listChanged(DeltaCloud cloud, DeltaCloudImage[] newImages) {
		clearChildren();
		DeltaCloudImage[] images = filter(newImages);
		addImages(images);
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
