/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.struts.ui.internal.action;

import org.jboss.tools.common.model.ui.util.ExtensionPointUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.jboss.tools.common.model.ui.*;
import org.jboss.tools.struts.ui.StrutsUIPlugin;
import org.jboss.tools.struts.ui.wizard.project.ImportProjectWizard;
import org.jboss.tools.struts.messages.StrutsUIMessages;

public class ImportProjectAction extends Action implements IWorkbenchWindowActionDelegate {
	public ImportProjectAction() {
		super(StrutsUIMessages.IMPORT_STRUTS_PROJECT);
		setToolTipText(StrutsUIMessages.IMPORT_STRUTS_PROJECT);
		ModelUIImages.setImageDescriptors(this, ModelUIImages.ACT_IMPORT_PROJECT);
	}

	public void run() {
		IImportWizard wizard = ExtensionPointUtils.findImportWizardsItem(
			StrutsUIPlugin.PLUGIN_ID,
			"org.jboss.tools.struts.ui.wizard.project.ImportProjectWizard"
		);
		if (wizard != null) {
			((ImportProjectWizard)wizard).setHelpAvailable(false);
			wizard.init(ModelUIPlugin.getDefault().getWorkbench(), null);
			WizardDialog dialog = new WizardDialog(ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
			dialog.create();
			StrutsUIPlugin.getDefault().getWorkbench().getHelpSystem().setHelp(dialog.getShell(), "org.eclipse.ui.import_wizard_context");
			dialog.open();  
		} else {
			StrutsUIPlugin.getPluginLog().logError("Unable to create wizard 'org.jboss.tools.struts.ui.wizard.project.ImportProjectWizard'.");
		}
	}

	public void dispose() {}

	public void init(IWorkbenchWindow window) {}

	public void run(IAction action) {
		run();
	}

	public void selectionChanged(IAction action, ISelection selection) {}

}
