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
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.ui.views.CVCloudElement;
import org.jboss.tools.deltacloud.ui.views.CVInstanceElement;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.deltacloud.ui.views.CloudViewElement;
import org.jboss.tools.deltacloud.ui.views.PerformInstanceActionThread;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;

/**
 * @author Andre Dietisheim
 */
public class StartInstanceHandler extends AbstractHandler implements IHandler {

	private final static String STARTING_INSTANCE_TITLE = "StartingInstance.title"; //$NON-NLS-1$
	private final static String STARTING_INSTANCE_MSG = "StartingInstance.msg"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			CVInstanceElement cvinstance = UIUtils.getFirstElement(selection, CVInstanceElement.class);
			startInstance(cvinstance);
		}

		return Status.OK_STATUS;
	}

	private void startInstance(CVInstanceElement cvInstance) {
		DeltaCloudInstance instance = (DeltaCloudInstance) cvInstance.getElement();
		CloudViewElement element = cvInstance;
		while (!(element instanceof CVCloudElement)) {
			element = (CloudViewElement) element.getParent();
		}
		CVCloudElement cvcloud = (CVCloudElement) element;
		DeltaCloud cloud = (DeltaCloud) cvcloud.getElement();
		PerformInstanceActionThread t = new PerformInstanceActionThread(cloud, instance,
				DeltaCloudInstance.START,
				CVMessages.getString(STARTING_INSTANCE_TITLE),
				CVMessages.getFormattedString(STARTING_INSTANCE_MSG, new String[] { instance.getName() }),
				DeltaCloudInstance.RUNNING);
		t.setUser(true);
		t.schedule();
	}
}
