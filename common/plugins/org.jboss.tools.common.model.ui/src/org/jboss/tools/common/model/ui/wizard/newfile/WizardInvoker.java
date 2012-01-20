/*******************************************************************************
 * Copyright (c) 2007 - 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.common.model.ui.wizard.newfile;

import java.util.Properties;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.common.meta.action.SpecialWizard;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.ui.util.ExtensionPointUtils;

public class WizardInvoker implements SpecialWizard {
	Properties p;

	public void setObject(Object object) {
		if(object instanceof Properties) {
			p = (Properties)object;
		}
		
	}

	public int execute() {
		if(p == null) {
			return 1;
		}
		String pluginId = p.getProperty("plugin"); //$NON-NLS-1$
		String wizardId = p.getProperty("wizard"); //$NON-NLS-1$
		XModelObject s = (XModelObject)p.get("object"); //$NON-NLS-1$
		INewWizard wizard = ExtensionPointUtils.findNewWizardsItem(pluginId, wizardId);
		
		if(wizard == null || s == null) {
			return 1;
		}

		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(s));
		
		WizardDialog dialog = new WizardDialog(ModelUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), wizard);
		dialog.create();
		PlatformUI.getWorkbench().getHelpSystem().setHelp(dialog.getShell(), "org.eclipse.ui.new_wizard_shortcut_context"); //$NON-NLS-1$
		dialog.open();  

		return 0;
	}

}
