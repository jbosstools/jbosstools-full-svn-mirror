/*******************************************************************************
 * Copyright (c) 2007-2009 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.vpe.editor.template.VpeAnyData;

public class ExportImportUnknownTagsTemplatesWizardDialog extends WizardDialog {

	public ExportImportUnknownTagsTemplatesWizardDialog(Shell parentShell,
			IWizard newWizard) {
		super(parentShell, newWizard);
		setHelpAvailable(false);
	}

	@Override
	public void setMinimumPageSize(int minWidth, int minHeight) {
		super.setMinimumPageSize(500, 400);
	}
	
	public List<VpeAnyData> getImportedList() {
		if (getWizard() instanceof ImportUnknownTagsTemplatesWizard) {
			return ((ImportUnknownTagsTemplatesWizard)getWizard()).getImportedList();
		} else {
			return new ArrayList<VpeAnyData>();
		}
	}
}
