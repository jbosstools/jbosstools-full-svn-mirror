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
package org.jboss.ide.eclipse.archives.core.model;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.build.ArchiveBuildDelegate;

/**
 * Utility methods and entry points for model-related APIs
 * @author Rob Stryker (rob.stryker@redhat.com)
 *
 */
public class ArchivesModelCore {
	
	
	/**
	 * Builds all of a project's packages. Note that this does not call any builders before or after the package builder (i.e. the JDT builder).
	 * If you are looking to run all the builders on a project use project.build()
	 * @param project The project to build
	 * @param buildType FULL_BUILD, INCREMENTAL_BUILD, CLEAN_BUILD, etc
	 * @param monitor A progress monitor
	 */
	public static void buildProject (IPath project, IProgressMonitor monitor) {
		if (monitor == null) monitor = new NullProgressMonitor();
		new ArchiveBuildDelegate().fullProjectBuild(project);
	}
	
	/**
	 * Build the passed-in package.
	 * @param pkg The package to build
	 */
	public static void buildArchive (IArchive pkg, IProgressMonitor monitor) {
		if (monitor == null) monitor = new NullProgressMonitor();
		new ArchiveBuildDelegate().fullArchiveBuild(pkg);
	}
	
	public static IArchive[] getProjectPackages (IPath project, IProgressMonitor monitor, boolean forceInit) {
		if (monitor == null) monitor = new NullProgressMonitor();
		
		monitor.beginTask("Fetching packages for \"" + project.lastSegment() + "\"...", 2);
		IArchive[] packages = ArchivesModel.instance().getProjectArchives(project);
		monitor.worked(1);
		
		if (packages == null) {
			if (forceInit && packageFileExists(project)) {
				ArchivesModel.instance().registerProject(project, monitor);
				packages = ArchivesModel.instance().getProjectArchives(project);
			}
			
			if (packages == null) return new IArchive[0];
		}

		monitor.worked(1);
		monitor.done();
		return packages;
	}

	public static boolean packageFileExists (IPath project) {
		return project.append(ArchivesModel.PROJECT_PACKAGES_FILE).toFile().exists();
	}
	
	public static boolean projectRegistered(IPath project) {
		return ArchivesModel.instance().getRoot(project) == null ? false : true;
	}

	
	/**
	 * Visit all of the top-level packages in the passed in project with the passed in node visitor
	 * @param project The project whose packages to visit
	 * @param visitor The visitor
	 */
	public static void visitProjectArchives (IPath project, IArchiveNodeVisitor visitor) {
		if (packageFileExists(project)) {
			IArchive packages[] = getProjectPackages(project, null, false);
			if( packages == null ) return;
			for (int i = 0; i < packages.length; i++) {
				boolean keepGoing = packages[i].accept(visitor);
				if (!keepGoing) break;
			}
		}
	}

	
	public static IPath[] findMatchingPaths(IPath root, String includes, String excludes) {
		DirectoryScanner scanner  = 
			DirectoryScannerFactory.createDirectoryScanner(root, includes, excludes, true);
		String[] files = scanner.getIncludedFiles();
		IPath[] paths = new IPath[files.length];
		for( int i = 0; i < files.length; i++ ) {
			paths[i] = new Path(files[i]);
		}
		return paths;
	}
	
}
