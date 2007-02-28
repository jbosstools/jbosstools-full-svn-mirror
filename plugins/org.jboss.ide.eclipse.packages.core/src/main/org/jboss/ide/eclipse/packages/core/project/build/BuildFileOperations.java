package org.jboss.ide.eclipse.packages.core.project.build;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.core.util.ResourceUtil;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageFolder;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.PackagesCore;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageFileSetImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.File;

public class BuildFileOperations {

	private PackageBuildDelegate builder;
	private Hashtable scannerCache;
	private NullProgressMonitor nullMonitor = new NullProgressMonitor();
	
	public BuildFileOperations (PackageBuildDelegate builder)
	{
		this.builder = builder;
		this.scannerCache = new Hashtable();
	}
	
	public void removeFileFromFilesets (IFile file, IPackageFileSet[] filesets)
	{
		removePathFromFilesets(ResourceUtil.makeAbsolute(file), filesets);
	}
	
	public synchronized void removePathFromFilesets (IPath path, IPackageFileSet[] filesets)
	{
		for (int i = 0; i < filesets.length; i++)
		{
			Hashtable pkgsAndPathways = PackagesModel.instance().getTopLevelPackagesAndPathways(filesets[i]);
			File[] packagedFiles = TruezipUtil.createFiles(filesets[i], getFilesetRelativePath(path, filesets[i]), pkgsAndPathways);
			IPackage[] topLevelPackages = (IPackage[])
				pkgsAndPathways.keySet().toArray(new IPackage[pkgsAndPathways.keySet().size()]);
				
			for (int j = 0; j < packagedFiles.length; j++)
			{
				File packagedFile = packagedFiles[j];
				if (packagedFile.exists())
				{
					packagedFile.delete();
					deleteEmptyFolders(packagedFile);
					
					IPath destPath = new Path(packagedFile.getAbsolutePath());
					builder.getEvents().fireFileRemoved(topLevelPackages[j], filesets[i], destPath);
				}
			}
		}	
	}

	public synchronized void updateFileInFilesets (IFile file, IPackageFileSet[] filesets, boolean checkStamps)
	{
		updatePathInFilesets(ResourceUtil.makeAbsolute(file), filesets, checkStamps);
	}
	
