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
package org.jboss.ide.eclipse.packages.core.project.build;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.selectors.SelectorUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.jboss.ide.eclipse.core.util.ResourceUtil;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.DirectoryScannerFactory;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageFileSetImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.util.PackagesExport;

public class PackageBuildDelegate {
	private static PackageBuildDelegate _instance;
	
	private boolean building;
	// 
	private TreeSet referencedProjects;
	private List packages;
	private IProject project;
	private IResourceDelta delta;
	
	private BuildFileOperations fileOperations;
	private BuildEvents events;
	
	public static PackageBuildDelegate instance ()
	{
		if (_instance == null)
			_instance = new PackageBuildDelegate();
		
		return _instance;
	}
	
	public PackageBuildDelegate () {
		events = new BuildEvents(this);
		fileOperations = new BuildFileOperations(this);
	}
	
	private IPackageFileSet[] findMatchingFilesetsForRemovedFile (final IFile removedFile)
	{
		final ArrayList filesets = new ArrayList();
		
		Set packageProjects = PackagesModel.instance().getRegisteredProjects();
		for (Iterator iter = packageProjects.iterator(); iter.hasNext(); )
		{
			IProject project = (IProject) iter.next();
			List packages = PackagesModel.instance().getProjectPackages(project);
			
			for (Iterator pkgIter = packages.iterator(); pkgIter.hasNext(); )
			{
				IPackage pkg = (IPackage) pkgIter.next();
				pkg.accept(new IPackageNodeVisitor () {
					public boolean visit(IPackageNode node) {
						if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
						{
							IPackageFileSet fileset = (IPackageFileSet) node;
							
							if (fileset.isSingleFile())
							{
								if (fileset.getFilePath().equals(ResourceUtil.makeAbsolute(removedFile)))
								{
									filesets.add(fileset);
								}
							} else {
								IPath root = fileset.getSourcePath();
								IPath relativePath = ResourceUtil.makeAbsolute(removedFile);
								relativePath = relativePath.removeFirstSegments(root.segmentCount());
								relativePath = relativePath.setDevice(null);
								
								boolean matchesIncludes = DirectoryScannerFactory.matchesPath(fileset.getIncludesPattern(), relativePath.toString());
								
								boolean matchesExcludes = false;
								if (fileset.getExcludesPattern() != null && fileset.getExcludesPattern().length() > 0)
								{
									matchesExcludes = DirectoryScannerFactory.matchesPath(fileset.getExcludesPattern(), relativePath.toString());
								}
								
								if (matchesIncludes && !matchesExcludes)
								{
									filesets.add(fileset);
								}
							}
						}
						return true;
					}
				});
			}
		}
		
		return (IPackageFileSet[]) filesets.toArray(new IPackageFileSet[filesets.size()]);
	}
	
	private IPackageFileSet[] findMatchingFilesets (final IFile file)
	{
		final ArrayList filesets = new ArrayList();
		
		Set packageProjects = PackagesModel.instance().getRegisteredProjects();
		for (Iterator iter = packageProjects.iterator(); iter.hasNext(); )
		{
			IProject project = (IProject) iter.next();
			List packages = PackagesModel.instance().getProjectPackages(project);
			
			for (Iterator pkgIter = packages.iterator(); pkgIter.hasNext(); )
			{
				IPackage pkg = (IPackage) pkgIter.next();
				pkg.accept(new IPackageNodeVisitor () {
					public boolean visit(IPackageNode node) {
						if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
						{
							IPackageFileSet fileset = (IPackageFileSet) node;
							if (fileset.matchesFile(file))
							{
								filesets.add(fileset);
							}
						}
						return true;
					}
				});
			}
		}
		
		return (IPackageFileSet[]) filesets.toArray(new IPackageFileSet[filesets.size()]);
	}
	
	public void buildFileset (IPackageFileSet fileset, boolean checkStamps)
	{
		DirectoryScanner scanner = ((PackageFileSetImpl)fileset).createDirectoryScanner(true);
		PackageFileSetImpl filesetImpl = (PackageFileSetImpl) fileset;
		IPackageFileSet filesets[] = new IPackageFileSet[] { fileset };
		
		if (fileset.isInWorkspace())
		{
			events.fireStartedCollectingFileSet(fileset);	
			IFile matchingFiles[] = filesetImpl.findMatchingFiles(scanner);
			events.fireFinishedCollectingFileSet(fileset);
			
			for (int i = 0; i < matchingFiles.length; i++)
			{
				fileOperations.updateFileInFilesets(matchingFiles[i], filesets, checkStamps);
			}
		} else {
			events.fireStartedCollectingFileSet(fileset);
			IPath matchingPaths[] = filesetImpl.findMatchingPaths(scanner);
			events.fireFinishedCollectingFileSet(fileset);
			
			for (int i = 0; i < matchingPaths.length; i++)
			{
				fileOperations.updatePathInFilesets(matchingPaths[i], filesets, checkStamps);
			}
		}
	}
	
