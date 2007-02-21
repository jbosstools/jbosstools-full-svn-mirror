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
package org.jboss.ide.eclipse.packages.core.model.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.util.ProjectUtil;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.IPackagesBuildListener;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelListener;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.util.PackagesExport;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileOutputStream;

public class PackageBuildDelegate implements IPackagesModelListener {
	private static PackageBuildDelegate _instance;
	

	private Hashtable scannerCache;
	private Hashtable oldScanners;
	private Object buildLock = new Object();
	private boolean building;
	private ArrayList nodesToUpdate, nodesToRemove;
	private NullProgressMonitor nullMonitor = new NullProgressMonitor();
	// 
	private TreeSet referencedProjects;
	private List packages;
	private IProject project;
	private IResourceDelta delta;
	
	public static PackageBuildDelegate instance ()
	{
		if (_instance == null)
			_instance = new PackageBuildDelegate();
		
		return _instance;
	}
	
	public PackageBuildDelegate () {
		scannerCache = new Hashtable();
		oldScanners = new Hashtable();
		nodesToUpdate = new ArrayList();
		nodesToRemove = new ArrayList();

		PackagesModel.instance().addPackagesModelListener(this);
	}
	
	public void projectRegistered(IProject project) {
		List packages = PackagesModel.instance().getProjectPackages(project);
		fillScannerCache(packages);
		
		if (packages != null)
		{
			for (Iterator iter = packages.iterator(); iter.hasNext(); )
			{
				buildSinglePackage((IPackage)iter.next(), nullMonitor);
			}
		}
	}
	
	public void packageNodeAdded(IPackageNode added) {
		synchronized (nodesToUpdate)
		{
			nodesToUpdate.add(added);
		}
		
		if (added.getNodeType() == IPackageNode.TYPE_PACKAGE)
		{
			fillScannerCache((IPackage) added);
		}
		
		incrementalBuildUnderNode(added, nullMonitor);
	}
	
