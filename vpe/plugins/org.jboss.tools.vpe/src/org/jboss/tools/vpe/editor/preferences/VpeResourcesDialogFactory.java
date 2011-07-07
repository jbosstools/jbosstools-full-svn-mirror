/*******************************************************************************
 * Copyright (c) 2007-2011 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.editor.preferences;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.util.FileUtil;
import org.jboss.tools.vpe.editor.util.VpeStyleUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.core.VpeResourcesDialog;

public class VpeResourcesDialogFactory {
	public static void openVpeResourcesDialog(IEditorInput input) {
		IPath absoluteDefaultPath = null;
		Object fileLocation = null;
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			IFolder absoluteDefaultFolder = FileUtil.getDefaultWebRootFolder(file);
			if (absoluteDefaultFolder != null) {
				absoluteDefaultPath = absoluteDefaultFolder.getLocation();
			}
			fileLocation = file;
		} else if (input instanceof ILocationProvider) {
			ILocationProvider provider = (ILocationProvider) input;
			IPath path = provider.getPath(input);
			if (path != null) {
				fileLocation = path;
			}
		}

		IPath relativeDafaultPath = VpeStyleUtil.getInputParentPath(input);
		if (null != fileLocation) {
			VpeResourcesDialog dialogNew = 
				new VpeResourcesDialog(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						fileLocation,
						new ResourceReference(absoluteDefaultPath != null ? absoluteDefaultPath.toOSString() : "", ResourceReference.PROJECT_SCOPE), //$NON-NLS-1$
						new ResourceReference(relativeDafaultPath != null ? relativeDafaultPath.toOSString() : "", ResourceReference.FOLDER_SCOPE)); //$NON-NLS-1$
			dialogNew.open();
		} else {
			VpePlugin.getDefault().logError(VpeUIMessages.COULD_NOT_OPEN_VPE_RESOURCES_DIALOG);
		}
	}
}