	private int buildSteps;
	private int countBuildSteps (IPackageNode node)
	{
		buildSteps = 0;
		node.accept(new IPackageNodeVisitor () {
			public boolean visit (IPackageNode node) {
				if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
				{
					buildSteps++;
				}
				return true;
			}
		});
		return buildSteps;
	}
	
	private void buildPackage (IPackage pkg, final IProgressMonitor monitor)
	{
		events.fireStartedBuildingPackage(pkg);
		
		int steps = countBuildSteps(pkg);
		monitor.beginTask("Building package \"" + pkg.getName() + "\"...", steps);
		
		pkg.accept(new IPackageNodeVisitor () {
			public boolean visit(IPackageNode node) {
				if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
				{
					buildFileset((IPackageFileSet)node, false);
					monitor.worked(1);
				}
				return true;
			}
		});
		
		TruezipUtil.umountAll();
		BuildFileOperations.refreshPackage(pkg);
		
		monitor.done();
		
		events.fireFinishedBuildingPackage(pkg);
	}
	
	private void fullBuild(Map args,  IProgressMonitor monitor)
	{
		for (Iterator iter = packages.iterator(); iter.hasNext(); )
		{
			IPackage pkg = (IPackage) iter.next();
			buildPackage(pkg, monitor);
		}
	}
	
	private void cleanBuild(Map args, IProgressMonitor monitor)
	{
		monitor.beginTask("Cleaning packages...", packages.size());
		for (Iterator iter = packages.iterator(); iter.hasNext(); )
		{
			IPackage pkg = (IPackage) iter.next();
			IResource packageFile = pkg.getPackageResource();
			
			if (packageFile.exists())
			{
				try {
					packageFile.delete(true, monitor);
				} catch (CoreException e) {
					Trace.trace(getClass(), e);
				}
			}
			monitor.worked(1);
		}
		monitor.done();
	}
	
	public void buildSinglePackage (IPackage pkg, IProgressMonitor monitor)
	{
		this.project = pkg.getProject();
		
		packages = new ArrayList();
		packages.add(pkg);
		
		building = true;
		
		buildPackage(pkg, monitor);
		
		building = false;
	}
	
	public IProject[] build(int kind, Map args, IProgressMonitor monitor)
		throws CoreException {
		
		building = true;
		referencedProjects = new TreeSet(new Comparator () {
			public int compare(Object o1, Object o2) {
				if (o1.equals(o2)) return 0;
				else return -1;
			}
		});
		
		packages = PackagesModel.instance().getProjectPackages(project);

		if (packages == null) return new IProject[0];
		
		monitor.beginTask("Finding referenced projects...", packages.size());
		for (Iterator iter = packages.iterator(); iter.hasNext(); )
		{
			IPackage pkg = (IPackage) iter.next();
			referencedProjects.addAll(PackagesExport.findReferencedProjects(project, pkg));
			monitor.worked(1);
		}
		monitor.done();
		
		ArrayList packagesBeingChanged = null;
		try {
			if (kind == IncrementalProjectBuilder.INCREMENTAL_BUILD || kind == IncrementalProjectBuilder.AUTO_BUILD)
			{
				events.fireStartedBuild(project);
				packagesBeingChanged = incrementalBuild(args, monitor);
			}
			else if (kind == IncrementalProjectBuilder.CLEAN_BUILD)
			{
				cleanBuild(args, monitor);
			}
			else if (kind == IncrementalProjectBuilder.FULL_BUILD)
			{
				events.fireStartedBuild(project);
				fullBuild(args, monitor);
			}
		} finally {
			TruezipUtil.umountAll();
		}
		
		if (packagesBeingChanged != null)
		{
			// alert this package is being changed
			for( int i = 0; i < packagesBeingChanged.size(); i++ ) {
				IPackage p = (IPackage)packagesBeingChanged.get(i);
				events.fireFinishedBuildingPackage(p);
			}
	
			for (Iterator iter = packagesBeingChanged.iterator(); iter.hasNext(); )
			{
				IPackage pkg = (IPackage) iter.next();
				BuildFileOperations.refreshPackage(pkg);
			}
		}
		
		events.fireFinishedBuild(project);
		
		building = false;
		delta = null;
		return (IProject[])referencedProjects.toArray(new IProject[referencedProjects.size()]);
	}
	
	public boolean isBuilding() {
		return building;
	}
	