	public void packageNodeChanged(IPackageNode changed) {
		synchronized (nodesToUpdate)
		{
			nodesToUpdate.add(changed);
		}
		
		if (changed.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
		{
			updateScannerCache((IPackageFileSet) changed);
		}
		
		incrementalBuildUnderNode(changed, nullMonitor);
	}
	
	public void packageNodeRemoved(IPackageNode removed) {
		synchronized (nodesToRemove)
		{
			nodesToRemove.add(removed);
		}
		
		if (removed.getNodeType() == IPackageNode.TYPE_PACKAGE
			|| removed.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
		{
			removeScannerCache(removed);
		}
		else if (removed.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
		{
			removeFilesetScannerCache((IPackageFileSet)removed);
		}
		incrementalBuildUnderNode(removed, nullMonitor);
	}
	
	public void packageNodeAttached(IPackageNode attached) {
		packageNodeAdded(attached);
	}
	
	private void fireStartedBuild (IProject project)
	{
		for (Iterator iter = PackagesModel.instance().getBuildListeners().iterator(); iter.hasNext(); )
			((IPackagesBuildListener)iter.next()).startedBuild(project);
	}
	
	private void fireStartedBuildingPackage (IPackage pkg)
	{
		for (Iterator iter = PackagesModel.instance().getBuildListeners().iterator(); iter.hasNext(); )
			((IPackagesBuildListener)iter.next()).startedBuildingPackage(pkg);
	}
	
	private void fireStartedCollectingFileSet (IPackageFileSet fileset)
	{
		for (Iterator iter = PackagesModel.instance().getBuildListeners().iterator(); iter.hasNext(); )
			((IPackagesBuildListener)iter.next()).startedCollectingFileSet(fileset);
	}
	
	private void fireFinishedCollectingFileSet (IPackageFileSet fileset)
	{
		for (Iterator iter = PackagesModel.instance().getBuildListeners().iterator(); iter.hasNext(); )
			((IPackagesBuildListener)iter.next()).finishedCollectingFileSet(fileset);
	}
	
	private void fireFinishedBuildingPackage (IPackage pkg)
	{
		for (Iterator iter = PackagesModel.instance().getBuildListeners().iterator(); iter.hasNext(); )
			((IPackagesBuildListener)iter.next()).finishedBuildingPackage(pkg);
	}
	
	private void fireFinishedBuild (IProject project)
	{
		for (Iterator iter = PackagesModel.instance().getBuildListeners().iterator(); iter.hasNext(); )
			((IPackagesBuildListener)iter.next()).finishedBuild(project);
	}

	private void fireBuildFailed (IPackage pkg, IStatus status)
	{
		for (Iterator iter = PackagesModel.instance().getBuildListeners().iterator(); iter.hasNext(); )
			((IPackagesBuildListener)iter.next()).buildFailed(pkg, status);
	}
	
	private void fillScannerCache (List packages)
	{
		if (packages == null) return;
		
		for (Iterator iter = packages.iterator(); iter.hasNext(); )
		{
			final IPackage pkg = (IPackage) iter.next();
			
			fillScannerCache(pkg);
		}
		
		Trace.trace(PackageBuildDelegate.class, "scannerCache = " + scannerCache);
	}
	
	private void fillScannerCache (IPackage pkg)
	{
		final Hashtable filesetCache = new Hashtable();
		
		scannerCache.put(pkg, filesetCache);
		
		pkg.accept(new IPackageNodeVisitor() {
			public boolean visit(IPackageNode node) {
				if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
				{
					PackageFileSetImpl fileset = (PackageFileSetImpl) node;
					
					Trace.trace(getClass(), "adding " + fileset + " @" + fileset.hashCode() + " to scanner cache.");
					filesetCache.put(fileset, fileset.createDirectoryScanner(true));
				}
				return true;
			}
		});
	}
	
	/**
	 * @return The old directory scanner for this fileset (or null if one did not exist)
	 */
	private void updateScannerCache (IPackageFileSet fileset)
	{
		IPackage topPackage = PackagesCore.getTopLevelPackage(fileset);
		if (topPackage != null && scannerCache.containsKey(topPackage))
		{
			Hashtable filesetCache = (Hashtable) scannerCache.get(topPackage);
			
			if (filesetCache != null)
			{
				DirectoryScanner scanner = null;
				if (filesetCache.containsKey(fileset))
				{
					scanner = (DirectoryScanner) filesetCache.get(fileset);
				}
				
				filesetCache.put(fileset, ((PackageFileSetImpl)fileset).createDirectoryScanner(true));
				
				if (scanner != null)
				{
					oldScanners.put(fileset, scanner);
				}
			}
		}
	}
	
	private void removeScannerCache (IPackageNode node)
	{
		node.accept(new IPackageNodeVisitor() {
			public boolean visit(IPackageNode node) {
				if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
					removeFilesetScannerCache((IPackageFileSet) node);
				return true;
			}
		});	
	}
	
	private void removeFilesetScannerCache (IPackageFileSet fileset) 
	{
		IPackage topPackage = PackagesCore.getTopLevelPackage(fileset);
		if (topPackage != null && scannerCache.containsKey(topPackage))
		{
			Hashtable filesetCache = (Hashtable) scannerCache.get(topPackage);
			
			if (filesetCache != null)
			{
				DirectoryScanner scanner = null;
				if (filesetCache.containsKey(fileset))
				{
					scanner = (DirectoryScanner) filesetCache.remove(fileset);
				}
				
				if (scanner != null)
				{
					oldScanners.put(fileset, scanner);
				}
			}
		}
	}
	
	private IPackageFileSet[] findMatchingFilesetsForRemovedFile (IFile removedFile)
	{
		ArrayList filesets = new ArrayList();
		
		for (Iterator iter = scannerCache.keySet().iterator(); iter.hasNext(); )
		{
			IPackage pkg = (IPackage) iter.next();
			Hashtable scanners = (Hashtable) scannerCache.get(pkg);
			
			for (Iterator filesetIter = scanners.keySet().iterator(); filesetIter.hasNext(); )
			{
				PackageFileSetImpl fileset = (PackageFileSetImpl) filesetIter.next();
				
				IContainer root = fileset.getSourceContainer();
				IPath relativePath = removedFile.getFullPath();
				IPath rootPath = root.getFullPath();
				relativePath = relativePath.removeFirstSegments(rootPath.segmentCount());
				
				boolean matchesIncludes = DirectoryScanner.match(fileset.getIncludesPattern(), relativePath.toString());
				boolean matchesExcludes = false;
				if (fileset.getExcludesPattern() != null && fileset.getExcludesPattern().length() > 0)
				{
					matchesExcludes = DirectoryScanner.match(fileset.getExcludesPattern(), relativePath.toString());
				}
				
				if (matchesIncludes && !matchesExcludes)
				{
					filesets.add(fileset);
				}
			}
		}
		
		return (IPackageFileSet[]) filesets.toArray(new IPackageFileSet[filesets.size()]);
	}
	
	private IPackageFileSet[] findMatchingFilesets (IFile file, boolean rescan)
	{
		ArrayList filesets = new ArrayList();
		
		for (Iterator iter = scannerCache.keySet().iterator(); iter.hasNext(); )
		{
			IPackage pkg = (IPackage) iter.next();
			Hashtable scanners = (Hashtable) scannerCache.get(pkg);
			
			for (Iterator filesetIter = scanners.keySet().iterator(); filesetIter.hasNext(); )
			{
				PackageFileSetImpl fileset = (PackageFileSetImpl) filesetIter.next();
				DirectoryScanner scanner = (DirectoryScanner) scanners.get(fileset);
				
				if (rescan) scanner.scan();
				
				if (fileset.matchesFile(scanner, file))
				{
					filesets.add(fileset);
				}
			}
		}
		
		return (IPackageFileSet[]) filesets.toArray(new IPackageFileSet[filesets.size()]);
	}
	
	private void buildFileset (IPackageFileSet fileset, boolean checkStamps)
	{
		IPackage pkg = PackagesCore.getTopLevelPackage(fileset);
		DirectoryScanner scanner = (DirectoryScanner) ((Hashtable)scannerCache.get(pkg)).get(fileset);
		DirectoryScanner oldScanner = (DirectoryScanner) oldScanners.get(fileset);
		PackageFileSetImpl filesetImpl = (PackageFileSetImpl) fileset;
		
		if (oldScanner != null)
		{
			if (fileset.isInWorkspace())
			{
				IFile oldFiles[] = filesetImpl.findMatchingFiles(oldScanner);
				for (int i = 0; i < oldFiles.length; i++)
				{
					removeFile(oldFiles[i], new IPackageFileSet[] { fileset });
				}
			} else {
				IPath oldPaths[] = filesetImpl.findMatchingPaths(oldScanner);
				for (int i = 0; i < oldPaths.length; i++)
				{
					removePath(oldPaths[i], fileset);
				}
			}
		}
		
		if (fileset.isInWorkspace())
		{
			fireStartedCollectingFileSet(fileset);	
			IFile matchingFiles[] = filesetImpl.findMatchingFiles(scanner);
			fireFinishedCollectingFileSet(fileset);
			for (int i = 0; i < matchingFiles.length; i++)
			{
				updateFile(matchingFiles[i], new IPackageFileSet[] { fileset }, checkStamps);
			}
		} else {
			fireStartedCollectingFileSet(fileset);
			IPath matchingPaths[] = filesetImpl.findMatchingPaths(scanner);
			fireFinishedCollectingFileSet(fileset);
			for (int i = 0; i < matchingPaths.length; i++)
			{
				updatePath(matchingPaths[i], fileset, checkStamps);
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
		fireStartedBuildingPackage(pkg);
		
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
		
		IFile file = pkg.getPackageFile();
		if (file != null)
		{
			try {
				file.refreshLocal(IResource.DEPTH_ONE, monitor);
			} catch (CoreException e) {
				Trace.trace(getClass(), e);
			}
		}
		
		try {
			File.umount();
		} catch (ArchiveException e) {
			Trace.trace(getClass(), e);
		}
		
		monitor.done();
		fireFinishedBuildingPackage(pkg);
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
			IFile packageFile = pkg.getPackageFile();
			
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
		
		buildPackage(pkg, monitor);
	}
	
	protected void incrementalBuildUnderNode (IPackageNode node, final IProgressMonitor monitor)
	{
		IPackage topLevelPackage = PackagesCore.getTopLevelPackage(node);
		packages = new ArrayList();
		packages.add(topLevelPackage);
		this.project = topLevelPackage.getProject();
		
		referencedProjects = new TreeSet();
		
		fireStartedBuildingPackage(topLevelPackage);
		incrementalBuild(null, monitor);
		fireFinishedBuildingPackage(topLevelPackage);
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
		
		try {
			if (kind == IncrementalProjectBuilder.INCREMENTAL_BUILD || kind == IncrementalProjectBuilder.AUTO_BUILD)
			{
				fireStartedBuild(project);
				incrementalBuild(args, monitor);
				fireFinishedBuild(project);
			}
			else if (kind == IncrementalProjectBuilder.CLEAN_BUILD)
			{
				cleanBuild(args, monitor);
			}
			else if (kind == IncrementalProjectBuilder.FULL_BUILD)
			{
				fireStartedBuild(project);
				fullBuild(args, monitor);
				fireFinishedBuild(project);
			}
		} finally {
			try {
				File.umount();
			} catch (ArchiveException e) {
				Trace.trace(getClass(), e);
			}
		}
		
		synchronized (nodesToUpdate)
		{
			nodesToUpdate.clear();
		}
		synchronized (nodesToRemove)
		{
			nodesToRemove.clear();
		}
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
						if (delta.getResource().getType() == IResource.FILE)
							processFileDelta(project, delta, packagesBeingChanged, monitor);
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
	private void incrementalBuild(Map args, final IProgressMonitor monitor)
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
			fireStartedBuildingPackage(p);
		}
		
		
		for (Iterator iter = filesToCopy.keySet().iterator(); iter.hasNext(); )	
		{
			IFile file = (IFile)iter.next();
			IPackageFileSet[] filesets = (IPackageFileSet[]) filesToCopy.get(file);
			
			updateFile(file, filesets, false);
		}
		
		for (Iterator iter = filesToRemove.keySet().iterator(); iter.hasNext(); )
		{
			IFile file = (IFile)iter.next();
			IPackageFileSet[] filesets = (IPackageFileSet[]) filesToRemove.get(file);
			
			removeFile(file, filesets);
		}
		
		synchronized (nodesToUpdate)
		{
			for (Iterator iter = nodesToUpdate.iterator(); iter.hasNext(); )
			{
				IPackageNode node = (IPackageNode) iter.next();
				updateNode(node);
			}
		}
		
		synchronized (nodesToRemove)
		{
			for (Iterator iter = nodesToRemove.iterator(); iter.hasNext(); )
			{
				IPackageNode node = (IPackageNode) iter.next();
				removeNode(node);
			}
		}

		filesToCopy.clear();
		filesToRemove.clear();
		
		// alert this package is being changed
		for( int i = 0; i < packagesBeingChanged.size(); i++ ) {
			IPackage p = (IPackage)packagesBeingChanged.get(i);
			fireFinishedBuildingPackage(p);
		}

	}
	
	private void processFileDelta (IProject project, IResourceDelta delta, ArrayList packagesBeingChanged, IProgressMonitor monitor)
	{
		IFile file = (IFile) delta.getResource();
		
		Trace.trace(getClass(), "processing file delta for file \"" + file.getName() + "\"");
		
		IPackageFileSet[] filesets = new IPackageFileSet[0];
		if ((delta.getKind() & IResourceDelta.REMOVED) > 0)
		{
			filesets = findMatchingFilesetsForRemovedFile(file); 
		} else {
			filesets = findMatchingFilesets(file,true);
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
	
	private File[] createFilesetRoots (IPackageFileSet fileset)
	{
		ArrayList roots = new ArrayList();
		
		Hashtable pkgsAndPaths = PackagesModel.instance().getTopLevelPackagesAndPathways(fileset);
		for (Iterator iter = pkgsAndPaths.keySet().iterator(); iter.hasNext(); )
		{
			IPackage topLevelPackage = (IPackage) iter.next();
			ArrayList pathway = (ArrayList) pkgsAndPaths.get(topLevelPackage);
			
			File root = null;
			if (topLevelPackage.isDestinationInWorkspace())
			{
				IPath projectPath = ProjectUtil.getProjectLocation(topLevelPackage.getProject());
				IPath subPath = topLevelPackage.getDestinationContainer().getProjectRelativePath();
				root = new File(projectPath.append(subPath).toFile());
			} else {
				root = new File(topLevelPackage.getDestinationPath().toFile());
			}
			
			for (Iterator iter2 = pathway.iterator(); iter2.hasNext(); )
			{
				IPackageNode currentParent = (IPackageNode) iter2.next();
				
				if (currentParent.getNodeType() == IPackageNode.TYPE_PACKAGE
					|| currentParent.getNodeType() == IPackageNode.TYPE_PACKAGE_REFERENCE) {
					IPackage pkg = (IPackage)currentParent;
					root = new File(root, pkg.getName(), pkg.isExploded() ? ArchiveDetector.NULL : ArchiveDetector.DEFAULT);
				} else if (currentParent.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER) {
					IPackageFolder folder = (IPackageFolder)currentParent;
					root = new File(root, folder.getName(), ArchiveDetector.NULL);
				}
			}
			
			roots.add(root);
		}
		
		return (File[]) roots.toArray(new File[roots.size()]);
	}
	
	private File[] createFiles (IPackageFileSet fileset, IPath subPath)
	{
		ArrayList files = new ArrayList();
		File[] roots = createFilesetRoots(fileset);
		for (int i = 0; i < roots.length; i++)
		{
			files.add(new File(roots[i], subPath.toString(), ArchiveDetector.NULL));
		}
		
		return (File[]) files.toArray(new File[files.size()]); 
	}
	
	private OutputStream[] createFileOutputStreams (IPackageFileSet fileset, IPath subPath)
	{
		ArrayList streams = new ArrayList();
		File[] files = createFiles(fileset, subPath);
		
		for (int i = 0; i < files.length; i++)
		{
			try {
				streams.add(new FileOutputStream(files[i]));
			} catch (FileNotFoundException e) {
				Trace.trace(getClass(), e);
			}	
		}
		
		return (OutputStream[]) streams.toArray(new OutputStream[streams.size()]);
	}
	
//	private IPath getNode333ParentPath (IPackageNode node)
//	{
//		IPackageNode parent = node.getParent();
//		IPath parentPath = null;
//		if (parent.getNodeType() == IPackageNode.TYPE_PACKAGE)
//		{
//			parentPath = ((IPackage)parent).getPackageRelativePath();
//		}
//		else if (parent.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
//		{
//			parentPath = ((IPackageFolder)parent).getPackageRelativePath();
//		}
//				
//		IPackage topLevelPackage = PackagesCore.getTopLevelPackage(node);
//		
//		IContainer destination = topLevelPackage.getDestinationContainer();
//		IPath fullPath = destination.getProjectRelativePath();
//		if (destination.equals(topLevelPackage.getProject()))
//		{
//			fullPath = ProjectUtil.getProjectLocation(topLevelPackage.getProject());
//		}
//		
//		if (parentPath != null)
//			fullPath = fullPath.append(parentPath);
//		
//		return fullPath;
//	}
	
	private IPath getFileDestinationPath (IFile file, IPackageFileSet fileset)
	{
		if (fileset.isSingleFile())
		{
			return new Path(file.getName());
		} else {
			IPath filePath = file.getProjectRelativePath();
			IPath copyTo = filePath.removeFirstSegments(fileset.getSourceContainer().getProjectRelativePath().segmentCount()).removeLastSegments(1);
			copyTo = copyTo.append(file.getName());
			copyTo = copyTo.setDevice(null);
			
			return copyTo;
		}
	}
	
	private IPath getPathDestinationPath (IPath path, IPackageFileSet fileset)
	{
		IPath copyTo = path.removeFirstSegments(fileset.getSourcePath().segmentCount()).removeLastSegments(1);
		copyTo = copyTo.append(path.lastSegment());
		copyTo = copyTo.setDevice(null);
		
		return copyTo;
	}
	
	private void updateFile(IFile file, IPackageFileSet[] filesets, boolean checkStamps)
	{
		synchronized (buildLock)
		{
			for (int i = 0; i < filesets.length; i++)
			{
				IPath copyTo = getFileDestinationPath(file, filesets[i]);
				
				if (checkStamps)
				{
					File[] packageFiles = createFiles(filesets[i], copyTo);
					for (int j = 0; j < packageFiles.length; j++)
					{
						File packageFile = packageFiles[j];
						long stamp = file.getModificationStamp();
						if (stamp != IResource.NULL_STAMP && packageFile.exists() && stamp >= packageFile.lastModified())
						{
							return;
						}
					}
				}
				
				Trace.trace(getClass(), "copying " + file.getProjectRelativePath().toString() + " ...");
				
				InputStream in = null;
				OutputStream[] outStreams = null;
				// I'm using the fully qualified package name here to avoid confusion with java.io
				try {
					outStreams = createFileOutputStreams(filesets[i], copyTo);
					
					for (int j = 0; j < outStreams.length; j++)
					{
						try {
							in = file.getContents();
							File.cp(in, outStreams[j]);
							
							Trace.trace(getClass(), "closing file contents inputstream", Trace.DEBUG_OPTION_STREAM_CLOSE);
							in.close();
						} catch (FileNotFoundException e) {
							Trace.trace(getClass(), e);
						} catch (IOException e) {
							Trace.trace(getClass(), e);
						}
					}
				} catch (CoreException e) { 
					Trace.trace(getClass(), e);
				} finally {
					try {
						if (outStreams != null) {
							Trace.trace(getClass(), "closing package file outputstreams", Trace.DEBUG_OPTION_STREAM_CLOSE);
							for (int j = 0; j < outStreams.length; j++)
							{
								outStreams[j].close();
							}
						}
					} catch (IOException e) {
						Trace.trace(getClass(), e);
					}
				}
			}
		}
	}
	
	private void updatePath(IPath path, IPackageFileSet fileset, boolean checkStamps)
	{
		synchronized (buildLock)
		{
			IPath copyTo = getPathDestinationPath(path, fileset);
			
			if (checkStamps)
			{
				File[] packageFiles = createFiles(fileset, copyTo);
				File externalFile = new File(path.toFile());
				
				for (int i = 0; i < packageFiles.length; i++)
				{
					File packageFile = packageFiles[i];
					long stamp = externalFile.lastModified();
					if (packageFile.exists() && stamp >= packageFile.lastModified())
					{
						return;
					}
				}
			}
			
			Trace.trace(getClass(), "copying " + path.toString() + " ...");
			
			InputStream in = null;
			OutputStream[] outStreams = null;
			try {
				outStreams = createFileOutputStreams(fileset, copyTo);
				for (int i = 0; i < outStreams.length; i++)
				{
					try {
						in = new FileInputStream(path.toFile());
						File.cp(in, outStreams[i]);
						
						Trace.trace(getClass(), "closing file contents input stream", Trace.DEBUG_OPTION_STREAM_CLOSE);
						in.close();
					} catch (IOException e) {
						Trace.trace(getClass(), e);
					}
				}
			} finally {
				try {
					if (outStreams != null) {
						Trace.trace(getClass(), "closing package file outputstream", Trace.DEBUG_OPTION_STREAM_CLOSE);
						for (int i = 0; i < outStreams.length; i++)
						{
							outStreams[i].close();
						}
					}
				} catch (IOException e) {
					Trace.trace(getClass(), e);
				}
			}
		}
	}
	
	private void deleteEmptyFolders (File child)
	{
		File parent = (File) child.getParentFile();
		
		while (parent != null && parent.exists())
		{
			if (parent.isDirectory())
			{
				if (parent.list().length == 0) {
					parent.delete();
				}
			}
			parent = (File) parent.getParentFile();
		}
	}
	
	private synchronized void removeFile(IFile file, IPackageFileSet[] filesets)
	{
		synchronized (buildLock)
		{
			for (int i = 0; i < filesets.length; i++)
			{
				File[] packagedFiles = createFiles(filesets[i], getFileDestinationPath(file, filesets[i]));
				
				for (int j = 0; j < packagedFiles.length; j++)
				{
					File packagedFile = packagedFiles[j];
					if (packagedFile.exists()) packagedFile.deleteAll();
					deleteEmptyFolders(packagedFile);
				}
			}
		}
	}
	
	private synchronized void removePath(IPath path, IPackageFileSet fileset)
	{
		synchronized (buildLock)
		{
			File packagedFile = new File(getPathDestinationPath(path, fileset).toFile());
			if (packagedFile.exists()) packagedFile.deleteAll();
			deleteEmptyFolders(packagedFile);
		}
	}
	
	private void updateNode (IPackageNode node)
	{
		if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
		{
			buildFileset((IPackageFileSet)node, true);
		}
		else {
			node.accept(new IPackageNodeVisitor () {
				public boolean visit(IPackageNode node) {
					if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
					{
						buildFileset((IPackageFileSet)node, true);
					}
					return true;
				}
			});
		}
	}
	
	private void removeNode (IPackageNode node)
	{
		NullProgressMonitor nullMonitor = new NullProgressMonitor();
		if (node.getNodeType() == IPackageNode.TYPE_PACKAGE)
		{
			IPackage pkg = (IPackage) node;
			if (pkg.isTopLevel())
			{
				IFile packageFile = pkg.getPackageFile();
				try {
					packageFile.delete(true, true, nullMonitor);
				} catch (CoreException e) {
					Trace.trace(getClass(), e);
				}
			}
			else {
				removeInnerPackage(pkg);
			}
		}
		else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
		{
			removeFileset((IPackageFileSet) node);
		}
		else if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER)
		{
			removeFolder((IPackageFolder) node);
		}
	}
	
	private void removeInnerPackage (IPackage pkg)
	{
//		IPath parentPath = getNodeParentPath(pkg);
//		File file = new File(parentPath.toFile());
//		
//		if (file.exists()) file.deleteAll();
	}
	
	private void removeFileset (IPackageFileSet fileset)
	{
		IPackage pkg = PackagesCore.getTopLevelPackage(fileset);
		DirectoryScanner scanner = (DirectoryScanner) ((Hashtable)scannerCache.get(pkg)).get(fileset);

		if (fileset.isInWorkspace())
		{
			fireStartedCollectingFileSet(fileset);
			IFile matchingFiles[] = ((PackageFileSetImpl)fileset).findMatchingFiles(scanner);
			fireFinishedCollectingFileSet(fileset);
			for (int i = 0; i < matchingFiles.length; i++)
			{
				removeFile(matchingFiles[i], new IPackageFileSet[] { fileset });
			}
		} else {
			fireStartedCollectingFileSet(fileset);
			IPath matchingPaths[] = ((PackageFileSetImpl)fileset).findMatchingPaths(scanner);
			fireFinishedCollectingFileSet(fileset);
			for (int i = 0; i < matchingPaths.length; i++)
			{
				removePath(matchingPaths[i], fileset);
			}
		}
	}
	
	private void removeFolder (IPackageFolder folder)
	{
//		IPath parentPath = getNodeParentPath(folder);
//		File file = new File(parentPath.append(folder.getName()).toFile());
//		
//		if (file.exists()) file.deleteAll();
	}

	public void setProject(IProject project) {
		this.project = project;

		fillScannerCache(PackagesModel.instance().getProjectPackages(project));
	}

	public IProject getProject() {
		return project;
	}

	public void setDelta(IResourceDelta delta) {
		this.delta = delta;
	}
}
