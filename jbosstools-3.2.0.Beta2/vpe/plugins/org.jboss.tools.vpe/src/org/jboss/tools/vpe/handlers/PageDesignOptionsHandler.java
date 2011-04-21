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

package org.jboss.tools.vpe.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.jst.jsp.jspeditor.JSPMultiPageEditor;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeController;
import org.jboss.tools.vpe.editor.util.FileUtil;
import org.jboss.tools.vpe.messages.VpeUIMessages;
import org.jboss.tools.vpe.resref.core.VpeResourcesDialog;

/**
 * Handler for PageDesignOptions
 */
public class PageDesignOptionsHandler extends VisualPartAbstractHandler {
	
	public static final String COMMAND_ID="org.jboss.tools.vpe.commands.pageDesignOptionsCommand"; //$NON-NLS-1$

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart activeEditor = HandlerUtil.getActiveEditorChecked(event);
		IEditorInput input = activeEditor.getEditorInput();
		Object fileLocation = null;
		if (input instanceof IFileEditorInput) {
			IFile file = ((IFileEditorInput) input).getFile();
			fileLocation = file;
		} else if (input instanceof ILocationProvider) {
			ILocationProvider provider = (ILocationProvider) input;
			IPath path = provider.getPath(input);
			if (path != null) {
				fileLocation = path;
			}
		}
		if (null != fileLocation) {
			VpeResourcesDialog dialogNew = new VpeResourcesDialog(PlatformUI
					.getWorkbench().getDisplay().getActiveShell(), fileLocation);
			dialogNew.open();
		} else {
			VpePlugin.getDefault().logError(
					VpeUIMessages.COULD_NOT_OPEN_VPE_RESOURCES_DIALOG);
		}
		return null;
	}

	@Override
	public void setEnabled(Object evaluationContext) {
		IEditorPart activeEditor = null;
		if (evaluationContext instanceof IEvaluationContext) {
			IEvaluationContext context = (IEvaluationContext) evaluationContext;
			Object editor = context.getVariable(ISources.ACTIVE_EDITOR_NAME);
			if(editor instanceof IEditorPart){
				activeEditor = (IEditorPart) editor;
			}
		}
		boolean isVisualPartVisible=false;
		boolean isFileExists=false;
		if (activeEditor != null) {
		
			IEditorInput input = activeEditor.getEditorInput();
			IFile file = null;
			if (input instanceof IFileEditorInput) {
					file = ((IFileEditorInput) input).getFile();
				} else if (input instanceof ILocationProvider) {
					ILocationProvider provider = (ILocationProvider) input;
					IPath path = provider.getPath(input);
					if (path != null) {
						file = FileUtil.getFile(input, path.lastSegment());
						isFileExists = file != null && file.exists();
					}
				}
			isFileExists = file != null && file.exists();
			
			if(activeEditor instanceof JSPMultiPageEditor){
					JSPMultiPageEditor jspEditor = (JSPMultiPageEditor) activeEditor;
					if(jspEditor.getVisualEditor().getController()!=null)
					isVisualPartVisible=((VpeController)(jspEditor.getVisualEditor().getController())).isVisualEditorVisible();
			}
		}
		boolean enabled=isFileExists&&isVisualPartVisible;
		if (isEnabled() != enabled) {
			setBaseEnabled(enabled);
		}
	}
}
