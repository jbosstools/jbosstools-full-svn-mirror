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

import org.jboss.tools.deltacloud.core.client.Image;


public class DeltaCloudImage {

	private Image image;
	
	public DeltaCloudImage(Image image) {
		this.image = image;
	}
	
	public String getName() {
		return image.getName();
	}
	
	public String getArchitecture() {
		return image.getArchitecture();
	}
	
	public String getDescription() {
		return image.getDescription();
	}
	
	public String getId() {
		return image.getId();
	}
}
