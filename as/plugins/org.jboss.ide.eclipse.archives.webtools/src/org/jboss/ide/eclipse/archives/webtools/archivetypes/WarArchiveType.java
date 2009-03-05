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
package org.jboss.ide.eclipse.archives.webtools.archivetypes;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.server.core.IJ2EEModule;
import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.server.core.IModule;
import org.jboss.ide.eclipse.archives.core.ArchivesCorePlugin;
import org.jboss.ide.eclipse.archives.core.asf.DirectoryScanner;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelException;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.webtools.IntegrationPlugin;
import org.jboss.ide.eclipse.archives.webtools.Messages;

/**
 *
 * @author rob.stryker@jboss.com
 */
public class WarArchiveType extends J2EEArchiveType {
	public static final String WAR_PACKAGE_TYPE = "org.jboss.ide.eclipse.as.core.packages.warPackage"; //$NON-NLS-1$

	public String getAssociatedModuleType() {
		return "jst.web"; //$NON-NLS-1$
	}

	public IArchive createDefaultConfiguration(String projectName, IProgressMonitor monitor) {
		IModule mod = getModule(projectName);
		if( mod == null )
			return createDefaultConfiguration2(projectName, monitor);
		else
			return createDefaultConfigFromModule(mod, monitor);
	}

	protected IArchive createDefaultConfiguration2(String projectName, IProgressMonitor monitor) {
		IProject project = getProject(projectName);
		IArchive topLevel = createGenericIArchive(project, null, project.getName() + ".war"); //$NON-NLS-1$
		return fillDefaultConfiguration(project, topLevel, monitor);
	}

	public IArchive fillDefaultConfiguration(String projectName, IArchive topLevel, IProgressMonitor monitor) {
		return fillDefaultConfiguration(getProject(projectName), topLevel, monitor);
	}
	public IArchive fillDefaultConfiguration(IProject project, IArchive topLevel, IProgressMonitor monitor) {
		try {
			IModule mod = getModule(project.getName());
			IArchiveFolder webinf = addFolder(project, topLevel, WEBINF);
			IArchiveFolder lib = addFolder(project, webinf, LIB);
			IArchiveFolder classes = addFolder(project, webinf, CLASSES);
			addReferencedProjectsAsLibs(project, lib);
			addLibFileset(project, lib, true);
			addClassesFileset(project, classes);

			if( mod == null ) {
				addWebinfFileset(project, webinf);
			} else {
				addWebContentFileset(project, topLevel);
			}
		} catch( ArchivesModelException ame) {}
		return topLevel;
	}

	// For modules only
	protected void addWebContentFileset(IProject project, IArchiveNode packageRoot) throws ArchivesModelException {
		try {
			IPath projectPath = project.getLocation();
			DirectoryScanner scanner =  createDirectoryScanner(projectPath.toString(), "**/WEB-INF/web.xml", null, true); //$NON-NLS-1$
			String[] files = scanner.getIncludedFiles();
			// just take the first
			if( files.length > 0 ) {
				IPath path = new Path(files[0]);
				path = path.removeLastSegments(2); // remove the file name
				path = new Path(project.getName()).append(path); // pre-pend project name to make workspace-relative
				IArchiveFileSet fs = addFileset(project, packageRoot, path.toOSString(), "**/*"); //$NON-NLS-1$
				//If we have separate file set for libraries, we do not need to duplicate jars.
				fs.setExcludesPattern("**/WEB-INF/lib/*.jar"); //$NON-NLS-1$
			}
		} catch( IllegalStateException ise ) {
			IStatus status = new Status(IStatus.WARNING, IntegrationPlugin.PLUGIN_ID, Messages.ExceptionCannotScanDirectory, ise);
			IntegrationPlugin.getDefault().getLog().log(status);
		}
	}

	protected void addClassesFileset(IProject project, IArchiveFolder folder) throws ArchivesModelException {
		IJavaProject jp = JavaCore.create(project);
		if( jp != null ) {
			try {
				IPath outputLoc = project.getWorkspace().getRoot().getLocation();
				outputLoc = outputLoc.append(jp.getOutputLocation());
				addFileset(project, folder, jp.getOutputLocation().toOSString(), "**/*"); //$NON-NLS-1$
			} catch( JavaModelException jme ) {
				// no logging
			}
		}
	}
	protected void addWebinfFileset(IProject project, IArchiveFolder folder) throws ArchivesModelException {
		try {
			IPath projectPath = project.getLocation();
			DirectoryScanner scanner =  createDirectoryScanner(projectPath.toString(), "**/web.xml", null, true); //$NON-NLS-1$
			String[] files = scanner.getIncludedFiles();
			// just take the first
			if( files.length > 0 ) {
				IPath path = new Path(files[0]);
				path = path.removeLastSegments(1); // remove the file name
				path = new Path(project.getName()).append(path); // pre-pend project name to make workspace-relative
				addFileset(project, folder, path.toOSString(), "**/*"); //$NON-NLS-1$
			}
		} catch( IllegalStateException ise ) {
			IStatus status = new Status(IStatus.WARNING, ArchivesCorePlugin.PLUGIN_ID, Messages.ExceptionCannotScanDirectory, ise);
			ArchivesCorePlugin.getDefault().getLog().log(status);
		}
	}

