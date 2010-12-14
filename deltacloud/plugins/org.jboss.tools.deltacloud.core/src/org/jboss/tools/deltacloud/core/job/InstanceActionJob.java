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
import org.jboss.tools.deltacloud.core.DeltaCloudInstance.Action;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance.State;

/**
 * @author André Dietisheim
 */
public class InstanceActionJob extends InstanceStateJob {

	private Action action;

	public InstanceActionJob(String name, DeltaCloudInstance instance, 
			Action action, State expectedState) {
		super(name, instance, expectedState);
		this.action = action;
	}

	@Override
	protected IStatus doRun(IProgressMonitor monitor) throws Exception {
		String id = getInstance().getId();
		getCloud().performInstanceAction(id, action);
		getCloud().waitForState(id, getExpectedState(), monitor);
		return Status.OK_STATUS;
	}

	protected Action getAction() {
		return action;
	}

}
