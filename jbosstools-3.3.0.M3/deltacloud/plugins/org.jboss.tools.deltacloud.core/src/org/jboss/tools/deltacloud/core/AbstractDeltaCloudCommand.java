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

import org.eclipse.core.runtime.Assert;

/**
 * @author André Dietishiem
 */
public abstract class AbstractDeltaCloudCommand {

	private DeltaCloud cloud;

	public AbstractDeltaCloudCommand(DeltaCloud cloud) {
		Assert.isLegal(cloud != null);
		this.cloud = cloud;
	}

	protected DeltaCloud getCloud() {
		return cloud;
	}

	public abstract void execute();
}
