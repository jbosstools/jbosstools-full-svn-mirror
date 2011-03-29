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
package org.jboss.tools.deltacloud.core.client.unmarshal;

import org.jboss.tools.deltacloud.core.client.Image;
import org.w3c.dom.Element;

/**
 * @author André Dietisheim
 */
public class ImageUnmarshaller extends AbstractDOMUnmarshaller<Image> {

	public ImageUnmarshaller() {
		super("image", Image.class);
	}

	protected Image doUnmarshall(Element element, Image image) throws Exception {
		image.setId(getAttributeText("id", element));
		image.setName(getFirstElementText("name", element));
		image.setOwnerId(getFirstElementText("owner_id", element));
		image.setDescription(getFirstElementText("description", element));
		image.setArchitecture(getFirstElementText("architecture", element));
		return image;
	}
}
