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
import org.jboss.tools.deltacloud.core.DeltaCloudMultiException;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.views.CloudViewElement;
import org.jboss.tools.internal.deltacloud.ui.utils.CloudViewElementUtils;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * @author Andre Dietisheim
 */
public class RefreshCloudHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			CloudViewElement cloudViewElement = UIUtils.getFirstAdaptedElement(selection, CloudViewElement.class);
			refresh(cloudViewElement);
		}

		return Status.OK_STATUS;
	}

	private void refresh(final CloudViewElement cloudViewElement) {
		if (cloudViewElement != null) {
			final DeltaCloud cloud = CloudViewElementUtils.getCloud(cloudViewElement);
			if (cloud != null) {
				// TODO: internationalize strings
				new AbstractCloudJob("Refreshing images and instances on " + cloud.getName()) {

					@Override
					protected IStatus doRun(IProgressMonitor monitor) throws DeltaCloudException {
						try {
							monitor.worked(1);
							cloud.loadChildren();
							monitor.done();
						} catch (DeltaCloudMultiException e) {
							return ErrorUtils.createMultiStatus(e);
						}
						return Status.OK_STATUS;
					}
				}.schedule();
			}
		}
	}
}
