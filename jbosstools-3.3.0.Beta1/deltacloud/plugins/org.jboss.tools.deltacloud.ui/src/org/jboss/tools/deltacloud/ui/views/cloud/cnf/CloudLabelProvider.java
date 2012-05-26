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
package org.jboss.tools.deltacloud.ui.views.cloud.cnf;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;
import org.jboss.tools.deltacloud.ui.views.cloud.cnf.CloudContentProvider.CategoryContent;
import org.jboss.tools.deltacloud.ui.views.cloud.cnf.CloudContentProvider.DelayObject;
import org.jboss.tools.deltacloud.ui.views.cloud.cnf.CloudContentProvider.ImagesPager;

public class CloudLabelProvider extends LabelProvider {
	public Image getImage(Object element) {
		if (element instanceof DeltaCloud) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_CLOUD);
		} else if (element instanceof CategoryContent ) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_FOLDER);
		} else if (element instanceof DeltaCloudInstance) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_INSTANCE);
		} else if (element instanceof DeltaCloudImage) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_IMAGE);
		} else if (element instanceof ImagesPager ) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_FOLDER);
		}
		return null;
	}

	public String getText(Object element) {
		if( element instanceof DelayObject )
			return "Loading...";

		if( element instanceof DeltaCloud)
			return ((DeltaCloud)element).getName();

		if( element instanceof CategoryContent) 
			return ((CategoryContent)element).getName();

		if( element instanceof DeltaCloudImage)
			return getImageText((DeltaCloudImage)element);

		if( element instanceof DeltaCloudInstance)
			return getInstanceText((DeltaCloudInstance)element);
		
		return (element == null ? "" : element.toString());//$NON-NLS-1$
	}
	
	private String getInstanceText(DeltaCloudInstance instance) {
		String result = instance.getAlias() != null 
		 		? instance.getAlias()
				: instance.getName() != null 
					? instance.getName() 
					: "";

		if (instance.getId() != null)
			result += " [" + instance.getId() + "]";

		return result.trim();
	}
	
	private String getImageText(DeltaCloudImage image) {
		StringBuilder builder = new StringBuilder();
		if (image.getName() != null)
			builder.append(image.getName()).append(' ');
		if (image.getId() != null)
			builder.append('[').append(image.getId()).append(']');
		return builder.toString();
	}
	
}
