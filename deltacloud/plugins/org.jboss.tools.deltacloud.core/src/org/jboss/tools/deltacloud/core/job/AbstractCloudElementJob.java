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
package org.jboss.tools.deltacloud.core.job;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.jboss.tools.deltacloud.core.DeltaCloud;

/**
 * @author André Dietisheim
 */
public abstract class AbstractCloudElementJob extends AbstractCloudJob {

	public static enum CLOUDELEMENT {
		IMAGES, INSTANCES
	}

	private CLOUDELEMENT cloudElement;

	public AbstractCloudElementJob(String name, DeltaCloud cloud, CLOUDELEMENT cloudElement) {
		super(name, cloud);
		this.cloudElement = cloudElement;
		// setUser(true);
	}

	protected abstract IStatus doRun(IProgressMonitor monitor) throws Exception;

	@Override
	protected ISchedulingRule getSchedulingRule() {
		return new CloudElementSchedulingRule(getCloud(), cloudElement);
	}
}
