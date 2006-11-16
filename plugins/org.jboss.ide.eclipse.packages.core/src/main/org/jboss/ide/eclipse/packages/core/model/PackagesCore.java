/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.packages.core.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageBuildDelegate;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;

public class PackagesCore {

	public static final QualifiedName PROPERTY_ANT_SCRIPT_PATH =
		new QualifiedName("org.jboss.ide.eclipse.packages.core", "antScriptPath");

	public static final IPath DEFAULT_ANT_SCRIPT_PATH = new Path("buildPackages.xml");
	
	public static IPackage[] getProjectPackages (IProject project, IProgressMonitor monitor)
	{
		if (monitor == null) monitor = new NullProgressMonitor();
		
		monitor.beginTask("Fetching packages for \"" + project.getName() + "\"...", 2);
		List packages = PackagesModel.instance().getProjectPackages(project);
		monitor.worked(1);
		
		if (packages == null) {
			if (project.getFile(PackagesModel.PROJECT_PACKAGES_FILE).exists())
			{
				// lazy init
				PackagesModel.instance().registerProject(project, monitor);
				packages = PackagesModel.instance().getProjectPackages(project);
			}
			
			if (packages == null) return new IPackage[0];
		}

		monitor.worked(1);
		monitor.done();
		
		return (IPackage[]) packages.toArray(new IPackage[packages.size()]);
	}
	
	public static boolean projectHasPackages (IProject project)
	{
		return project.getFile(PackagesModel.PROJECT_PACKAGES_FILE).exists();
	}
	
	/**
	 * Find files that match the given inclusion and exclusion pattern under the file system absolute path "root"
	 * @param root An IPath that represents an absolute file system path (the root to search)
	 * @param includesPattern A pattern of ant-style inclusions
	 * @param excludesPattern A pattern of ant-style exclusions
	 * @return An array of absolute IPath's pointing to matching files in the filesystem
	 */
	public static IPath[] findMatchingPaths (IPath root, String includesPattern, String excludesPattern)
	{
		DirectoryScanner scanner = PackagesModel.createDirectoryScanner(
				root, includesPattern, excludesPattern);
		
		return findMatchingPaths(scanner, root, includesPattern, excludesPattern);
	}
	
	public static IPath[] findMatchingPaths (DirectoryScanner scanner, IPath root, String includesPattern, String excludesPattern)
	{
		ArrayList paths = new ArrayList();
		
		String matched[] = scanner.getIncludedFiles();
		for (int i = 0; i < matched.length; i++)
		{
			IPath path = root.append(new Path(matched[i]));
			paths.add(path);
		}
		
		return (IPath[])paths.toArray(new IPath[paths.size()]);
	}
	
	/**
	 * Find files that match the given inclusion and exclusion pattern under the workspace container "root"
	 * @param root An IContainer in the current workspace to search
	 * @param includesPattern A pattern of ant-style inclusions
	 * @param excludesPattern A pattern of ant-style exclusions
	 * @return
	 */
	public static IFile[] findMatchingFiles (IContainer root, String includesPattern, String excludesPattern)
	{
		DirectoryScanner scanner = PackagesModel.createDirectoryScanner(
				root, includesPattern, excludesPattern);
		
		return findMatchingFiles(scanner, root, includesPattern, excludesPattern);
	}
	
	public static IFile[] findMatchingFiles (DirectoryScanner scanner, IContainer root, String includesPattern, String excludesPattern)
	{
		ArrayList files = new ArrayList();
		
		String matched[] = scanner.getIncludedFiles();
		for (int i = 0; i < matched.length; i++)
		{
			IFile file = root.getFile(new Path(matched[i]));
			if (file.exists())
			{
				files.add(file);
			}
		}
		
		return (IFile[])files.toArray(new IFile[files.size()]);
	}
	
