/*******************************************************************************
// * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.vpe.editor.util;

import java.io.File;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.ILocationProvider;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.jboss.tools.common.model.XModel;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.project.IModelNature;
import org.jboss.tools.common.model.util.EclipseResourceUtil;
import org.jboss.tools.common.resref.core.ResourceReference;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.resref.core.AbsoluteFolderReferenceList;
import org.jboss.tools.vpe.resref.core.RelativeFolderReferenceList;


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
			XModelObject webRoot = (model == null) ? null : model.getByPath("FileSystems/WEB-ROOT"); //$NON-NLS-1$
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
		if (fileName.startsWith("/")) { //$NON-NLS-1$
			ResourceReference[] resources = AbsoluteFolderReferenceList
					.getInstance().getAllResources(includeFile);
			if (resources != null && resources.length == 1) {
				String location = resources[0].getLocation() + fileName;
				IPath path = new Path(location);
				return ResourcesPlugin.getWorkspace().getRoot()
						.getFileForLocation(path);
			} else {
				//WebArtifactEdit edit = WebArtifactEdit
				//		.getWebArtifactEditForRead(includeFile.getProject());
				IVirtualComponent com = ComponentCore
						.createComponent(includeFile.getProject());
				if (com != null) {
					IVirtualFolder webRootFolder = com.getRootFolder().getFolder(
							new Path("/")); //$NON-NLS-1$
					IContainer folder = webRootFolder.getUnderlyingFolder();
					IPath path = folder.getFullPath().append(fileName);
					file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);										
				} else {
					/* Yahor Radtsevich (yradtsevich):
					 * Fix of JBIDE-4416: assume that the parent directory
					 * of the opened file is the web-root directory */
					file = resolveRelatedPath(includeFile, fileName);
				}
			}
		} else {
			ResourceReference[] resources = RelativeFolderReferenceList
					.getInstance().getAllResources(includeFile);
			if ((resources != null) && resources.length == 1) {
				String location = resources[0].getLocation() + File.separator
						+ fileName;
				IPath path = new Path(location);
				return ResourcesPlugin.getWorkspace().getRoot()
						.getFileForLocation(path);
			} else {
				file = resolveRelatedPath(includeFile, fileName);
			}
		}
		return file;
	}

	/**
	 * Appends {@code relatedFilePath} to the parent directory of
	 * {@code baseFile}.
	 */
	private static IFile resolveRelatedPath(IFile baseFile,
			String relatedFilePath) {
		IPath currentFolder = baseFile.getParent().getFullPath();
		IPath path = currentFolder.append(relatedFilePath);
		return ResourcesPlugin.getWorkspace().getRoot().getFile(path);
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
		} catch (PartInitException ex) {
			VpePlugin.reportProblem(ex);
		}

	}
	
	/**
	 * 
	 * @param input
	 *            The editor input
	 * @return Path
	 */
	public static IPath getInputPath(IEditorInput input) {
		IPath inputPath = null;
		if (input instanceof ILocationProvider) {
			inputPath = ((ILocationProvider) input).getPath(input);
		} else if (input instanceof IFileEditorInput) {
			IFile inputFile = ((IFileEditorInput) input).getFile();
			if (inputFile != null) {
				inputPath = inputFile.getLocation();
			}
		}
		return inputPath;
	}

}
