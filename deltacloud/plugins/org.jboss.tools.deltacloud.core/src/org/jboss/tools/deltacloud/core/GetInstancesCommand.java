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

import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * @author Andre Dietisheim
 */
public class GetInstancesCommand extends AbstractDeltaCloudCommand {

	public GetInstancesCommand(DeltaCloud cloud) {
		super(cloud);
	}

	public void execute() {
		// TODO: internationalize strings
		new AbstractCloudJob(
				MessageFormat.format("Get instances from cloud {0}", getCloud().getName())) {

			@Override
			protected IStatus doRun(IProgressMonitor monitor) throws DeltaCloudException {
				getCloud().asyncGetInstances();
				return Status.OK_STATUS;
			}

		}.schedule();
	}
}
