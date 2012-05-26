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
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.DeltaCloudResourceAction;

/**
 * @author André Dietisheim
 */
public class DestroyCloudInstanceJob extends InstanceActionJob {

	public DestroyCloudInstanceJob(String name, DeltaCloudInstance instance) {
		super(name, instance, DeltaCloudResourceAction.DESTROY, DeltaCloudInstance.State.TERMINATED);
	}

	@Override
	protected IStatus doRun(IProgressMonitor monitor) throws Exception {
		String id = getInstance().getId();
		getCloud().performInstanceAction(id, getAction());
		return Status.OK_STATUS;
	}

	@Override
	protected ISchedulingRule getSchedulingRule() {
		return new InstanceSchedulingRule(getCloud(), getCloudElement(), getInstance());
	}
}
