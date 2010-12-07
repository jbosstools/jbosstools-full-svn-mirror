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
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.views.cloud.CloudViewElement;
import org.jboss.tools.deltacloud.ui.views.cloud.DeltaCloudViewElement;
import org.jboss.tools.internal.deltacloud.ui.utils.UIUtils;
import org.jboss.tools.internal.deltacloud.ui.wizards.NewInstanceWizard2;

/**
 * @author Jeff Johnston
 */
public class CreateInstanceHandler2 extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			DeltaCloudViewElement element = UIUtils.getFirstAdaptedElement(selection, DeltaCloudViewElement.class);
			while (element != null && !(element instanceof CloudViewElement)) {
				element = (DeltaCloudViewElement) element.getParent();
			}
			if (element != null) {
				CloudViewElement cloudElement = (CloudViewElement) element;
				DeltaCloud cloud = (DeltaCloud) cloudElement.getModel();
				IWizard wizard = new NewInstanceWizard2(cloud);
				WizardDialog dialog = new WizardDialog(UIUtils.getActiveWorkbenchWindow().getShell(),
						wizard);
				dialog.create();
				dialog.open();
			}
		}

		return Status.OK_STATUS;
	}

}
