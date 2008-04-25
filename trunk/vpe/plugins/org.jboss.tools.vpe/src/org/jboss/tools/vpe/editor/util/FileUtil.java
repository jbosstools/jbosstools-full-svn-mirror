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
package org.jboss.tools.vpe.editor.util;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;

import org.jboss.tools.common.model.*;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.vpe.VpePlugin;


public class FileUtil {

    public static IFile getFile(IEditorInput input, String value) {
		IPath tagPath = new Path(value);
		if (tagPath.isEmpty()) return null;

		if (input instanceof IFileEditorInput) {
			IContainer container = null;
			if(tagPath.isAbsolute()) {
				container = getWebRoot((IFileEditorInput)input);
			} else {
				IFile inputFile = ((IFileEditorInput)input).getFile();
				if(inputFile != null) container = inputFile.getParent();
			}
			IFile f = (container == null) ? null : container.getFile(tagPath);
			return (f == null || !f.exists()) ? null : f;
		} else if (input instanceof ILocationProvider) {
    	    IPath path = ((ILocationProvider)input).getPath(input);
    	    if(path == null || path.segmentCount() < 1) return null;
    	    path = path.removeLastSegments(1).append(value);
    	    return EclipseResourceUtil.getFile(path.toString());			
		}
        return null;
    }

    private static IContainer getWebRoot(IFileEditorInput input) {
		IProject project = ((IFileEditorInput)input).getFile().getProject();
		if(project != null && project.isOpen()) {
			IModelNature modelNature = EclipseResourceUtil.getModelNature(project);
			XModel model = (modelNature == null) ? null : modelNature.getModel();
			XModelObject webRoot = (model == null) ? null : model.getByPath("FileSystems/WEB-ROOT");
			IResource webRootResource = (webRoot == null) ? null : EclipseResourceUtil.getResource(webRoot);
			return (webRootResource instanceof IContainer) ? (IContainer)webRootResource : null;
		}
	    return null;
	}

	/**
	 * @param fileName
	 * @param includeFile
	 * @return
	 */
	public static IFile getFile(String fileName, IFile includeFile) {
		IFile file = null;
		if(fileName.startsWith("/")) {
			try {
				WebArtifactEdit edit = 
					WebArtifactEdit.getWebArtifactEditForRead(includeFile.getProject());
				IVirtualComponent com = ComponentCore.createComponent(includeFile.getProject());
				IVirtualFolder webRootFolder = com.getRootFolder().getFolder(new Path("/"));
				IContainer folder = webRootFolder.getUnderlyingFolder();
				IPath path = folder.getFullPath().append(fileName);
				file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			} catch (Exception ex) {
				// do nothing that means include will shown as text region with included file name
			}
		} else {
			IPath currentFolder = includeFile.getParent().getFullPath();
			IPath path = currentFolder.append(fileName);
			file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		}
		return file;
	}
	
	/**
	 * open editor
	 * @param file
	 */
	public static void openEditor(IFile file) {

		IWorkbenchPage workbenchPage = VpePlugin.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		try {
			if (file != null) {
				IDE.openEditor(workbenchPage, file, true);
			}
		} catch (Exception ex) {
			VpePlugin.reportProblem(ex);
		}

	}

}
