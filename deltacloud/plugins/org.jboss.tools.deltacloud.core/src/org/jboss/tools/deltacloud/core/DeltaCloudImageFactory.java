/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.deltacloud.client.Image;
import org.eclipse.core.runtime.Assert;

/**
 * @author André Dietisheim
 */
public class DeltaCloudImageFactory {

	public static DeltaCloudImage create(Image image, DeltaCloud cloud) {
		Assert.isLegal(image != null);
		Assert.isLegal(cloud != null);

		return new DeltaCloudImage(image, cloud);
	}

	public static Collection<DeltaCloudImage> create(List<Image> images, DeltaCloud cloud) {
		Assert.isLegal(images != null);
		Assert.isLegal(cloud != null);
		
		List<DeltaCloudImage> deltaCloudImages = new ArrayList<DeltaCloudImage>();
		for(Image image : images) {
			DeltaCloudImage deltaCloudImage = create(image, cloud);
			deltaCloudImages.add(deltaCloudImage);
		}
		return deltaCloudImages;
	}
}
