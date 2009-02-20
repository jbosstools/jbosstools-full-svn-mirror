/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.ui.test.preferences;

import junit.framework.TestCase;

import org.eclipse.jface.preference.PreferenceDialog;
import org.jboss.tools.test.util.WorkbenchUtils;
import org.jboss.tools.vpe.editor.preferences.ELVariablesPreferencePage;
import org.jboss.tools.vpe.editor.preferences.VpeEditorPreferencesPage;

public class VpeEditorPreferencesPageTest extends TestCase {
	public void testVpeEditorPreferencesPageShow() {
		PreferenceDialog prefDialog = 
			WorkbenchUtils.createPreferenceDialog(
					VpeEditorPreferencesPage.ID);

		try {
			prefDialog.setBlockOnOpen(false);
			prefDialog.open();
			
			Object selectedPage = prefDialog.getSelectedPage();
			assertTrue("Selected page is not an instance of org.jboss.tools.vpe.editor.preferences.VpeEditorPreferencesPage", selectedPage instanceof VpeEditorPreferencesPage); //$NON-NLS-1$
		} finally {
			prefDialog.close();
		}
	}
	
	public void testVpeEditorELPreferencesPageShow() {
		PreferenceDialog prefDialog = 
			WorkbenchUtils.createPreferenceDialog(
					ELVariablesPreferencePage.ID);

		try {
			prefDialog.setBlockOnOpen(false);
			prefDialog.open();
			
			Object selectedPage = prefDialog.getSelectedPage();
			assertTrue("Selected page is not an instance of org.jboss.tools.vpe.editor.preferences.ELVariablesPreferencePage", selectedPage instanceof ELVariablesPreferencePage); //$NON-NLS-1$
		} finally {
			prefDialog.close();
		}
	}
}
