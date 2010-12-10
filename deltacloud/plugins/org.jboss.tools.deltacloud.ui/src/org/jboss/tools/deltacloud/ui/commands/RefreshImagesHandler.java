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
package org.jboss.tools.deltacloud.ui.commands;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.job.AbstractCloudJob;

/**
 * @author Andre Dietisheim
 */
public class RefreshImagesHandler extends RefreshCloudHandler {

	protected void refresh(final DeltaCloud cloud) {
		// TODO: internationalize strings
		new AbstractCloudJob("Refreshing images on cloud " + cloud.getName(), cloud) {

			@Override
			protected IStatus doRun(IProgressMonitor monitor) throws DeltaCloudException {
				monitor.worked(1);
				cloud.loadImages();
				monitor.done();
				return Status.OK_STATUS;
			}
		}.schedule();
	}
}
