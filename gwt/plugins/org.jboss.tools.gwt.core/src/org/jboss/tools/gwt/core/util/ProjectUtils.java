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

package org.jboss.tools.gwt.core.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.ModuleCoreNature;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * A class that holds various utility methods that help to deal with projects.
 * 
 * @author adietish.
 * 
 * @see IProject
 */
public class ProjectUtils {

	/** The name of the web xml file */
	public static final String WEB_XML_FILE = "web.xml";

	/** the folder that holds the web configurations and deployed classes */
	public static final String WEB_INF_FOLDER = "WEB-INF";

	/** Signals the end of a stream. */
	private static final int EOS = -1;

	public static IPath getWebXmlPath() {	
		return new Path(WEB_INF_FOLDER).append(WEB_XML_FILE);
	}

	/**
	 * Adds a classpath entry to the given java project.
	 * 
	 * @param javaProject
	 *            the java project
	 * @param entry
	 *            the classpath entry to add
	 * @param monitor
	 * @throws JavaModelException
	 *             signals that an error occurred when adding the given entry
	 * 
	 * @see IJavaProject
	 * @see IClasspathEntry
	 */
	public static void addClasspathEntry(IJavaProject javaProject, IClasspathEntry entry, IProgressMonitor monitor)
			throws JavaModelException {
		IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
		IClasspathEntry[] newClasspath = new IClasspathEntry[rawClasspath.length + 1];
		System.arraycopy(rawClasspath, 0, newClasspath, 0, rawClasspath.length);
		newClasspath[rawClasspath.length] = entry;
		javaProject.setRawClasspath(newClasspath, monitor);
	}

	/**
	 * Returns the path of the web content folder in the given project. The path
	 * returned is relative to the workspace.
	 * 
	 * @param project
	 *            the project to return the web content folder for
	 * @return the path of the web content folder (relative to the workspace)
	 */
	public static IPath getWebContentRootPath(IProject project) {
		if (!ModuleCoreNature.isFlexibleProject(project))
			return project.getFullPath();
		IPath path = null;
		IVirtualComponent component = ComponentCore.createComponent(project);
		if (component != null && component.exists()) {
			path = component.getRootFolder().getWorkspaceRelativePath();
		} else {
			path = project.getFullPath();
		}
		return path;
	}

	/**
	 * Unzips the given ZipInputStream to the given folder.
	 * 
	 * @param zipInputStream
	 *            the zip input stream
	 * @param dirToExtractTo
	 *            the dir to extract to
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * 
	 * @see ZipInputStream
	 * @see File
	 */
	public static final void unzipToFolder(ZipInputStream zipInputStream, File dirToExtractTo) throws IOException {
		Assert.isLegal(zipInputStream != null);
		Assert.isLegal(!dirToExtractTo.exists() || dirToExtractTo.isDirectory());
		try {
			for (ZipEntry zipEntry = null; (zipEntry = zipInputStream.getNextEntry()) != null;) {
				File file = new File(dirToExtractTo, zipEntry.getName());
				if (zipEntry.isDirectory()) {
					if (!file.exists()) {
						file.mkdirs();
					}
				} else {
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}
					writeTo(zipInputStream, file);
				}
			}
		} finally {
			zipInputStream.close();
		}
	}

	private static void writeTo(InputStream inputStream, File file) throws IOException {
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
		byte[] buffer = new byte[8192];
		try {
			for (int read = 0; (read = inputStream.read(buffer)) != EOS;) {
				outputStream.write(buffer, 0, read);
			}
		} finally {
			outputStream.close();
		}
	}

	/**
	 * Returns the (paths of) the source folders for the given java project. The
	 * paths returned are relative to the workspace.
	 * 
	 * @param javaProject
	 *            the java project
	 * @return the source folders (relative to the workspace)
	 * @throws JavaModelException
	 *             the java model exception
	 * 
	 * @see IPath
	 * @see IJavaProject
	 * @see IClasspathEntry
	 * @see IClasspathEntry#CPE_SOURCE
	 */
	public static List<IPath> getSourceFolders(IJavaProject javaProject) throws JavaModelException {
		List<IPath> srcFolderList = new ArrayList<IPath>();
		for (IClasspathEntry classpathEntry : javaProject.getRawClasspath()) {
			if (IClasspathEntry.CPE_SOURCE == classpathEntry.getEntryKind()) {
				srcFolderList.add(classpathEntry.getPath());
			}
		}
		return srcFolderList;
	}

	/**
	 * Returns a file for the given path in the given project.
	 * 
	 * @param path
	 *            the path to return the file for
	 * @param project
	 *            the project the path's located in
	 * @return the file that represents the given path in the given project
	 */
	public static File getFile(IPath path, IProject project) {
		IFolder folder = project.getWorkspace().getRoot().getFolder(path);
		return folder.getLocation().toFile();
	}
	
	/**
	 * Returns a resource as stream while checking whether the resource exists.
	 *
	 * @param resourceName the resource name
	 * @return the input stream
	 * 
	 * @throws AssertionFailedException if the resource's not found
	 */
	public static InputStream checkedGetResourceStream(String resourceName, Class<?> clazz) {
		InputStream inputStream = clazz.getResourceAsStream(resourceName);
		Assert.isTrue(inputStream != null, MessageFormat.format("Could not find the zip file {0}", resourceName));
		return inputStream;
	}


}
