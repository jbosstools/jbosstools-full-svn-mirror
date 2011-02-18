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
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance.State;

/**
 * @author André Dietisheim
 */
public class InstanceStateJob extends AbstractInstanceJob {

	private State expectedState;

	public InstanceStateJob(String name, DeltaCloudInstance instance, State expectedState) {
		super(name, instance);
		this.expectedState = expectedState;
	}

	@Override
	protected IStatus doRun(IProgressMonitor monitor) throws Exception {
		String id = getInstance().getId();
		getCloud().waitForState(id, expectedState, monitor);
		if( monitor.isCanceled())
			return Status.CANCEL_STATUS;
		return Status.OK_STATUS;
	}

	protected State getExpectedState() {
		return expectedState;
	}
}
