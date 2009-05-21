/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * (from org.eclipse.jdt.testplugin)
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Ferenc Hechler, ferenc_hechler@users.sourceforge.net - 83258 [jar exporter] Deploy java application as executable jar
 *     Max Rydahl Andersen, made more general for usage in Hibernate tests.
 *******************************************************************************/
package org.hibernate.eclipse.console.test.xpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import junit.framework.Assert;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.test.HibernateConsoleTestPlugin;

/**
 * Helper methods to set up a IJavaProject.
 */
public class JavaProjectHelper {
	
	
	private static final int MAX_RETRY= 5;
	
	/**
	 * Creates a IJavaProject.
	 * @param projectName The name of the project
	 * @param binFolderName Name of the output folder
	 * @return Returns the Java project handle
	 * @throws CoreException Project creation failed
	 */	
	public static IJavaProject createJavaProject(String projectName, String binFolderName) throws CoreException {
		IWorkspaceRoot root= ResourcesPlugin.getWorkspace().getRoot();
		IProject project= root.getProject(projectName);
		if (!project.exists()) {
			project.create(null);
		} else {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		}
		
		if (!project.isOpen()) {
			project.open(null);
		}
		
		IPath outputLocation;
		if (binFolderName != null && binFolderName.length() > 0) {
			IFolder binFolder= project.getFolder(binFolderName);
			if (!binFolder.exists()) {
				CoreUtility.createFolder(binFolder, false, true, null);
			}
			outputLocation= binFolder.getFullPath();
		} else {
			outputLocation= project.getFullPath();
		}
		
		if (!project.hasNature(JavaCore.NATURE_ID)) {
			addNatureToProject(project, JavaCore.NATURE_ID, null);
		}
		
		IJavaProject jproject= JavaCore.create(project);
		
		jproject.setOutputLocation(outputLocation, null);
		jproject.setRawClasspath(new IClasspathEntry[0], null);
		
		return jproject;	
	}
	
	/**
	 * Sets the compiler options to 1.5 for the given project.
	 * @param project the java project
	 */	
	public static void set15CompilerOptions(IJavaProject project) {
		Map options= project.getOptions(false);
		JavaProjectHelper.set15CompilerOptions(options);
		project.setOptions(options);
	}
	
	/**
	 * Sets the compiler options to 1.4 for the given project.
	 * @param project the java project
	 */	
	public static void set14CompilerOptions(IJavaProject project) {
		Map options= project.getOptions(false);
		JavaProjectHelper.set14CompilerOptions(options);
		project.setOptions(options);
	}
	
	/**
	 * Sets the compiler options to 1.5
	 * @param options The compiler options to configure
	 */	
	public static void set15CompilerOptions(Map options) {
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
		options.put(JavaCore.COMPILER_PB_ASSERT_IDENTIFIER, JavaCore.ERROR);
		options.put(JavaCore.COMPILER_PB_ENUM_IDENTIFIER, JavaCore.ERROR);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
	}
	
