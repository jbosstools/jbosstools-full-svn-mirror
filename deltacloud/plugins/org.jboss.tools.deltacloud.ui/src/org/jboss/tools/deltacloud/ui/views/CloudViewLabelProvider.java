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

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

public class CloudViewLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if (element instanceof CVCloudElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_CLOUD);
		} else if (element instanceof CVCloudElementCategoryElement ||
				element instanceof CVNumericFoldingElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_FOLDER);
		} else if (element instanceof CVInstanceElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_INSTANCE);
		} else if (element instanceof CVImageElement) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_IMAGE);
		}
		return null;
	}
	
	@Override
	public String getText(Object element) {
		CloudViewElement e = (CloudViewElement)element;
		return e.getName();
	}

}
