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
package org.jboss.tools.deltacloud.core;

import org.apache.deltacloud.client.Image;


/**
 * A image that may be reached on a DeltaCloud instance. Wraps Image from upper layers.
 * 
 * @see Image
 * @see DeltaCloud
 * 
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class DeltaCloudImage extends AbstractDeltaCloudElement {

	private Image image;
	
	public DeltaCloudImage(Image image, DeltaCloud deltaCloud) {
		super(deltaCloud);
		this.image = image;
	}
	
	public String getName() {
		return image.getName();
	}
	
	public String getId() {
		return image.getId();
	}

	public String getArchitecture() {
		return image.getArchitecture();
	}
	
	public String getDescription() {
		return image.getDescription();
	}
}
