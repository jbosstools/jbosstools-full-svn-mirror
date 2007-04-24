package org.jboss.ide.eclipse.archives.core.model.events;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveBuildListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelListener;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeDelta;
import org.jboss.ide.eclipse.archives.core.model.ArchivesCore;

public class EventManager {
	
	public static void cleanProjectBuild(IProject project) {
		try { 
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].cleanProject(project);
		} catch(Exception e ) {}
	}
	
	public static void cleanArchiveBuild(IArchive archive) {
		try {
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].cleanArchive(archive);
		} catch(Exception e ) {}
	}
	
	public static void startedBuild(IProject project) {
		try {
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].startedBuild(project);
		} catch(Exception e ) {}
	}

	public static void finishedBuild(IProject project) {
		try {
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].finishedBuild(project);
		} catch(Exception e ) {}
	}

	public static void startedBuildingArchive(IArchive archive) {
		try {
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].startedBuildingArchive(archive);
		} catch(Exception e ) {}
	}

	public static void finishedBuildingArchive(IArchive archive) {
		try {
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].finishedBuildingArchive(archive);
		} catch(Exception e ) {}
	}

	
	
	public static void startedCollectingFileSet(IArchiveFileSet fileset) {
		try {
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].startedCollectingFileSet(fileset);
		} catch(Exception e ) {}
	}
	public static void finishedCollectingFileSet(IArchiveFileSet fileset) {
		try {
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].finishedCollectingFileSet(fileset);
		} catch(Exception e ) {}
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
		try {
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].fileUpdated(topLevelArchive, fileset, filePath);
		} catch(Exception e ) {}
	}

	public static void fileRemoved(IArchive topLevelArchive, IArchiveFileSet fileset, IPath filePath) {
		try {
			IArchiveBuildListener[] listeners = ArchivesCore.getInstance().getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].fileRemoved(topLevelArchive, fileset, filePath);
		} catch(Exception e ) {}
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

	public static void buildFailed(IArchive pkg, IStatus status) {
	}

	/**
	 * Model changes
	 * @param delta
	 */
	
	public static void fireDelta(IArchiveNodeDelta delta) {
		try {
			IArchiveModelListener[] listeners = ArchivesCore.getInstance().getModelListeners();
			for( int i = 0; i < listeners.length; i++ ) 
				listeners[i].modelChanged(delta);
		} catch(Exception e ) {}
	}


}
