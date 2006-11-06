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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
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
import org.jboss.ide.eclipse.packages.core.project.PackagesBuilder;
import org.jboss.ide.eclipse.packages.core.util.PackagesExport;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;
import de.schlichtherle.io.FileOutputStream;

public class PackageBuildDelegate implements IPackagesModelListener {
	private ArrayList referencedProjects;
	private Hashtable scannerCache;
	private IPackage packages[];
	private ArrayList nodesToUpdate, nodesToRemove;
	private IProject project;
	private IResourceDelta delta;
	
	
	public PackageBuildDelegate (PackagesBuilder builder)
	{
		this(builder.getProject(), builder.getDelta(builder.getProject()));
	}
	
	public PackageBuildDelegate (IProject project)
	{
		this(project, null);
	}
	
	public PackageBuildDelegate (IProject project, IResourceDelta delta)
	{
		this.project = project;
		this.delta = delta;
		
		nodesToUpdate = new ArrayList();
		nodesToRemove = new ArrayList();
		PackagesModel.instance().addPackagesModelListener(this);
	}
	
	public void packageNodeAdded(IPackageNode added) {
		nodesToUpdate.add(added);
	}
	
	public void packageNodeChanged(IPackageNode changed) {
		nodesToUpdate.add(changed);
	}
	
