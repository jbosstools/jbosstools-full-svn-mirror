/*******************************************************************************
 * Copyright (c) 2007-2010 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test.wizard;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.vpe.editor.template.VpeAnyData;
import org.jboss.tools.vpe.editor.template.VpeTemplateManager;
import org.jboss.tools.vpe.editor.wizards.ExportUnknownTagsTemplatesWizard;
import org.jboss.tools.vpe.editor.wizards.ExportUnknownTagsTemplatesWizardPage;
import org.jboss.tools.vpe.editor.wizards.ImportUnknownTagsTemplatesWizard;
import org.jboss.tools.vpe.editor.wizards.ImportUnknownTagsTemplatesWizardPage;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.ui.test.TestUtil;
import org.jboss.tools.vpe.ui.test.VpeTest;
import org.jboss.tools.vpe.ui.test.VpeUiTests;

public class VpeImportExportUnknownTagsWizardsTest extends VpeTest {

	private final String EMPTY_RESULT = "List is empty"; //$NON-NLS-1$
	private final String TEMPLATES_FILE_PATH = "storedTags.xml"; //$NON-NLS-1$
	private List<VpeAnyData> tagsList;
	
	public VpeImportExportUnknownTagsWizardsTest(String name) {
		super(name);
	}

	public void testExport() throws Throwable {
		ExportUnknownTagsTemplatesWizard wizard = new ExportUnknownTagsTemplatesWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
		wizard.addPages();
		ExportUnknownTagsTemplatesWizardPage page = (ExportUnknownTagsTemplatesWizardPage) wizard
				.getPage(VpeUIMessages.EXPORT_UNKNOWN_TAGS_TEMPLATES_WIZARD_PAGE);

		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setBlockOnOpen(false);
		dialog.open();
		/*
		 * Cannot flip to next page
		 */
		assertFalse(page.canFlipToNextPage());
		/*
		 * Cannot finish the wizard
		 */
		assertFalse(wizard.canFinish());
		/*
		 * Check that templates list is empty
		 */
		String taglib = page.getValueAt(0, 0);
		assertEquals(EMPTY_RESULT, taglib);
		/*
		 * Close the dialog
		 */
		dialog.close();
		
		/*
		 * Load stored templates to the JBDS.
		 */
		IPath path = TestUtil.getComponentPath(TEMPLATES_FILE_PATH,
				VpeUiTests.IMPORT_PROJECT_NAME).getLocation();
		tagsList = VpeTemplateManager.getInstance().getAnyTemplates(path);
		VpeTemplateManager.getInstance().setAnyTemplates(tagsList);
		
		/*
		 * Open the dialog once again
		 */
		wizard = new ExportUnknownTagsTemplatesWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
		wizard.addPages();
		page = (ExportUnknownTagsTemplatesWizardPage) wizard
				.getPage(VpeUIMessages.EXPORT_UNKNOWN_TAGS_TEMPLATES_WIZARD_PAGE);
		dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setBlockOnOpen(false);
		dialog.open();
		/*
		 * Cannot flip to next page
		 */
		assertFalse(page.canFlipToNextPage());
		/*
		 * Cannot finish the wizard
		 */
		assertFalse(wizard.canFinish());
		/*
		 * Check that templates list has some values
		 */
		taglib = page.getValueAt(0, 0);
		assertEquals("taglibName:tagName", taglib); //$NON-NLS-1$
		
		taglib = page.getValueAt(1, 0);
		assertEquals("lib:tag", taglib); //$NON-NLS-1$
		/*
		 * Close the dialog
		 */
		dialog.close();
	}

	public void testImport() throws Throwable {
		ImportUnknownTagsTemplatesWizard wizard = new ImportUnknownTagsTemplatesWizard();
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection());
		wizard.addPages();
		ImportUnknownTagsTemplatesWizardPage page = (ImportUnknownTagsTemplatesWizardPage) wizard
				.getPage(VpeUIMessages.IMPORT_UNKNOWN_TAGS_TEMPLATES_WIZARD_PAGE);

		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell(), wizard);
		dialog.setBlockOnOpen(false);
		dialog.open();
		/*
		 * Cannot flip to next page
		 */
		assertFalse(page.canFlipToNextPage());
		/*
		 * Cannot finish the wizard
		 */
		assertFalse(wizard.canFinish());
		/*
		 * Check that templates list is empty
		 */
		String taglib = page.getValueAt(0, 0);
		assertEquals(EMPTY_RESULT, taglib);
		/*
		 * Close the dialog
		 */
		dialog.close();
	}
}
