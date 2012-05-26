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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.JavaElement;
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
import org.jboss.tools.jst.web.WebUtils;
import org.jboss.tools.vpe.VpePlugin;
import org.jboss.tools.vpe.editor.VpeIncludeInfo;
import org.jboss.tools.vpe.editor.VpeVisualDomBuilder;
import org.jboss.tools.vpe.editor.context.VpePageContext;
import org.jboss.tools.vpe.editor.template.VpeCreatorUtil;
import org.jboss.tools.vpe.resref.core.AbsoluteFolderReferenceList;
import org.jboss.tools.vpe.resref.core.RelativeFolderReferenceList;


public class FileUtil {

	private static final String JSF2_RESOURCES = "/resources/"; //$NON-NLS-1$
	/**
	 * Returns the path to the resource {@code #{resource[resourceStr]}, where
	 * {@code resourceStr} is in the form {@code 'library:name'}.
	 *  
	 * See JBIDE-2550
	 * 
	 * @author mareshkau
	 * @author yradtsevich
	 */
    public static final String getJSF2ResourcePath(VpePageContext pageContext,
    		String resourceStr) {
		if (resourceStr.contains(":")) {					//$NON-NLS-1$
				String[] parts = resourceStr.split(":");	//$NON-NLS-1$
				return getJSF2ResourcePath(pageContext, parts[0], parts[1]);
		} else {
			return getJSF2ResourcePath(pageContext, null, resourceStr);
		}
    }

	/**
	 * Returns the path to the resource specified by the {@code library}
	 * and the {@code name}.
	 * 
	 * See JBIDE-5638
	 *
	 * @param library may be {@code null}
	 * 
	 * @author mareshkau
	 * @author yradtsevich
	 * 
	 * @see <a href="http://java.sun.com/javaee/javaserverfaces/2.0/docs/api/javax/faces/application/ResourceHandler.html">javax.faces.application.ResourceHandler</a>
	 */
    public static final String getJSF2ResourcePath(VpePageContext pageContext,
    		String library, String name) {
    	String tempString = library == null ? name 
    										: library + '/' + name;

    	tempString = FileUtil.JSF2_RESOURCES + tempString;
    	String result = ""; //$NON-NLS-1$
    	// if file not accessible and try to search in jar files
    	if(VpeCreatorUtil.getFile(tempString, pageContext)==null) {
    		String tempEntryPath =seachResourceInClassPath(pageContext,
    				"META-INF" + tempString); //$NON-NLS-1$
    		if(tempEntryPath!=null) {
    			result = tempEntryPath;
    		}
    	} else {
    		result = tempString;
    	}
    	return result;
	}

    public static boolean isExistsInJSF2Resources(VpePageContext pageContext, String resStr) {
    	String resourceString = resStr;
    	resourceString = resourceString.replaceAll(":", "/");  //$NON-NLS-1$//$NON-NLS-2$
    	resourceString = FileUtil.JSF2_RESOURCES+resourceString;
    	if(FileUtil.getFile(pageContext.getEditPart().getEditorInput(), resourceString)!=null || 
    			FileUtil.seachResourceInClassPath(pageContext, "META-INF"+resourceString)!=null) {
    		return true;
    	}
    	return false;	
    }
    
    private static IJavaProject getJavaProject(VpePageContext pageContext) {
    	VpeVisualDomBuilder visualBuilder = pageContext != null ? pageContext.getVisualBuilder() : null;
		VpeIncludeInfo currentIncludeInfo = visualBuilder != null ? visualBuilder.getCurrentIncludeInfo() : null;
		IFile currentFile = currentIncludeInfo != null ? (IFile) currentIncludeInfo.getStorage() : null;
		IProject project = currentFile != null ? currentFile.getProject() : null;
		return JavaCore.create(project);
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
		final IJavaProject javaProject = getJavaProject(pageContext);
		if (javaProject == null) {
			return null;
		}
		
		String result = null;
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
				} else if (fragmentRoot instanceof JavaElement) {
					IResource resource = ((JavaElement)fragmentRoot).resource();
					if(resource instanceof IContainer && resource.exists()) {
						IFile f = ((IContainer)resource).getFile(new Path(classPathResource));
						if(f.exists()) {
							result = f.getLocation().toFile().getAbsolutePath();
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
    
    
    
    public static IFile getFile(IEditorInput input, String fileName) {
		IPath tagPath = new Path(fileName);
		if (tagPath.isEmpty()) return null;

		if (input instanceof IFileEditorInput) {
			return getFile(fileName, ((IFileEditorInput) input).getFile());
		} else if (input instanceof ILocationProvider) {
    	    IPath path = ((ILocationProvider)input).getPath(input);
    	    if(path == null || path.segmentCount() < 1) return null;
    	    path = path.removeLastSegments(1).append(fileName);
    	    return EclipseResourceUtil.getFile(path.toString());			
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
			if (resources.length == 1) {
				String location = resources[0].getLocation() + fileName;
				IPath path = new Path(location);
				file = ResourcesPlugin.getWorkspace().getRoot()
						.getFileForLocation(path);
			} else {
				//WebArtifactEdit edit = WebArtifactEdit
				//		.getWebArtifactEditForRead(includeFile.getProject());
				IContainer[] webRootFolders = WebUtils.getWebRootFolders(includeFile.getProject());
				if (webRootFolders.length > 0) {
					for (IContainer webRootFolder : webRootFolders) {
						IFile handle = webRootFolder.getFile(new Path(fileName));
						if (handle.exists()) {
							file = handle;
							break;
						}
					}
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
			if (resources.length == 1) {
				String location = resources[0].getLocation() + File.separator
						+ fileName;
				IPath path = new Path(location);
				file = ResourcesPlugin.getWorkspace().getRoot()
						.getFileForLocation(path);
			} else {
				file = resolveRelatedPath(includeFile, fileName);
			}
		}
		return file;
	}
	
	public static IFolder getDefaultWebRootFolder(IFile file) {
		IProject project = file.getProject();
		if (project == null) {
			return null;
		}

		IContainer[] webRootFolders = WebUtils.getWebRootFolders(project);
		IPath defaultWebRootPath = null;
		if (webRootFolders.length > 0) {
			defaultWebRootPath = webRootFolders[0].getFullPath();
		}
		if (defaultWebRootPath == null) {
			return null;
		}
		
		return ResourcesPlugin.getWorkspace().getRoot().getFolder(defaultWebRootPath);
	}

	/**
	 * Appends {@code relatedFilePath} to the parent directory of
	 * {@code baseFile}. Returns {@code null} if the file does not exist.
	 */
	private static IFile resolveRelatedPath(IFile baseFile,
			String relatedFilePath) {
		IPath currentFolder = baseFile.getParent().getFullPath();
		IPath path = currentFolder.append(relatedFilePath);
		IFile handle = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		return handle.exists() ? handle : null;
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