	public synchronized void updatePathInFilesets (IPath path, IPackageFileSet[] filesets, boolean checkStamps)
	{
		for (int i = 0; i < filesets.length; i++)
		{
			IPath copyTo = getFilesetRelativePath(path, filesets[i]);
			Hashtable pkgsAndPaths = PackagesModel.instance().getTopLevelPackagesAndPathways(filesets[i]);
			IPackage[] topLevelPackages = (IPackage [])
				pkgsAndPaths.keySet().toArray(new IPackage[pkgsAndPaths.keySet().size()]);
			
			File[] packageFiles = TruezipUtil.createFiles (filesets[i], copyTo, pkgsAndPaths);
			
			if (checkStamps)
			{
				File externalFile = new File(path.toFile());
				
				for (int j = 0; j < packageFiles.length; j++)
				{
					File packageFile = packageFiles[j];
					long stamp = externalFile.lastModified();
					if (stamp != IResource.NULL_STAMP && packageFile.exists() && stamp >= packageFile.lastModified())
					{
						return;
					}
				}
			}
			
			Trace.trace(getClass(), "copying " + path.toString() + " ...");
			
			InputStream in = null;
			OutputStream[] outStreams = null;
			// I'm using the fully qualified package name here to avoid confusion with java.io
			try {
				outStreams = TruezipUtil.createFileOutputStreams(packageFiles);
				
				for (int j = 0; j < outStreams.length; j++)
				{
					try {
						in = new FileInputStream(path.toFile());
						File.cp(in, outStreams[j]);
						
						Trace.trace(getClass(), "closing file contents inputstream", Trace.DEBUG_OPTION_STREAM_CLOSE);
						in.close();
						
						IPath destPath = new Path(packageFiles[j].getAbsolutePath());
						builder.getEvents().fireFileUpdated(topLevelPackages[j], filesets[i], destPath);
					} catch (FileNotFoundException e) {
						Trace.trace(getClass(), e);
					} catch (IOException e) {
						Trace.trace(getClass(), e);
					}
				}
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

	public synchronized void deleteEmptyFolders (File child)
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

	public synchronized void removeFileset (IPackageFileSet fileset)
	{
		DirectoryScanner scanner = ((PackageFileSetImpl)fileset).createDirectoryScanner(true);
		IPackageFileSet filesets[] = new IPackageFileSet[] { fileset };

		builder.getEvents().fireStartedCollectingFileSet(fileset);
		IPath matchingPaths[] = ((PackageFileSetImpl)fileset).findMatchingPaths(scanner);
		builder.getEvents().fireFinishedCollectingFileSet(fileset);
		
		for (int i = 0; i < matchingPaths.length; i++)
		{
			if (!otherFilesetsMatch(matchingPaths[i], fileset))
			{
				removePathFromFilesets(matchingPaths[i], filesets);
			}
		}
	}

	public synchronized void removeFolder (IPackageFolder folder)
		{
	//		IPath parentPath = getNodeParentPath(folder);
	//		File file = new File(parentPath.append(folder.getName()).toFile());
	//		
	//		if (file.exists()) file.deleteAll();
		}

	public synchronized void removeInnerPackage (IPackage pkg)
		{
	//		IPath parentPath = getNodeParentPath(pkg);
	//		File file = new File(parentPath.toFile());wha
	//		
	//		if (file.exists()) file.deleteAll();
		}

	public synchronized void removeNode (IPackageNode node)
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

	public synchronized void updateNode (IPackageNode node)
	{
		if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
		{
			builder.buildFileset((IPackageFileSet)node, true);
		}
		else {
			node.accept(new IPackageNodeVisitor () {
				public boolean visit(IPackageNode node) {
					if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
					{
						builder.buildFileset((IPackageFileSet)node, true);
					}
					return true;
				}
			});
		}
	}
	
	public IPath getFilesetRelativePath (IFile file, IPackageFileSet fileset)
	{
		return getFilesetRelativePath(ResourceUtil.makeAbsolute(file), fileset);
	}
	
	public IPath getFilesetRelativePath (IPath absolutePath, IPackageFileSet fileset)
	{
		if (fileset.isSingleFile())
		{
			return new Path(fileset.getDestinationFilename());
		} else {
			IPath copyTo = absolutePath.removeFirstSegments(fileset.getSourcePath().segmentCount()).removeLastSegments(1);
			copyTo = copyTo.append(absolutePath.lastSegment());
			copyTo = copyTo.setDevice(null);
			
			return copyTo;
		}
	}
	
	public IPath getTopLevelPackageRelativePath (IPath absolutePath, ArrayList pathway, IPackageFileSet fileset)
	{
		IPath filesetRelativePath = getFilesetRelativePath(absolutePath, fileset);
		IPath topPath = new Path("");
		for (Iterator iter = pathway.iterator(); iter.hasNext(); )
		{
			IPackageNode pathNode = (IPackageNode) iter.next();
			switch (pathNode.getNodeType())
			{
				case IPackageNode.TYPE_PACKAGE:
				case IPackageNode.TYPE_PACKAGE_REFERENCE:
				{
					topPath = topPath.append(((IPackage)pathNode).getName());
				} break;
				
				case IPackageNode.TYPE_PACKAGE_FOLDER:
				{
					topPath = topPath.append(((IPackageFolder)pathNode).getName());
				}
			}
		}

		topPath = topPath.append(filesetRelativePath);
		return topPath;
	}
	
	public void updateScannerCache (IPackageFileSet fileset)
	{
		scannerCache.put(fileset, ((PackageFileSetImpl)fileset).createDirectoryScanner(true));
	}
	
	public void updateScannerCache (IPackage pkg)
	{
		if (pkg.isTopLevel())
		{
			pkg.accept(new IPackageNodeVisitor () {
				public boolean visit(IPackageNode node) {
					if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
					{
						updateScannerCache((IPackageFileSet)node);
					}
					return true;
				}
			});
		}
	}
	
	private boolean otherMatches = false;
	private boolean otherFilesetsMatch (final IPath absolutePath, final IPackageFileSet current)
	{
		otherMatches = false;
		Hashtable pkgsAndPathways = PackagesModel.instance().getTopLevelPackagesAndPathways(current);
		
		for (Iterator pkgIter = pkgsAndPathways.keySet().iterator(); pkgIter.hasNext(); )
		{
			final IPackage topLevelPackage = (IPackage) pkgIter.next();
			final ArrayList pathway = (ArrayList) pkgsAndPathways.get(topLevelPackage);
			
			final IPath destPath = getTopLevelPackageRelativePath(absolutePath, pathway, current);
			
			topLevelPackage.accept(new IPackageNodeVisitor () {
				public boolean visit(IPackageNode node) {
					if (node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
					{
						IPackageFileSet fileset = (IPackageFileSet) node;
						if (fileset != current)
						{
							IPath currentDestPath = getTopLevelPackageRelativePath(absolutePath, pathway, fileset);
							if (currentDestPath.equals(destPath))
							{
								otherMatches = true;
								return false;
							}
						}
					}
					return true;
				}
			});
		}
		return otherMatches;
	}
	
	public void changePackage (IPackage pkg)
	{
		File packageFile = TruezipUtil.getPackageFile(pkg);
		
		if (! packageFile.getName().equals(pkg.getName()))
		{
			// File name was changed, rename
			File newPackageFile = new File(packageFile.getParent(), pkg.getName());
			packageFile.renameTo(newPackageFile, packageFile.getArchiveDetector());
		}
		else if (packageFile.getDelegate().isFile() && pkg.isExploded())
		{
			// Changed to exploded from compressed
			File tmpFile = new File(packageFile.getParent(), "_tmp_" + pkg.getName(), ArchiveDetector.NULL);
			File newPackageFile = new File(packageFile.getParent(), pkg.getName(), ArchiveDetector.NULL);
			
			packageFile.renameTo(tmpFile, ArchiveDetector.NULL);
			tmpFile.renameTo(newPackageFile, ArchiveDetector.NULL);
		}
		else if (packageFile.getDelegate().isDirectory() && !pkg.isExploded())
		{
			//	Changed to compressed from exploded
			File tmpFile = new File(packageFile.getParent(), "_tmp_" + pkg.getName());
			packageFile.renameTo(tmpFile, ArchiveDetector.DEFAULT);
			tmpFile.renameTo(packageFile, ArchiveDetector.DEFAULT);
		}
	}
	
	public void changeFileset (IPackageFileSet fileset)
	{
		updateNode(fileset);
		IPackageFileSet filesets[] = new IPackageFileSet[] { fileset };
		PackageFileSetImpl filesetImpl = (PackageFileSetImpl) fileset;
		
		DirectoryScanner oldScanner = (DirectoryScanner) scannerCache.get(fileset);
		
		if (oldScanner != null)
		{
			IPath oldPaths[] = filesetImpl.findMatchingPaths(oldScanner);
			for (int i = 0; i < oldPaths.length; i++)
			{
				if (!otherFilesetsMatch(oldPaths[i], fileset))
				{
					removePathFromFilesets(oldPaths[i], filesets);
				}
			}
		}
		
		IPath newPaths[] = filesetImpl.findMatchingPaths();
		for (int i = 0; i < newPaths.length; i++)
		{
			updatePathInFilesets(newPaths[i], filesets, false);
		}
		
		updateScannerCache(fileset);
	}
}