	public static IPath getPathToPackagesScript (IProject project)
	{
		try {
			String scriptPath = project.getPersistentProperty(PROPERTY_ANT_SCRIPT_PATH);
			if (scriptPath == null) {
				project.setPersistentProperty(PROPERTY_ANT_SCRIPT_PATH, DEFAULT_ANT_SCRIPT_PATH.toString());
				return DEFAULT_ANT_SCRIPT_PATH;
			}
			else
				return new Path(scriptPath);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return DEFAULT_ANT_SCRIPT_PATH;
	}
	
	public static IPath getRawPathToPackagesScript (IProject project)
	{
		IPath scriptPath = getPathToPackagesScript(project);
		IFile script = project.getFile(scriptPath);
		if (script != null)
		{
			if (script.getLocation() == null)
				return script.getRawLocation();
			return script.getLocation();
		}
		
		return scriptPath;
	}
	
	/**
	 * Visit all of the top-level packages in the passed in project with the passed in node visitor
	 * @param project The project whose packages to visit
	 * @param visitor The visitor
	 */
	public static void visitProjectPackages (IProject project, IPackageNodeVisitor visitor)
	{
		if (projectHasPackages(project))
		{
			IPackage packages[] = getProjectPackages(project, null);
			for (int i = 0; i < packages.length; i++)
			{
				boolean keepGoing = packages[i].accept(visitor);
				if (!keepGoing) break;
			}
		}
	}
	
	public static IPackage getTopLevelPackage (IPackageNode node)
	{
		IPackageNode tmp = node.getParent(), top = tmp;
		while (tmp != null)
		{
			top = tmp;
			tmp = tmp.getParent();
		}
		
		if (top instanceof IPackage)
			return (IPackage)top;
		else return null;
	}
	
	/**
	 * @see getPackage(IFile);
	 * 
	 * @param project The project
	 * @param path The project relative path of the IPackage
	 * @return an IPackage or null
	 */
	public static IPackage getPackage (IProject project, IPath path)
	{
		return getPackage(project.getFile(path));
	}
	
	/**
	 * Returns the IPackage represented by the passed-in file.
	 * This will return null if the passed-in file does not represent an IPackage. Note that this will
	 * return an IPackage that's destination matches, but it may not actually exist yet.
	 * 
	 * @param project The project
	 * @param path The project relative path of the IPackage
	 * @return an IPackage or null
	 */
	public static IPackage getPackage (IFile file)
	{
		IPackage[] packages = getProjectPackages(file.getProject(), null);
		for (int i = 0; i < packages.length; i++)
		{
			if (packages[i].getPackageFile().equals(file))
			{
				return packages[i];
			}
		}
		
		return null;
	}
	
	/**
	 * Build the passed-in package.
	 * @param pkg The package to build
	 */
	public static void buildPackage (IPackage pkg, IProgressMonitor monitor)
	{
		if (monitor == null) monitor = new NullProgressMonitor();

		PackageBuildDelegate delegate = new PackageBuildDelegate(pkg.getProject());
		
		delegate.buildSinglePackage(pkg, monitor);
	}
	
	public static IPackage createPackage (IProject project, boolean isTopLevel)
	{
		return PackagesModel.instance().createPackage(project, isTopLevel);
	}
	
	public static IPackageFolder createPackageFolder (IProject project)
	{
		return PackagesModel.instance().createPackageFolder(project);
	}
	
	public static IPackageFileSet createPackageFileSet (IProject project)
	{
		return PackagesModel.instance().createPackageFileSet(project);
	}
	
	public static void addPackagesModelListener (IPackagesModelListener listener)
	{
		PackagesModel.instance().addPackagesModelListener(listener);
	}
	
	public static void removePackagesModelListener (IPackagesModelListener listener)
	{
		PackagesModel.instance().removePackagesModelListener(listener);
	}
	
	public static void addPackagesBuildListener (IPackagesBuildListener listener)
	{
		PackagesModel.instance().addPackagesBuildListener(listener);
	}
	
	public static void removePackagesBuildListener (IPackagesBuildListener listener)
	{
		PackagesModel.instance().removePackagesBuildListener(listener);
	}
	
}