	private void processDelta (final IProject project, final ArrayList packagesBeingChanged, final IProgressMonitor monitor)
	{
		if (delta != null)
		{
			try {
				delta.accept(new IResourceDeltaVisitor () { 
					public boolean visit(IResourceDelta delta) throws CoreException {
						if (delta.getResource().getType() == IResource.FILE)  {
							processFileDelta(project, delta, packagesBeingChanged, monitor);
						} else if (delta.getResource().getType() == IResource.PROJECT) {
							if ((delta.getKind() & IResourceDelta.REMOVED) > 0)
							{
								processProjectRemoved ((IProject) delta.getResource());
							}
						}
						return true;
					}
				});
			} catch (CoreException e) {
				Trace.trace(getClass(), e);
			}
		}
	}
	
	private Hashtable filesToCopy, filesToRemove;
	/*
	 * This method is responsible for throwing the proper events itself
	 * as to which packages are going to be changed.
	 */
	private ArrayList incrementalBuild (Map args, final IProgressMonitor monitor)
	{
		filesToCopy = new Hashtable();
		filesToRemove = new Hashtable();
		ArrayList packagesBeingChanged = new ArrayList();
		for (Iterator iter = referencedProjects.iterator(); iter.hasNext(); )
		{
			IProject project = (IProject) iter.next();
			processDelta(project, packagesBeingChanged, monitor);
		}
		
		processDelta(this.project, packagesBeingChanged, monitor);
		
		// alert this package is being changed
		for( int i = 0; i < packagesBeingChanged.size(); i++ ) {
			IPackage p = (IPackage)packagesBeingChanged.get(i);
			events.fireStartedBuildingPackage(p);
		}
		
		for (Iterator iter = filesToCopy.keySet().iterator(); iter.hasNext(); )	
		{
			IFile file = (IFile)iter.next();
			IPackageFileSet[] filesets = (IPackageFileSet[]) filesToCopy.get(file);
			
			fileOperations.updateFileInFilesets(file, filesets, false);
		}
		
		for (Iterator iter = filesToRemove.keySet().iterator(); iter.hasNext(); )
		{
			IFile file = (IFile)iter.next();
			IPackageFileSet[] filesets = (IPackageFileSet[]) filesToRemove.get(file);
			
			fileOperations.removeFileFromFilesets(file, filesets);
		}

		filesToCopy.clear();
		filesToRemove.clear();
		
		return packagesBeingChanged;
	}
	
	private void processFileDelta (IProject project, IResourceDelta delta, ArrayList packagesBeingChanged, IProgressMonitor monitor)
	{
		IFile file = (IFile) delta.getResource();
		
		Trace.trace(getClass(), "processing file delta for file \"" + file.getName() + "\"");
		
		IPackageFileSet[] filesets = new IPackageFileSet[0];
		if ((delta.getKind() & IResourceDelta.REMOVED) > 0)
		{
			try {
				filesets = findMatchingFilesetsForRemovedFile(file);
			} catch (Exception e) {
				Trace.trace(getClass(), e);
			}
		} else {
			filesets = findMatchingFilesets(file);
		}
		
		IPackage pkg = PackagesCore.getPackage(file);
		if ((delta.getKind() & IResourceDelta.REMOVED) > 0)
		{
			if (pkg != null)
			{
				buildPackage(pkg, monitor);
				return;
			}
		}
		
		if (filesets != null && filesets.length > 0)
		{
			// Is this right?? Is the parent guarenteed to be a package?
			for( int i = 0; i < filesets.length; i++ ) {
				IPackage parentPackage = PackagesCore.getParentPackage(filesets[i]);
				packagesBeingChanged.add(parentPackage);
			}
			
			if ((delta.getKind() & IResourceDelta.ADDED) > 0)
			{
				filesToCopy.put(file, filesets);
			}
			else if ((delta.getKind() & IResourceDelta.CONTENT) > 0 || (delta.getKind() & IResourceDelta.CHANGED) > 0)
			{
				filesToCopy.put(file, filesets);
			}
			else if ((delta.getKind() & IResourceDelta.REMOVED) > 0)
			{
				filesToRemove.put(file, filesets);
			}
		}
	}
	
	private void processProjectRemoved (IProject project)
	{
		// For now removing model objects should be good enough, need to come back later and add support for removing any package references
		
		List packages = PackagesModel.instance().getProjectPackages(project);
		for (Iterator iter = packages.iterator(); iter.hasNext(); )
		{
			IPackage pkg = (IPackage) iter.next();
			PackagesModel.instance().removePackage(pkg, false);
		}
	}
	
	public void setProject(IProject project) {
		this.project = project;
	}

	public IProject getProject() {
		return project;
	}

	public void setDelta(IResourceDelta delta) {
		this.delta = delta;
	}

	public BuildEvents getEvents() {
		return events;
	}

	public BuildFileOperations getFileOperations() {
		return fileOperations;
	}
}
