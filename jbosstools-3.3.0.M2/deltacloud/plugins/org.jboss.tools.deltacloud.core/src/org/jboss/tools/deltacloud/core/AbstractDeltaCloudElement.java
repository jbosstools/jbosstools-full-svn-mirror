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


/**
 * A common superclass for cloud elements. Currently know subclasses are DeltaCloudImage and DeltaCloudInstance.
 * 
 * @see DeltaCloudInstance
 * @see DeltaCloudImage
 * 
 * @author Andre Dietisheim
 */
public abstract class AbstractDeltaCloudElement implements IDeltaCloudElement {

	private DeltaCloud cloud;

	protected AbstractDeltaCloudElement(DeltaCloud cloud) {
		this.cloud = cloud;
	}

	public DeltaCloud getDeltaCloud() {
		return cloud;
	}
}
