/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.preferences;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.jboss.tools.common.model.ui.preferences.TabbedPreferencesPage;
import org.jboss.tools.common.model.ui.preferences.XMOBasedPreferencesPage;
import org.jboss.tools.common.model.ui.util.ModelUtilities;
import org.jboss.tools.jst.jsp.preferences.VpePreference;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.messages.VpeUIMessages;

public class VpeEditorPreferencesPage extends TabbedPreferencesPage implements IWorkbenchPreferencePage {

	
	public static final String ID = "org.jboss.tools.vpe.editor";  //$NON-NLS-1$
	public VpeEditorPreferencesPage() {
		addPreferencePage(new GeneralPage());
		addPreferencePage(new TemplatesPreferencePage());
	}

	public static void openPreferenceDialog() {

        PreferenceDialog prefsdlg = PreferencesUtil.createPreferenceDialogOn(
                PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                ID, new String[] {
                        ID }, null);

        prefsdlg.open();
	}

	static class GeneralPage extends XMOBasedPreferencesPage {
		public GeneralPage() {
			super(ModelUtilities.getPreferenceModel().getByPath(VpePreference.VPE_EDITOR_PATH));
		}

		public String getTitle() {
		  	return VpeUIMessages.GENERAL;
		}
	}
}