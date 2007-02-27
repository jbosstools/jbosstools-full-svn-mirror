package org.jboss.ide.eclipse.packages.core.project.build;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.IPackagesBuildListener;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelListener;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageFileSetImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.File;

public class BuildEvents implements IPackagesModelListener {

	private PackageBuildDelegate builder;
	private NullProgressMonitor nullMonitor = new NullProgressMonitor();
	private Hashtable scannerCache;
	
	public BuildEvents (PackageBuildDelegate builder)
	{
		this.builder = builder;
		this.scannerCache = new Hashtable();
		
		PackagesModel.instance().addPackagesModelListener(this);
	}
	
	private static interface ListenerVisitor
	{
		public void visitListener (IPackagesBuildListener listener);
	}
	
	protected void fireEvent (ListenerVisitor visitor)
	{
		for (Iterator iter = PackagesModel.instance().getBuildListeners().iterator(); iter.hasNext(); )
		{
			IPackagesBuildListener listener = (IPackagesBuildListener) iter.next();
			try {	
					visitor.visitListener(listener);
			} catch (RuntimeException e) {
				Trace.trace(getClass(), e);
				// Handle exceptions here so we can still broadcast events to the rest of our listeners
			}
		}
	}
	
	public void fireBuildFailed (final IPackage pkg, final IStatus status)
	{
		fireEvent(new ListenerVisitor () {
			public void visitListener(IPackagesBuildListener listener) {
				listener.buildFailed(pkg, status);
			}
		});
	}

	public void fireFileRemoved (final IPackage topLevelPackage, final IPackageFileSet fileset, final IPath filePath)
	{
		fireEvent(new ListenerVisitor () {
			public void visitListener(IPackagesBuildListener listener) {
				listener.fileRemoved(topLevelPackage, fileset, filePath);
			}
		});
	}

	public void fireFileUpdated (final IPackage topLevelPackage, final IPackageFileSet fileset, final IPath filePath)
	{
		fireEvent(new ListenerVisitor () {
			public void visitListener(IPackagesBuildListener listener) {
				listener.fileUpdated(topLevelPackage, fileset, filePath);
			}
		});
	}

	public void fireFinishedBuild (final IProject project)
	{
		fireEvent(new ListenerVisitor () {
			public void visitListener(IPackagesBuildListener listener) {
				listener.finishedBuild(project);
			}
		});
	}

	public void fireFinishedBuildingPackage (final IPackage pkg)
	{
		fireEvent(new ListenerVisitor () {
			public void visitListener(IPackagesBuildListener listener) {
				listener.finishedBuildingPackage(pkg);
			}
		});
	}

	public void fireFinishedCollectingFileSet (final IPackageFileSet fileset)
	{
		fireEvent(new ListenerVisitor () {
			public void visitListener(IPackagesBuildListener listener) {
				listener.finishedCollectingFileSet(fileset);
			}
		});
	}

	public void fireStartedBuild (final IProject project)
	{
		fireEvent(new ListenerVisitor () {
			public void visitListener(IPackagesBuildListener listener) {
				listener.startedBuild(project);
			}
		});
	}

	public void fireStartedBuildingPackage (final IPackage pkg)
	{
		fireEvent(new ListenerVisitor () {
			public void visitListener(IPackagesBuildListener listener) {
				listener.startedBuildingPackage(pkg);
			}
		});
	}

	public void fireStartedCollectingFileSet (final IPackageFileSet fileset)
	{
		fireEvent(new ListenerVisitor () {
			public void visitListener(IPackagesBuildListener listener) {
				listener.startedCollectingFileSet(fileset);
			}
		});
	}

	public void packageNodeAdded(IPackageNode added) {		
		builder.getFileOperations().updateNode(added);
		
		if (added.getNodeType() == IPackageNode.TYPE_PACKAGE)
		{
			updateScannerCache ((IPackage) added);
		}
		else if (added.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
		{
			updateScannerCache ((IPackageFileSet)added);
		}
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
	
	public void packageNodeAttached(IPackageNode attached) {
		packageNodeAdded(attached);
	}

	public void packageNodeChanged(IPackageNode changed) {
		
		if (changed.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
		{
			filesetChanged ((IPackageFileSet)changed);
		}
		else if (changed.getNodeType() == IPackageNode.TYPE_PACKAGE)
		{
			packageChanged ((IPackage)changed);
		}
	}

	private void filesetChanged (IPackageFileSet fileset)
	{
		builder.getFileOperations().updateNode(fileset);
		IPackageFileSet filesets[] = new IPackageFileSet[] { fileset };
		PackageFileSetImpl filesetImpl = (PackageFileSetImpl) fileset;
		
		DirectoryScanner oldScanner = (DirectoryScanner) scannerCache.get(fileset);
		
		if (oldScanner != null)
		{
			if (fileset.isInWorkspace())
			{
				IFile oldFiles[] = filesetImpl.findMatchingFiles(oldScanner);
				for (int i = 0; i < oldFiles.length; i++)
				{
					builder.getFileOperations().removeFileFromFilesets(oldFiles[i], filesets);
				}
			} else {
				IPath oldPaths[] = filesetImpl.findMatchingPaths(oldScanner);
				for (int i = 0; i < oldPaths.length; i++)
				{
					builder.getFileOperations().removePathFromFilesets(oldPaths[i], filesets);
				}
			}
		}
	}
	
	private void packageChanged (IPackage pkg)
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
			packageFile.renameTo(packageFile, ArchiveDetector.DEFAULT);
		}
		else if (packageFile.getDelegate().isDirectory() && !pkg.isExploded())
		{
			//	Changed to compressed from exploded
			packageFile.renameTo(packageFile, ArchiveDetector.NULL);
		}
	}
	
	public void packageNodeRemoved(IPackageNode removed) {	
		builder.getFileOperations().removeNode(removed);
	}

	public void projectRegistered(IProject project) {
		List packages = PackagesModel.instance().getProjectPackages(project);
		
		if (packages != null)
		{
			for (Iterator iter = packages.iterator(); iter.hasNext(); )
			{
				builder.buildSinglePackage((IPackage)iter.next(), nullMonitor);
			}
		}
	}

}
