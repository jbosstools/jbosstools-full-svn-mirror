/**
 * 
 */
package org.jboss.tools.smooks.configuration.editors;

import org.eclipse.ui.PartInitException;
import org.jboss.tools.smooks.dbm.editor.SmooksConfigurationFormPage;
import org.jboss.tools.smooks.editor.AbstractSmooksFormEditor;
import org.jboss.tools.smooks.graphical.editors.SmooksProcessGraphicalEditor;

/**
 * @author Dart
 *
 */
public class SmooksMultiFormEditor extends AbstractSmooksFormEditor {

	private SmooksConfigurationFormPage optionsPage;
	
	@Override
	protected void addPages() {
		
		addProcessGraphicalEditor();
		optionsPage = createSmooksConfigurationOverviewPage();
		addSourceSynchronizeListener(optionsPage);
		addSmooksEditorInitListener(optionsPage);
		try {
			int index = this.addPage(optionsPage);
			setPageText(index, Messages.SmooksMultiFormEditor_opetiontab_label);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
		super.addPages();
	}
	
	

	private SmooksConfigurationFormPage createSmooksConfigurationOverviewPage() {
		return new SmooksConfigurationFormPage(this,
				"options_page", Messages.SmooksMultiFormEditor_optinepage_name, this); //$NON-NLS-1$
	}
	
	private void addProcessGraphicalEditor() {
		SmooksProcessGraphicalEditor processPage = new SmooksProcessGraphicalEditor(this,
				"process", Messages.SmooksMultiFormEditor_processpage_name, this); //$NON-NLS-1$
//		addSourceSynchronizeListener(processPage);
//		addValidateListener(processPage);
//		addSmooksEditorInitListener(processPage);
		try {
			int index = this.addPage(processPage);
			setPageText(index, Messages.SmooksMultiFormEditor_processtabel_label);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
}