	public void packageNodeRemoved(IPackageNode removed) {
		nodesToRemove.add(removed);
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

	private void createScannerCache ()
	{
		scannerCache = new Hashtable();
		for (int i = 0; i < packages.length; i++)
		{
			final IPackage pkg = packages[i];
			final Hashtable filesetCache = new Hashtable();
			
			scannerCache.put(pkg, filesetCache);
			
			pkg.accept(new IPackageNodeVisitor() {
				public boolean visit(IPackageNode node) {
					if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
					{
						PackageFileSetImpl fileset = (PackageFileSetImpl) node;
						filesetCache.put(fileset, fileset.createDirectoryScanner(true));
					}
					return true;
				}
			});
		}
	}
	
	private IPackageFileSet findMatchingFileset (IFile file)
	{
		for (Iterator iter = scannerCache.keySet().iterator(); iter.hasNext(); )
		{
			IPackage pkg = (IPackage) iter.next();
			Hashtable scanners = (Hashtable) scannerCache.get(pkg);
			
			for (Iterator filesetIter = scanners.keySet().iterator(); filesetIter.hasNext(); )
			{
				PackageFileSetImpl fileset = (PackageFileSetImpl) filesetIter.next();
				DirectoryScanner scanner = (DirectoryScanner) scanners.get(fileset);
				
				if (fileset.matchesFile(scanner, file))
				{
					return fileset;
				}
			}
		}
		return null;
	}
	
	private void buildFileset (IPackageFileSet fileset, boolean checkStamps)
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
				updateFile(matchingFiles[i], fileset, checkStamps);
			}
		} else {
			fireStartedCollectingFileSet(fileset);
			IPath matchingPaths[] = ((PackageFileSetImpl)fileset).findMatchingPaths(scanner);
			fireFinishedCollectingFileSet(fileset);
			for (int i = 0; i < matchingPaths.length; i++)
			{
				updatePath(matchingPaths[i], fileset, checkStamps);
			}
		}
	}
	
	private int buildSteps;
	private int countBuildSteps (IPackage pkg)
	{
		buildSteps = 0;
		pkg.accept(new IPackageNodeVisitor () {
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
		try {
			file.refreshLocal(IResource.DEPTH_ONE, monitor);
		} catch (CoreException e) {
			Trace.trace(getClass(), e);
		}
		
		monitor.done();
		fireFinishedBuildingPackage(pkg);
	}
	
	private void fullBuild(Map args,  IProgressMonitor monitor)
	{
		for (int i = 0; i < packages.length; i++)
		{
			buildPackage(packages[i], monitor);
		}
	}
	
	private void cleanBuild(Map args, IProgressMonitor monitor)
	{
		monitor.beginTask("Cleaning packages...", packages.length);
		for (int i = 0; i < packages.length; i++)
		{
			IFile packageFile = packages[i].getPackageFile();
			
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
		packages = new IPackage[] { pkg };
		createScannerCache();
		
		buildPackage(pkg, monitor);
		
		scannerCache = null;
	}
	
	public IProject[] build(int kind, Map args, IProgressMonitor monitor)
		throws CoreException {

		referencedProjects = new ArrayList();
		
		packages = PackagesCore.getProjectPackages(project, monitor);
		monitor.beginTask("Finding referenced projects...", packages.length);
		for (int i = 0; i < packages.length; i++)
		{
			referencedProjects.addAll(PackagesExport.findReferencedProjects(project, packages[i]));
			monitor.worked(1);
		}
		monitor.done();
		
		createScannerCache();
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
		
		scannerCache = null;
		
		nodesToUpdate.clear();
		nodesToRemove.clear();
		return (IProject[])referencedProjects.toArray(new IProject[referencedProjects.size()]);
	}
	
	private Hashtable filesToCopy, filesToRemove;
	private void incrementalBuild(Map args, final IProgressMonitor monitor)
	{
		filesToCopy = new Hashtable();
		filesToRemove = new Hashtable();
		for (Iterator iter = referencedProjects.iterator(); iter.hasNext(); )
		{
			final IProject project = (IProject) iter.next();
			
			if (delta != null)
			{
				try {
					delta.accept(new IResourceDeltaVisitor () { 
						public boolean visit(IResourceDelta delta) throws CoreException {
							if (delta.getResource().getType() == IResource.FILE)
								processFileDelta(project, delta, monitor);
							return true;
						}
					});
				} catch (CoreException e) {
					Trace.trace(getClass(), e);
				}
			}
		}
		
		for (Iterator iter = filesToCopy.keySet().iterator(); iter.hasNext(); )	
		{
			IFile file = (IFile)iter.next();
			IPackageFileSet fileset = (IPackageFileSet) filesToCopy.get(file);
			
			updateFile(file, fileset, false);
		}
		
		for (Iterator iter = filesToRemove.keySet().iterator(); iter.hasNext(); )
		{
			IFile file = (IFile)iter.next();
			IPackageFileSet fileset = (IPackageFileSet) filesToRemove.get(file);
			
			removeFile(file, fileset);
		}
		
		for (Iterator iter = nodesToUpdate.iterator(); iter.hasNext(); )
		{
			IPackageNode node = (IPackageNode) iter.next();
			updateNode(node);
		}
		
		for (Iterator iter = nodesToRemove.iterator(); iter.hasNext(); )
		{
			IPackageNode node = (IPackageNode) iter.next();
			removeNode(node);
		}

		filesToCopy.clear();
		filesToRemove.clear();
	}
	
	private IPackage getPackageFromFile (IFile file)
	{
		for (int i = 0; i < packages.length; i++)
		{
			if (packages[i].getName().equals(file.getName()) && packages[i].getDestinationContainer().equals(file.getParent()))
			{
				return packages[i];
			}
		}
		return null;
	}
	
	private void processFileDelta (IProject project, IResourceDelta delta, IProgressMonitor monitor)
	{
		IFile file = (IFile) delta.getResource();
		IPackageFileSet fileset = findMatchingFileset(file);
		
		IPackage pkg = getPackageFromFile(file);
		if ((delta.getFlags() & IResourceDelta.REMOVED) > 0)
		{
			if (pkg != null)
			{
				buildPackage(pkg, monitor);
				return;
			}
		}
		
		if (fileset != null)
		{
			if ((delta.getFlags() & IResourceDelta.ADDED) > 0)
			{
				filesToCopy.put(file, fileset);
			}
			else if ((delta.getFlags() & IResourceDelta.CONTENT) > 0 || (delta.getFlags() & IResourceDelta.CHANGED) > 0)
			{
				filesToCopy.put(file, fileset);
			}
			else if ((delta.getFlags() & IResourceDelta.REMOVED) > 0)
			{
				filesToRemove.put(file, fileset);
			}
		}
	}
	
	private File createFilesetRoot (IPackageFileSet fileset)
	{
		ArrayList parents = new ArrayList();
		
		IPackageNode parent = fileset.getParent();
		while (parent != null)
		{
			parents.add(0, parent);
			
			parent = parent.getParent();
		}
		
		IPackage topLevelPackage = PackagesCore.getTopLevelPackage(fileset);
		File root = null;
		if (topLevelPackage.isDestinationInWorkspace())
		{
			IPath projectPath = ProjectUtil.getProjectLocation(topLevelPackage.getProject());
			IPath subPath = topLevelPackage.getDestinationContainer().getProjectRelativePath();
			root = new File(projectPath.append(subPath).toFile());
		} else {
			root = new File(topLevelPackage.getDestinationFolder().toFile());
		}
		
		for (Iterator iter = parents.iterator(); iter.hasNext(); )
		{
			IPackageNode currentParent = (IPackageNode) iter.next();
			
			if (currentParent.getNodeType() == IPackageNode.TYPE_PACKAGE) {
				IPackage pkg = (IPackage)currentParent;
				root = new File(root, pkg.getName(), pkg.isExploded() ? ArchiveDetector.NULL : ArchiveDetector.DEFAULT);
			} else if (currentParent.getNodeType() == IPackageNode.TYPE_PACKAGE_FOLDER) {
				IPackageFolder folder = (IPackageFolder)currentParent;
				root = new File(root, folder.getName(), ArchiveDetector.NULL);
			}
		}
		
		return root;
	}
	
	private File createFile (IPackageFileSet fileset, IPath subPath)
	{
		return new File(createFilesetRoot(fileset), subPath.toString(), ArchiveDetector.NULL);
	}
	
	private OutputStream createFileOutputStream (IPackageFileSet fileset, IPath subPath)
	{
		File root = createFilesetRoot(fileset);
		root = new File(root, subPath.toString(), ArchiveDetector.NULL);
		try {
			return new FileOutputStream(root);
		} catch (FileNotFoundException e) {
			Trace.trace(getClass(), e);
		}
		return null;
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
		IPath filePath = file.getProjectRelativePath();
		IPath copyTo = filePath.removeFirstSegments(fileset.getSourceContainer().getProjectRelativePath().segmentCount()).removeLastSegments(1);
		copyTo = copyTo.append(file.getName());
		copyTo = copyTo.setDevice(null);
		
		return copyTo;
	}
	
	private IPath getPathDestinationPath (IPath path, IPackageFileSet fileset)
	{
		IPath copyTo = path.removeFirstSegments(fileset.getSourceFolder().segmentCount()).removeLastSegments(1);
		copyTo = copyTo.append(path.lastSegment());
		copyTo = copyTo.setDevice(null);
		
		return copyTo;
	}
	
	private void updateFile(IFile file, IPackageFileSet fileset, boolean checkStamps)
	{
		IPath copyTo = getFileDestinationPath(file, fileset);
		
		if (checkStamps)
		{
			File packageFile = createFile(fileset, copyTo);
			long stamp = file.getModificationStamp();
			if (stamp != IResource.NULL_STAMP && packageFile.exists() && stamp >= packageFile.lastModified())
			{
				return;
			}
		}
		
		Trace.trace(getClass(), "copying " + file.getProjectRelativePath().toString() + " ...");
		
		InputStream in = null;
		OutputStream out = null;
		// I'm using the fully qualified package name here to avoid confusion with java.io
		try {
			try {
				in = file.getContents();
				out = createFileOutputStream(fileset, copyTo);
				File.cp(in, out);
			} catch (FileNotFoundException e) {
				Trace.trace(getClass(), e);
			} catch (IOException e) {
				Trace.trace(getClass(), e);
			} catch (CoreException e) {
				Trace.trace(getClass(), e);
			}
		} finally {
			try {
				if (in != null) {
					Trace.trace(getClass(), "closing file contents inputstream", Trace.DEBUG_OPTION_STREAM_CLOSE);
					in.close();
				}
				if (out != null) {
					Trace.trace(getClass(), "closing package file outputstream", Trace.DEBUG_OPTION_STREAM_CLOSE);
					out.close();
				}
			} catch (IOException e) {
				Trace.trace(getClass(), e);
			}
		}
	}
	
	private void updatePath(IPath path, IPackageFileSet fileset, boolean checkStamps)
	{
		IPath copyTo = getPathDestinationPath(path, fileset);
		
		if (checkStamps)
		{
			File packageFile = createFile(fileset, copyTo);
			File externalFile = new File(path.toFile());
			
			long stamp = externalFile.lastModified();
			if (packageFile.exists() && stamp >= packageFile.lastModified())
			{
				return;
			}
		}
		
		Trace.trace(getClass(), "copying " + path.toString() + " ...");
		
		InputStream in = null;
		OutputStream out = null;
		try {
			try {
				in = new FileInputStream(path.toFile());
				out = createFileOutputStream(fileset, copyTo);
				File.cp(in, out);
			} catch (FileNotFoundException e) {
				Trace.trace(getClass(), e);
			} catch (IOException e) {
				Trace.trace(getClass(), e);
			}
		} finally {
			try {
				if (in != null) {
					Trace.trace(getClass(), "closing file contents input stream", Trace.DEBUG_OPTION_STREAM_CLOSE);
					in.close();
				}
				if (out != null) {
					Trace.trace(getClass(), "closing package file outputstream", Trace.DEBUG_OPTION_STREAM_CLOSE);
					out.close();
				}
			} catch (IOException e) {
				Trace.trace(getClass(), e);
			}
		}
	}
	
	private void removeFile(IFile file, IPackageFileSet fileset)
	{
		File packagedFile = new File(getFileDestinationPath(file, fileset).toFile());
		
		if (packagedFile.exists()) packagedFile.deleteAll();
	}
	
	private void removePath(IPath path, IPackageFileSet fileset)
	{
		File packagedFile = new File(getPathDestinationPath(path, fileset).toFile());
		if (packagedFile.exists()) packagedFile.deleteAll();
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
				removeFile(matchingFiles[i], fileset);
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

}
