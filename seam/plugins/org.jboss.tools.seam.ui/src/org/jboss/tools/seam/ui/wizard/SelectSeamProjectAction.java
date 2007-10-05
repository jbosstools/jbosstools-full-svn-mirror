/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.seam.ui.wizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.ListDialog;
import org.jboss.tools.seam.ui.SeamUIMessages;
import org.jboss.tools.seam.ui.widget.editor.ButtonFieldEditor;

public class SelectSeamProjectAction extends ButtonFieldEditor.ButtonPressedAction {

	/**
	 * @param label
	 */
	public SelectSeamProjectAction() {
		super(SeamUIMessages.SELECT_SEAM_PROJECT_ACTION_BROWSE);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		ListDialog dialog = new SeamProjectSelectionDialog(
				Display.getCurrent().getActiveShell());
		if(!"".equals(this.getFieldEditor().getValueAsString())) //$NON-NLS-1$
			dialog.setInitialSelections(new Object[] {
				ResourcesPlugin.getWorkspace().getRoot().getProject(this.getFieldEditor().getValueAsString())});
		if (dialog.open() == Window.CANCEL) {
			return;
		}
		getFieldEditor().setValue(((IProject)dialog.getResult()[0]).getName());
	}
}