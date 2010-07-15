/**
 * 
 */
package org.jboss.tools.smooks.dbm.editor;

import org.eclipse.ui.PartInitException;
import org.jboss.tools.smooks.configuration.editors.Messages;

/**
 * @author Dart
 *
 */
public class SmooksMultiplePageEditor extends AbstractSmooksMultiplePageEditor {

	private SmooksConfigurationFormPage optionsPage;
	
	@Override
	protected void addPages() {
		optionsPage = createSmooksConfigurationOverviewPage();
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
}
