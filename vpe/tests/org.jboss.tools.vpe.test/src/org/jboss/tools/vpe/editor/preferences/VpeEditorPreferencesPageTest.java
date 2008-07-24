package org.jboss.tools.vpe.editor.preferences;

import junit.framework.TestCase;

import org.eclipse.jface.preference.PreferenceDialog;
import org.jboss.tools.test.util.WorkbenchUtils;

public class VpeEditorPreferencesPageTest extends TestCase {
	public void testVpeEditorPreferencesPageShow() {
		PreferenceDialog prefDialog = 
			WorkbenchUtils.createPreferenceDialog(
					VpeEditorPreferencesPage.class.getName());

		try {
			prefDialog.setBlockOnOpen(false);
			prefDialog.open();
			
			Object selectedPage = prefDialog.getSelectedPage();
			assertTrue("Selected page is not an instance of org.jboss.tools.vpe.editor.preferences.VpeEditorPreferencesPage", selectedPage instanceof VpeEditorPreferencesPage);
		} finally {
			prefDialog.close();
		}
	}
}
