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
