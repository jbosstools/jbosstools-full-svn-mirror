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

import org.jboss.tools.deltacloud.core.client.Image;

/**
 * @author André Dietisheim
 */
public class DeltaCloudImagesRepository extends AbstractDeltaCloudObjectRepository<DeltaCloudImage, String> {

	public DeltaCloudImagesRepository() {
		super(DeltaCloudImage.class);
	}

	// TODO: move to DeltaCloudImageFactory
	public DeltaCloudImage add(Image image, DeltaCloud cloud) {
		DeltaCloudImage deltaCloudImage = new DeltaCloudImage(image, cloud);
		add(deltaCloudImage);
		return deltaCloudImage;
	}

	// TODO: move to DeltaCloudImageFactory
	public Collection<DeltaCloudImage> add(Collection<Image> imagesToAdd, DeltaCloud cloud) {
		List<DeltaCloudImage> deltaCloudImages = new ArrayList<DeltaCloudImage>();
		for (Image image : imagesToAdd) {
			DeltaCloudImage deltaCloudImage = add(image, cloud);
			deltaCloudImages.add(deltaCloudImage);
		}
		return deltaCloudImages;
	}

	@Override
	protected boolean matches(String id, DeltaCloudImage image) {
			return image != null
					&& id.equals(image.getId());
	}
}
