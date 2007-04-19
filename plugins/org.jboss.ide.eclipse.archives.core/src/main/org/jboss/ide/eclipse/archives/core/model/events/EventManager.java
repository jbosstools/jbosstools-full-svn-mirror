package org.jboss.ide.eclipse.archives.core.model.events;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveBuildListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.PackagesCore;

public class EventManager {
	public static void startedBuild(IProject project) {
		IArchiveBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].startedBuild(project);
	}

	public static void finishedBuild(IProject project) {
		IArchiveBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].finishedBuild(project);
	}

	public static void startedBuildingArchive(IArchive archive) {
		IArchiveBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].startedBuildingArchive(archive);
	}

	public static void finishedBuildingArchive(IArchive archive) {
		IArchiveBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].finishedBuildingArchive(archive);
	}

	
	
	public static void startedCollectingFileSet(IArchiveFileSet fileset) {
		IArchiveBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].startedCollectingFileSet(fileset);
	}
	public static void finishedCollectingFileSet(IArchiveFileSet fileset) {
		IArchiveBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].finishedCollectingFileSet(fileset);
	}

	// Bulk events
	public static void filesUpdated(IArchive topLevelArchive, IArchiveFileSet fileset, IPath[] filePath) {
		for( int i = 0; i < filePath.length; i++ ) {
			fileUpdated(topLevelArchive, fileset, filePath[i]);
		}
	}
	
	// one file updated matching multiple filesets
	public static void fileUpdated(IPath path, IArchiveFileSet[] matchingFilesets) {
		for( int i = 0; i < matchingFilesets.length; i++ ) {
			fileUpdated(matchingFilesets[i].getRootArchive(), matchingFilesets[i], path);
		}
	}
	
	public static void fileUpdated(IArchive topLevelArchive, IArchiveFileSet fileset, IPath filePath) {
		IArchiveBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].fileUpdated(topLevelArchive, fileset, filePath);
	}

	public static void fileRemoved(IArchive topLevelArchive, IArchiveFileSet fileset, IPath filePath) {
		IArchiveBuildListener[] listeners = PackagesCore.getInstance().getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].fileRemoved(topLevelArchive, fileset, filePath);
	}

	// one file removed matching multiple filesets
	public static void fileRemoved(IPath path, IArchiveFileSet[] matchingFilesets) {
		for( int i = 0; i < matchingFilesets.length; i++ ) {
			fileRemoved(matchingFilesets[i].getRootArchive(), matchingFilesets[i], path);
		}
	}
	
	public static void filesRemoved(IPath[] paths, IArchiveFileSet fileset) {
		for( int i = 0; i < paths.length; i++ ) {
			fileRemoved(fileset.getRootArchive(), fileset, paths[i]);
		}
	}


	public static void ArchiveBuildTypeChanged(IArchive topLevelArchive, boolean isExploded) {
	}

	public static void buildFailed(IArchive pkg, IStatus status) {
	}






	
	/**
	 * Model changes
	 * @param delta
	 */
	
	public static void fireDelta(IArchiveNodeDelta delta) {
		IArchiveModelListener[] listeners = PackagesCore.getInstance().getModelListeners();
		for( int i = 0; i < listeners.length; i++ ) 
			listeners[i].modelChanged(delta);
	}


}
