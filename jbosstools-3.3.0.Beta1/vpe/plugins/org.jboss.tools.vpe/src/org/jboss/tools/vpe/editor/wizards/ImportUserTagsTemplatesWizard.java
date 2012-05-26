/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.wizards;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class ImportUserTagsTemplatesWizard extends Wizard {

	private ImportUserTagsTemplatesWizardPage mainPage;
	private List<VpeAnyData> importedList = new ArrayList<VpeAnyData>();
	private List<VpeAnyData> currentList;
	
	public ImportUserTagsTemplatesWizard(List<VpeAnyData>  currentList) {
		super();
		setWindowTitle(VpeUIMessages.IMPORT_USER_TAGS_PAGE_TITLE);
		this.currentList = currentList;
	}
	
	@Override
	public void addPages() {
		super.addPages();
		mainPage = new ImportUserTagsTemplatesWizardPage(
				VpeUIMessages.IMPORT_USER_TAGS_TEMPLATES_WIZARD_PAGE,
				currentList);
		addPage(mainPage);
	}

	@Override
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}

	@Override
	public boolean performFinish() {
		boolean pageFinished = mainPage.finish();
		if (pageFinished) {
			importedList = mainPage.getImportedList();
		}
		return pageFinished;
	}
	
	public List<VpeAnyData> getImportedList() {
		return importedList;
	}
	
}
