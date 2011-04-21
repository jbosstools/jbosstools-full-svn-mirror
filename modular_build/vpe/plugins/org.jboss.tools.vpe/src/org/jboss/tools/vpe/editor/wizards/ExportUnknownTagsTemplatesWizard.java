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

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * Wizard that exports Unknown Tags Templates from Preference Page
 * to the vpe-templates-auto.xml file.
 * 
 * @author dmaliarevich
 */
public class ExportUnknownTagsTemplatesWizard extends Wizard implements
		IExportWizard {

	private IStructuredSelection selection;
	private ExportUnknownTagsTemplatesWizardPage mainPage;
	
	/**
	 * Constructor
	 */
	public ExportUnknownTagsTemplatesWizard() {
		setWindowTitle(VpeUIMessages.EXPORT_UNKNOWN_TAGS_PAGE_TITLE);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

	@Override
	public void addPages() {
		super.addPages();
		mainPage = new ExportUnknownTagsTemplatesWizardPage(
				VpeUIMessages.EXPORT_UNKNOWN_TAGS_TEMPLATES_WIZARD_PAGE,
				selection);
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		return mainPage.finish();
	}

}
