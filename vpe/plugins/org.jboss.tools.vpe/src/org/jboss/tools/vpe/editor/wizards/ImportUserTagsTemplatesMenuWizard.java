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
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.jboss.tools.vpe.messages.VpeUIMessages;

/**
 * 
 * Wizard that imports user specified tag templates from external xml file.
 * 
 * @author dmaliarevich
 */
public class ImportUserTagsTemplatesMenuWizard extends Wizard implements
		IImportWizard {

	private IStructuredSelection selection;
	private ImportUserTagsTemplatesMenuWizardPage mainPage;
	
	/**
	 * Constructor
	 */
	public ImportUserTagsTemplatesMenuWizard() {
		super();
		setWindowTitle(VpeUIMessages.IMPORT_USER_TAGS_PAGE_TITLE);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle(VpeUIMessages.IMPORT_USER_TAGS_PAGE_TITLE);
	}
	
	@Override
	public void addPages() {
		super.addPages();
		mainPage = new ImportUserTagsTemplatesMenuWizardPage(
				VpeUIMessages.IMPORT_USER_TAGS_TEMPLATES_WIZARD_PAGE,
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
