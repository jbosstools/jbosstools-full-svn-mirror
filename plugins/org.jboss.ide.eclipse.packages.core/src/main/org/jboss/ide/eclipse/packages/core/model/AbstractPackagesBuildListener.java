package org.jboss.ide.eclipse.packages.core.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;

public class AbstractPackagesBuildListener implements IPackagesBuildListener {

	public void buildFailed(IPackage pkg, IStatus status) {
		// TODO Auto-generated method stub

	}

	public void finishedBuild(IProject project) {
		// TODO Auto-generated method stub

	}

	public void finishedBuildingPackage(IPackage pkg) {
		// TODO Auto-generated method stub

	}

	public void finishedCollectingFileSet(IPackageFileSet fileset) {
		// TODO Auto-generated method stub

	}

	public void startedBuild(IProject project) {
		// TODO Auto-generated method stub

	}

	public void startedBuildingPackage(IPackage pkg) {
		// TODO Auto-generated method stub

	}

	public void startedCollectingFileSet(IPackageFileSet fileset) {
		// TODO Auto-generated method stub

	}
	
	public void fileRemoved(IPackage topLevelPackage, IPackageFileSet fileset, IPath filePath) {
		// TODO Auto-generated method stub
		
	}
	
	public void fileUpdated(IPackage topLevelPackage, IPackageFileSet fileset, IPath filePath) {
		// TODO Auto-generated method stub
		
	}
	
	public void packageBuildTypeChanged(IPackage topLevelPackage, boolean exploded) {
		// TODO Auto-generated method stub
		
	}

}
