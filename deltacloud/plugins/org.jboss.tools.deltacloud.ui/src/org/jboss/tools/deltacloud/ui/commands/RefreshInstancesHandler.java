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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.AbstractCloudJob;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * @author Andre Dietisheim
 */
public class RefreshInstancesHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			DeltaCloudInstance deltaCloudInstance = UIUtils.getFirstAdaptedElement(selection, DeltaCloudInstance.class);
			refresh(deltaCloudInstance);
		}

		return Status.OK_STATUS;
	}

	private void refresh(final DeltaCloudInstance deltaCloudInstance) {
		if (deltaCloudInstance != null) {
			final DeltaCloud cloud = deltaCloudInstance.getDeltaCloud();
			if (cloud != null) {
				// TODO: internationalize strings
				new AbstractCloudJob("Refreshing instances on cloud " + cloud.getName()) {

					@Override
					protected IStatus doRun(IProgressMonitor monitor) throws DeltaCloudException {
						monitor.worked(1);
						cloud.loadChildren();
						monitor.done();
						return Status.OK_STATUS;
					}

				}.setUser(true);
			}
		}
	}
}
