package org.jboss.ide.eclipse.packages.core.project.build;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jboss.ide.eclipse.packages.core.Trace;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackagesBuildListener;
import org.jboss.ide.eclipse.packages.core.model.IPackagesModelListener;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;

import de.schlichtherle.io.ArchiveDetector;
import de.schlichtherle.io.File;

public class BuildEvents implements IPackagesModelListener {

	private PackageBuildDelegate builder;
	private NullProgressMonitor nullMonitor = new NullProgressMonitor();
	
	public BuildEvents (PackageBuildDelegate builder)
	{
		this.builder = builder;

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
	}

	public void packageNodeAttached(IPackageNode attached) {
		packageNodeAdded(attached);
	}

	public void packageNodeChanged(IPackageNode changed) {
		
		if (changed.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
		{
			builder.getFileOperations().updateNode(changed);
		}
		else if (changed.getNodeType() == IPackageNode.TYPE_PACKAGE)
		{
			IPackage pkg = (IPackage) changed;
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
