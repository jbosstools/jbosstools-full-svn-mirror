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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
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
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeCreatorUtil;
import org.jboss.tools.vpe.resref.core.AbsoluteFolderReferenceList;
import org.jboss.tools.vpe.resref.core.RelativeFolderReferenceList;


public class FileUtil {

	private static final String JSF2_RESOURCES = "/resources/"; //$NON-NLS-1$
    /**
     * See JBIDE-2550
     * @author mareshkau
     * @param matcher
     * @return
     */
    public static final String processJSF2Resource(VpePageContext pageContext, String resStr){
    	String resulString = resStr;
    	resulString=resulString.replaceAll(":", "/");  //$NON-NLS-1$//$NON-NLS-2$
    	resulString = "/resources/"+resulString; //$NON-NLS-1$
    	// if file not accessible and try to search in jar files
    	if(VpeCreatorUtil.getFile(resulString, pageContext)==null) {
    		String tempEntryPath =seachResourceInClassPath(pageContext, "META-INF"+resulString); //$NON-NLS-1$
    		if(tempEntryPath!=null) {
    			resulString = tempEntryPath;
    		}
    	}
    	return resulString;
    }
    
    public static boolean isExistsInJSF2Resources(VpePageContext pageContext, String resStr) {
    	String resourceString = resStr;
    	resourceString = resourceString.replaceAll(":", "/");  //$NON-NLS-1$//$NON-NLS-2$
    	resourceString = "/resources/"+resourceString; //$NON-NLS-1$
    	if(FileUtil.getFile(pageContext.getEditPart().getEditorInput(), resourceString)!=null || 
    			seachResourceInClassPath(pageContext, "META-INF"+resourceString)!=null) {
    		return true;
    	}
    	return false;	
    }
    
    /**
     * Function search into project class path resource, if resource founded in jar file, make a 
     * temp copy of this resource and return path to copy.
     * @author mareshkau
     * @param pageContext
     * @param classPathResource
     * @return path to file
     */
    private static String  seachResourceInClassPath(VpePageContext pageContext, String classPathResource) {
    	String result = null;
		final IFile currentFile = (IFile) pageContext.getVisualBuilder().getCurrentIncludeInfo().getStorage();
		final IProject project = currentFile.getProject();
		IJavaProject javaProject = JavaCore.create(project);
		try {
			for (IPackageFragmentRoot fragmentRoot : javaProject.getAllPackageFragmentRoots()) {
				if(fragmentRoot instanceof JarPackageFragmentRoot) {
					JarPackageFragmentRoot jarPackageFragmentRoot = (JarPackageFragmentRoot) fragmentRoot;
					ZipEntry zipEntry = jarPackageFragmentRoot.getJar().getEntry(classPathResource);
					if(zipEntry!=null){
						InputStream inputStream = jarPackageFragmentRoot.getJar().getInputStream(zipEntry);
						IPath stateLocation = VpePlugin.getDefault().getStateLocation();
						
						String fileName = null;
						String fileExtension = null;
						if(classPathResource.lastIndexOf(".")!=-1) { //$NON-NLS-1$ //added by mareshkau, fix for JBIDE-4954
							fileName = classPathResource.substring(classPathResource.lastIndexOf("/")+1,classPathResource.lastIndexOf(".")); //$NON-NLS-1$ //$NON-NLS-2$
							fileExtension = classPathResource.substring(classPathResource.lastIndexOf("."),classPathResource.length()); //$NON-NLS-1$
						}
						if(fileName!=null && fileName.length()>0
								&& fileExtension!=null && fileExtension.length()>0) {
							File temporaryFile =File.createTempFile(
									fileName, 
									fileExtension,  
									new File(stateLocation.toOSString())); 
							temporaryFile.deleteOnExit();
							OutputStream out = new FileOutputStream(temporaryFile,false);
					        byte[] buf = new byte[1024];
					        int len;
					        while ((len = inputStream.read(buf)) > 0) {
					            out.write(buf, 0, len);
					        }
					        inputStream.close();
					        out.close();
					        result = temporaryFile.getAbsolutePath();
						}
					}
				}
			}
		} catch (JavaModelException e) {
				VpePlugin.reportProblem(e);
		} catch (IllegalStateException e) {
				VpePlugin.reportProblem(e);
		} catch (FileNotFoundException e) {
				VpePlugin.reportProblem(e);
		} catch (CoreException e) {
				VpePlugin.reportProblem(e);
		} catch (IOException e) {
				VpePlugin.reportProblem(e);
		}
    	return result;
    }
    
    
    
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
