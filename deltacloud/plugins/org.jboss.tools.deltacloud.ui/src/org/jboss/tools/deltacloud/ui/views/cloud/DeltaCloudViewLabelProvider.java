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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

public class DeltaCloudViewLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if (element instanceof CloudViewElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_CLOUD);
		} else if (element instanceof CloudElementCategoryViewElement ||
				element instanceof NumericFoldingViewElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_FOLDER);
		} else if (element instanceof InstanceViewElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_INSTANCE);
		} else if (element instanceof ImageViewElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_IMAGE);
		}
		return null;
	}
	
	@Override
	public String getText(Object element) {
		DeltaCloudViewElement e = (DeltaCloudViewElement)element;
		return e.getName();
	}

}
