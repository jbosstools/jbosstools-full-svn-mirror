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

import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.jboss.tools.common.model.ui.preferences.TabbedPreferencesPage;

public class VpeEditorPreferencesPage extends TabbedPreferencesPage implements IWorkbenchPreferencePage {
	
	public static final String ID = "org.jboss.tools.vpe.editor";  //$NON-NLS-1$
	public VpeEditorPreferencesPage() {
		addPreferencePage(new VpePreferencesPage());
		addPreferencePage(new TemplatesPreferencePage());
	}

	public static void openPreferenceDialog() {

        PreferenceDialog prefsdlg = PreferencesUtil.createPreferenceDialogOn(
                PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                ID, new String[] { ID }, null);
		/*
		 * https://jira.jboss.org/jira/browse/JBIDE-4975 
		 * Setting up the correct preference page size. 
		 * Without specifying any preferred size 
		 * the default constrained Shell bounds are used, 
		 * which are not always optimal.
		 */
        prefsdlg.getShell().setSize(prefsdlg.getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
        prefsdlg.open();
	}
	
}