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
package org.jboss.tools.vpe.resref;

import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.model.options.PreferenceModelUtilities;
import org.jboss.tools.common.model.ui.wizards.query.AbstractQueryWizard;

public class VpeResourcesDialog extends AbstractQueryWizard {
	
	public static boolean run(IFile file) {
		VpeResourcesDialog dialog = new VpeResourcesDialog();
		Properties p = new Properties();
		p.setProperty("help", "VpeResourcesDialog"); //$NON-NLS-1$ //$NON-NLS-2$
		p.put("file", file); //$NON-NLS-1$
		p.put("model", PreferenceModelUtilities.getPreferenceModel()); //$NON-NLS-1$
		dialog.setObject(p);
		int code = dialog.execute();
		return code == 0;
	}
	
	public static boolean run(IPath path) {
		VpeResourcesDialog dialog = new VpeResourcesDialog();
		Properties p = new Properties();
		p.setProperty("help", "VpeResourcesDialog"); //$NON-NLS-1$ //$NON-NLS-2$
		p.put("path", path); //$NON-NLS-1$
		p.put("model", PreferenceModelUtilities.getPreferenceModel()); //$NON-NLS-1$
		dialog.setObject(p);
		int code = dialog.execute();
		return code == 0;
	}
	
	public VpeResourcesDialog() {
		setView(new VpeResourcesDialogView());
	}

}
