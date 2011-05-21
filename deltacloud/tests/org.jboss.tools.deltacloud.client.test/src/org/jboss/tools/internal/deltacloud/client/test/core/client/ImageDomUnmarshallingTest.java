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
package org.jboss.tools.internal.deltacloud.client.test.core.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.deltacloud.client.DeltaCloudClientException;
import org.jboss.tools.deltacloud.client.Image;
import org.jboss.tools.deltacloud.client.unmarshal.ImageUnmarshaller;
import org.jboss.tools.deltacloud.client.unmarshal.ImagesUnmarshaller;
import org.jboss.tools.internal.deltacloud.client.test.fakes.ImageResponseFakes.ImageResponse;
import org.jboss.tools.internal.deltacloud.client.test.fakes.ImageResponseFakes.ImagesResponse;
import org.junit.Test;

/**
 * @author André Dietisheim
 */
public class ImageDomUnmarshallingTest {

	@Test
	public void imageMayBeUnmarshalled() throws DeltaCloudClientException {
		Image image = new Image();
		ByteArrayInputStream inputStream = new ByteArrayInputStream(ImageResponse.response.getBytes());
		new ImageUnmarshaller().unmarshall(inputStream, image);
		assertNotNull(image);
		assertEquals(ImageResponse.id, image.getId());
		assertEquals(ImageResponse.name, image.getName());
		assertEquals(ImageResponse.ownerId, image.getOwnerId());
		assertEquals(ImageResponse.description, image.getDescription());
		assertEquals(ImageResponse.architecture, image.getArchitecture());		
	}

	@Test
	public void imagesMayBeUnmarshalled() throws DeltaCloudClientException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(ImagesResponse.response.getBytes());
		List<Image> images = new ArrayList<Image>();
		new ImagesUnmarshaller().unmarshall(inputStream, images);
		assertEquals(2, images.size());

		Image image = images.get(0);
		assertEquals(ImagesResponse.id1, image.getId());
		assertEquals(ImagesResponse.name1, image.getName());

		image = images.get(1);
		assertEquals(ImagesResponse.id2, image.getId());
		assertEquals(ImagesResponse.name2, image.getName());
	}

}