	// Lib support
	protected void addLibFileset(IProject project, IArchiveFolder folder, boolean includeTopLevelJars) throws ArchivesModelException {
		// Let us find /WEB-INF/lib directory and set it as source for the file set.
		String sourcePath = null;

		IPath projectPath = project.getLocation();
		DirectoryScanner scanner =  createDirectoryScanner(projectPath.toString(), "**/WEB-INF/web.xml", null, true); //$NON-NLS-1$
		String[] files = scanner.getIncludedFiles();

		if(files != null && files.length > 0) {
			IPath path = new Path(files[0]);
			path = path.removeLastSegments(1).append("lib"); //$NON-NLS-1$
			sourcePath = project.getFullPath().append(path).toString();
			addFileset(project, folder, sourcePath, "*.jar");  // add default jars //$NON-NLS-1$
		} else {
			//having failed to find 'lib' directory, let us make source of the project itself
			sourcePath = project.getName();
			DirectoryScanner scanner2 =  createDirectoryScanner(projectPath.toString(), "**/*.jar", null, true); //$NON-NLS-1$
			String[] files2 = scanner2.getIncludedFiles();
			IPath p;
			ArrayList list = new ArrayList();
			for( int i = 0; i < files2.length; i++ ) {
				p = project.getFullPath().append(files2[i]).removeLastSegments(1);
				if( !list.contains(p)) {
					list.add(p);
					addFileset(project, folder, p.toString(), "*.jar");  // add default jars //$NON-NLS-1$
				}
			}
		}
	}
	protected void addReferencedProjectsAsLibs(IProject project, IArchiveFolder folder) throws ArchivesModelException {
		IJavaProject jp = JavaCore.create(project);
		if( jp != null && jp.exists()) {
			try {
				IClasspathEntry[] entries = jp.getRawClasspath();
				for( int i = 0; i < entries.length; i++ ) {
					if( entries[i].getEntryKind() == IClasspathEntry.CPE_PROJECT) {
						IPath path = entries[i].getPath();
						IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
						if( res instanceof IProject ) {
							createLibFromProject((IProject)res, folder);
						}
					}
				}
			} catch( JavaModelException jme ) {
				// no logging
			}
		}
	}


	protected void createLibFromProject(IProject project, IArchiveFolder folder) throws ArchivesModelException {
		IArchive pack = createGenericIArchive(project, null, project.getName() + ".jar");//$NON-NLS-1$
		folder.addChild(pack);
	}

	protected IArchive createDefaultConfigFromModule(IModule mod, IProgressMonitor monitor) {
		try {
			IProject project = mod.getProject();

			// create the stub
			IArchive topLevel = createGenericIArchive(project, null, project.getName() + ".war");//$NON-NLS-1$
			topLevel.setDestinationPath(new Path(project.getName()));
			topLevel.setInWorkspace(true);

			// add lib folder so we can add libraries
			IArchiveFolder webinf = addFolder(project, topLevel, WEBINF);
			IArchiveFolder lib = addFolder(project, webinf, LIB);
			IArchiveFolder classes = addFolder(project, webinf, CLASSES);


			IVirtualComponent vc = ComponentCore.createComponent(project);
			IPath webContentPath = vc.getRootFolder().getUnderlyingFolder().getFullPath();
			addFileset(project, topLevel, webContentPath.toOSString(), "**/*");//$NON-NLS-1$
			addClassesFileset(project, classes);

			// package each child and add to lib folder
			IWebModule webModule = (IWebModule)mod.loadAdapter(IWebModule.class, monitor);
			IModule[] childModules = webModule.getModules();
			for (int i = 0; i < childModules.length; i++) {
				IModule child = childModules[i];
				IJ2EEModule j2eeChild = (IJ2EEModule)child.loadAdapter(IJ2EEModule.class, new NullProgressMonitor());
				if( j2eeChild != null && !j2eeChild.isBinary()) {
					lib.addChild(createGenericIArchive(child.getProject(), null, child.getProject().getName() + ".jar"));//$NON-NLS-1$
				}
			}
			return topLevel;
		} catch( Exception e ) {
			IntegrationPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, IntegrationPlugin.PLUGIN_ID, Messages.ExceptionUnexpectedException, e));
		}
		return null;
	}

	public String getId() {
		return WAR_PACKAGE_TYPE;
	}

	public String getLabel() {
		return "WAR";//$NON-NLS-1$
	}
}