	/**
	 * Sets the compiler options to 1.4
	 * @param options The compiler options to configure
	 */	
	public static void set14CompilerOptions(Map options) {
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_4);
		options.put(JavaCore.COMPILER_PB_ASSERT_IDENTIFIER, JavaCore.ERROR);
		options.put(JavaCore.COMPILER_PB_ENUM_IDENTIFIER, JavaCore.WARNING);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_3);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_2);
	}
	
	/**
	 * Sets the compiler options to 1.3
	 * @param options The compiler options to configure
	 */	
	public static void set13CompilerOptions(Map options) {
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_3);
		options.put(JavaCore.COMPILER_PB_ASSERT_IDENTIFIER, JavaCore.WARNING);
		options.put(JavaCore.COMPILER_PB_ENUM_IDENTIFIER, JavaCore.WARNING);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_3);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_2);
	}
	
	/**
	 * Removes a IJavaElement
	 * 
	 * @param elem The element to remove
	 * @throws CoreException Removing failed
	 * @see #ASSERT_NO_MIXED_LINE_DELIMIERS
	 */		
	public static void delete(final IJavaElement elem) throws CoreException {
		
		IWorkspaceRunnable runnable= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				performDummySearch();
				if (elem instanceof IJavaProject) {
					IJavaProject jproject= (IJavaProject) elem;
					jproject.setRawClasspath(new IClasspathEntry[0], jproject.getProject().getFullPath(), null);
				}
				for (int i= 0; i < MAX_RETRY; i++) {
					try {
						elem.getResource().delete(true, null);
						i= MAX_RETRY;
					} catch (CoreException e) {
						if (i == MAX_RETRY - 1) {
							JavaPlugin.log(e);
							throw e;
						}
						try {
							Thread.sleep(1000); // sleep a second
						} catch (InterruptedException e1) {
						} 
					}
				}
			}
		};
		ResourcesPlugin.getWorkspace().run(runnable, null);	
		
	}

	/**
	 * Removes all files in the project and sets the given classpath
	 * @param jproject The project to clear
	 * @param entries The default class path to set
	 * @throws CoreException Clearing the project failed
	 */			
	public static void clear(final IJavaProject jproject, final IClasspathEntry[] entries) throws CoreException {
		performDummySearch();
		
		IWorkspaceRunnable runnable= new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				jproject.setRawClasspath(entries, null);
		
				IResource[] resources= jproject.getProject().members();
				for (int i= 0; i < resources.length; i++) {
					if (!resources[i].getName().startsWith(".")) { //$NON-NLS-1$
						resources[i].delete(true, null);
					}
				}
			}
		};
		ResourcesPlugin.getWorkspace().run(runnable, null);
	}
	

	public static void performDummySearch() throws JavaModelException {
		new SearchEngine().searchAllTypeNames(
				null,
				SearchPattern.R_EXACT_MATCH,
				"XXXXXXXXX".toCharArray(),  //$NON-NLS-1$ // make sure we search a concrete name. This is faster according to Kent 
				SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE,
				IJavaSearchConstants.CLASS,
				SearchEngine.createJavaSearchScope(new IJavaElement[0]),
				new Requestor(),
				IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH,
				null);
	}


	/**
	 * Adds a source container to a IJavaProject.
	 * @param jproject The parent project
	 * @param containerName The name of the new source container
	 * @return The handle to the new source container
	 * @throws CoreException Creation failed
	 */		
	public static IPackageFragmentRoot addSourceContainer(IJavaProject jproject, String containerName) throws CoreException {
		return addSourceContainer(jproject, containerName, new Path[0]);
	}

	/**
	 * Adds a source container to a IJavaProject.
	 * @param jproject The parent project
	 * @param containerName The name of the new source container
	 * @param exclusionFilters Exclusion filters to set
	 * @return The handle to the new source container
	 * @throws CoreException Creation failed
	 */		
	public static IPackageFragmentRoot addSourceContainer(IJavaProject jproject, String containerName, IPath[] exclusionFilters) throws CoreException {
		return addSourceContainer(jproject, containerName, new Path[0], exclusionFilters);
	}
	
	/**
	 * Adds a source container to a IJavaProject.
	 * @param jproject The parent project
	 * @param containerName The name of the new source container
	 * @param inclusionFilters Inclusion filters to set
	 * @param exclusionFilters Exclusion filters to set
	 * @return The handle to the new source container
	 * @throws CoreException Creation failed
	 */				
	public static IPackageFragmentRoot addSourceContainer(IJavaProject jproject, String containerName, IPath[] inclusionFilters, IPath[] exclusionFilters) throws CoreException {
		return addSourceContainer(jproject, containerName, inclusionFilters, exclusionFilters, null);
	}
	
	/**
	 * Adds a source container to a IJavaProject.
	 * @param jproject The parent project
	 * @param containerName The name of the new source container
	 * @param inclusionFilters Inclusion filters to set
	 * @param exclusionFilters Exclusion filters to set
	 * @param outputLocation The location where class files are written to, <b>null</b> for project output folder
	 * @return The handle to the new source container
	 * @throws CoreException Creation failed
	 */
	public static IPackageFragmentRoot addSourceContainer(IJavaProject jproject, String containerName, IPath[] inclusionFilters, IPath[] exclusionFilters, String outputLocation) throws CoreException {
		IProject project= jproject.getProject();
		IContainer container= null;
		if (containerName == null || containerName.length() == 0) {
			container= project;
		} else {
			IFolder folder= project.getFolder(containerName);
			if (!folder.exists()) {
				CoreUtility.createFolder(folder, false, true, null);
			}
			container= folder;
		}
		IPackageFragmentRoot root= jproject.getPackageFragmentRoot(container);
		
		IPath outputPath= null;
		if (outputLocation != null) {
			IFolder folder= project.getFolder(outputLocation);
			if (!folder.exists()) {
				CoreUtility.createFolder(folder, false, true, null);
			}
			outputPath= folder.getFullPath();
		}
		IClasspathEntry cpe= JavaCore.newSourceEntry(root.getPath(), inclusionFilters, exclusionFilters, outputPath);
		addToClasspath(jproject, cpe);		
		return root;
	}
	
	/**
	 * Adds a source container to a IJavaProject and imports all files contained
	 * in the given ZIP file.
	 * @param jproject The parent project
	 * @param containerName Name of the source container
	 * @param zipFile Archive to import
	 * @param containerEncoding encoding for the generated source container
	 * @return The handle to the new source container
	 * @throws InvocationTargetException Creation failed
	 * @throws CoreException Creation failed
	 * @throws IOException Creation failed
	 */		
	public static IPackageFragmentRoot addSourceContainerWithImport(IJavaProject jproject, String containerName, File zipFile, String containerEncoding) throws InvocationTargetException, CoreException, IOException {
		return addSourceContainerWithImport(jproject, containerName, zipFile, containerEncoding, new Path[0]);
	}	

	/**
	 * Adds a source container to a IJavaProject and imports all files contained
	 * in the given ZIP file.
	 * @param jproject The parent project
	 * @param containerName Name of the source container
	 * @param zipFile Archive to import
	 * @param containerEncoding encoding for the generated source container
	 * @param exclusionFilters Exclusion filters to set
	 * @return The handle to the new source container
	 * @throws InvocationTargetException Creation failed
	 * @throws CoreException Creation failed
	 * @throws IOException Creation failed
	 */		
	public static IPackageFragmentRoot addSourceContainerWithImport(IJavaProject jproject, String containerName, File zipFile, String containerEncoding, IPath[] exclusionFilters) throws InvocationTargetException, CoreException, IOException {
		ZipFile file= new ZipFile(zipFile);
		try {
			IPackageFragmentRoot root= addSourceContainer(jproject, containerName, exclusionFilters);
			((IContainer) root.getCorrespondingResource()).setDefaultCharset(containerEncoding, null);
			importFilesFromZip(file, root.getPath(), null);
			return root;
		} finally {
			file.close();
		}
	}

	/**
	 * Removes a source folder from a IJavaProject.
	 * @param jproject The parent project
	 * @param containerName Name of the source folder to remove
	 * @throws CoreException Remove failed
	 */		
	public static void removeSourceContainer(IJavaProject jproject, String containerName) throws CoreException {
		IFolder folder= jproject.getProject().getFolder(containerName);
		removeFromClasspath(jproject, folder.getFullPath());
		folder.delete(true, null);
	}

	/**
	 * Adds a library entry to a IJavaProject.
	 * @param jproject The parent project
	 * @param path The path of the library to add
	 * @return The handle of the created root
	 * @throws JavaModelException 
	 */	
	public static IPackageFragmentRoot addLibrary(IJavaProject jproject, IPath path) throws JavaModelException {
		return addLibrary(jproject, path, null, null);
	}

	/**
	 * Adds a library entry with source attachment to a IJavaProject.
	 * @param jproject The parent project
	 * @param path The path of the library to add
	 * @param sourceAttachPath The source attachment path
	 * @param sourceAttachRoot The source attachment root path
	 * @return The handle of the created root
	 * @throws JavaModelException 
	 */			
	public static IPackageFragmentRoot addLibrary(IJavaProject jproject, IPath path, IPath sourceAttachPath, IPath sourceAttachRoot) throws JavaModelException {
		IClasspathEntry cpe= JavaCore.newLibraryEntry(path, sourceAttachPath, sourceAttachRoot);
		addToClasspath(jproject, cpe);
		return jproject.getPackageFragmentRoot(path.toString());
	}
	

	/**
	 * Copies the library into the project and adds it as library entry.
	 * @param jproject The parent project
	 * @param jarPath 
	 * @param sourceAttachPath The source attachment path
	 * @param sourceAttachRoot The source attachment root path
	 * @return The handle of the created root
	 * @throws IOException 
	 * @throws CoreException 
	 */			
	public static IPackageFragmentRoot addLibraryWithImport(IJavaProject jproject, IPath jarPath, IPath sourceAttachPath, IPath sourceAttachRoot) throws IOException, CoreException {
		IProject project= jproject.getProject();
		IFile newFile= project.getFile(jarPath.lastSegment());
		InputStream inputStream= null;
		try {
			inputStream= new FileInputStream(jarPath.toFile()); 
			newFile.create(inputStream, true, null);
		} finally {
			if (inputStream != null) {
				try { inputStream.close(); } catch (IOException e) { }
			}
		}				
		return addLibrary(jproject, newFile.getFullPath(), sourceAttachPath, sourceAttachRoot);
	}	

	/**
	 * Creates and adds a class folder to the class path.
	 * @param jproject The parent project
	 * @param containerName 
	 * @param sourceAttachPath The source attachment path
	 * @param sourceAttachRoot The source attachment root path
	 * @return The handle of the created root
	 * @throws CoreException 
	 */			
	public static IPackageFragmentRoot addClassFolder(IJavaProject jproject, String containerName, IPath sourceAttachPath, IPath sourceAttachRoot) throws CoreException {
		IProject project= jproject.getProject();
		IContainer container= null;
		if (containerName == null || containerName.length() == 0) {
			container= project;
		} else {
			IFolder folder= project.getFolder(containerName);
			if (!folder.exists()) {
				CoreUtility.createFolder(folder, false, true, null);
			}
			container= folder;
		}
		IClasspathEntry cpe= JavaCore.newLibraryEntry(container.getFullPath(), sourceAttachPath, sourceAttachRoot);
		addToClasspath(jproject, cpe);
		return jproject.getPackageFragmentRoot(container);
	}

	/**
	 * Creates and adds a class folder to the class path and imports all files
	 * contained in the given ZIP file.
	 * @param jproject The parent project
	 * @param containerName 
	 * @param sourceAttachPath The source attachment path
	 * @param sourceAttachRoot The source attachment root path
	 * @param zipFile 
	 * @return The handle of the created root
	 * @throws IOException 
	 * @throws CoreException 
	 * @throws InvocationTargetException 
	 */			
	public static IPackageFragmentRoot addClassFolderWithImport(IJavaProject jproject, String containerName, IPath sourceAttachPath, IPath sourceAttachRoot, File zipFile) throws IOException, CoreException, InvocationTargetException {
		ZipFile file= new ZipFile(zipFile);
		try {
			IPackageFragmentRoot root= addClassFolder(jproject, containerName, sourceAttachPath, sourceAttachRoot);
			importFilesFromZip(file, root.getPath(), null);
			return root;
		} finally {
			file.close();
		}
	}	

	
	/**
	 * Adds a variable entry with source attachment to a IJavaProject.
	 * Can return null if variable can not be resolved.
	 * @param jproject The parent project
	 * @param path The variable path
	 * @param sourceAttachPath The source attachment path (variable path)
	 * @param sourceAttachRoot The source attachment root path (variable path)
	 * @return The added package fragment root
	 * @throws JavaModelException 
	 */			
	public static IPackageFragmentRoot addVariableEntry(IJavaProject jproject, IPath path, IPath sourceAttachPath, IPath sourceAttachRoot) throws JavaModelException {
		IClasspathEntry cpe= JavaCore.newVariableEntry(path, sourceAttachPath, sourceAttachRoot);
		addToClasspath(jproject, cpe);
		IPath resolvedPath= JavaCore.getResolvedVariablePath(path);
		if (resolvedPath != null) {
			return jproject.getPackageFragmentRoot(resolvedPath.toString());
		}
		return null;
	}
	
	

	/**
	 * Adds a required project entry.
	 * @param jproject Parent project
	 * @param required Project to add to the build path
	 * @throws JavaModelException Creation failed
	 */		
	public static void addRequiredProject(IJavaProject jproject, IJavaProject required) throws JavaModelException {
		IClasspathEntry cpe= JavaCore.newProjectEntry(required.getProject().getFullPath());
		addToClasspath(jproject, cpe);
	}	
	
	public static void removeFromClasspath(IJavaProject jproject, IPath path) throws JavaModelException {
		IClasspathEntry[] oldEntries= jproject.getRawClasspath();
		int nEntries= oldEntries.length;
		List<IClasspathEntry> list= new ArrayList<IClasspathEntry>(nEntries);
		for (int i= 0 ; i < nEntries ; i++) {
			IClasspathEntry curr= oldEntries[i];
			if (!path.equals(curr.getPath())) {
				list.add(curr);			
			}
		}
		IClasspathEntry[] newEntries= list.toArray(new IClasspathEntry[list.size()]);
		jproject.setRawClasspath(newEntries, null);
	}	

	/**
	 * Sets auto-building state for the test workspace.
	 * @param state The new auto building state
	 * @return The previous state
	 * @throws CoreException Change failed
	 */
	public static boolean setAutoBuilding(boolean state) throws CoreException {
		// disable auto build
		IWorkspace workspace= ResourcesPlugin.getWorkspace();
		IWorkspaceDescription desc= workspace.getDescription();
		boolean result= desc.isAutoBuilding();
		desc.setAutoBuilding(state);
		workspace.setDescription(desc);
		return result;
	}

	public static void addToClasspath(IJavaProject jproject, IClasspathEntry cpe) throws JavaModelException {
		IClasspathEntry[] oldEntries= jproject.getRawClasspath();
		for (int i= 0; i < oldEntries.length; i++) {
			if (oldEntries[i].equals(cpe)) {
				return;
			}
		}
		int nEntries= oldEntries.length;
		IClasspathEntry[] newEntries= new IClasspathEntry[nEntries + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, nEntries);
		newEntries[nEntries]= cpe;
		jproject.setRawClasspath(newEntries, null);
	}

	
	private static void addNatureToProject(IProject proj, String natureId, IProgressMonitor monitor) throws CoreException {
		IProjectDescription description = proj.getDescription();
		String[] prevNatures= description.getNatureIds();
		String[] newNatures= new String[prevNatures.length + 1];
		System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
		newNatures[prevNatures.length]= natureId;
		description.setNatureIds(newNatures);
		proj.setDescription(description, monitor);
	}
	
	private static void importFilesFromZip(ZipFile srcZipFile, IPath destPath, IProgressMonitor monitor) throws InvocationTargetException {		
		ZipFileStructureProvider structureProvider=	new ZipFileStructureProvider(srcZipFile);
		try {
			ImportOperation op= new ImportOperation(destPath, structureProvider.getRoot(), structureProvider, new ImportOverwriteQuery());
			op.run(monitor);
		} catch (InterruptedException e) {
			// should not happen
		}
	}
	
	/**
	 * Imports resources from <code>bundleSourcePath</code> to <code>importTarget</code>.
	 * 
	 * @param importTarget the parent container
	 * @param bundleSourcePath the path to a folder containing resources
	 * 
	 * @throws CoreException import failed
	 * @throws IOException import failed
	 */
	public static void importResources(IContainer importTarget, String bundleSourcePath) throws CoreException, IOException {
		Enumeration entryPaths= HibernateConsoleTestPlugin.getDefault().getBundle().getEntryPaths(bundleSourcePath);
		while (entryPaths.hasMoreElements()) {
			String path= (String) entryPaths.nextElement();
			IPath name= new Path(path.substring(bundleSourcePath.length()));
			if (path.endsWith("/")) { //$NON-NLS-1$
				IFolder folder= importTarget.getFolder(name);
				folder.create(false, true, null);
				importResources(folder, path);
			} else {
				URL url= HibernateConsoleTestPlugin.getDefault().getBundle().getEntry(path);
				IFile file= importTarget.getFile(name);
				file.create(url.openStream(), true, null);
			}
		}
	}

	private static class ImportOverwriteQuery implements IOverwriteQuery {
		public String queryOverwrite(String file) {
			return ALL;
		}	
	}		
	
	private static class Requestor extends TypeNameRequestor{
	}
}

