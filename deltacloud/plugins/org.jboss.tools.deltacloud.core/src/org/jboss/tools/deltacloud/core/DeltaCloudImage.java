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
	private DeltaCloud deltaCloud;
	
	public DeltaCloudImage(Image image, DeltaCloud deltaCloud) {
		this.image = image;
		this.deltaCloud = deltaCloud;
	}
	
	public DeltaCloud getDeltaCloud() {
		return deltaCloud;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		return result;
	}

	/**
	 * The current strategy regarding instances is to create new instances (and
	 * not update instances). We therefore need equals to be able to match
	 * domain objects. We might have to change that since in my experience it is
	 * not a good choice to create new instances, better is to update the ones
	 * that are available in the client.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeltaCloudImage other = (DeltaCloudImage) obj;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		return true;
	}
}
