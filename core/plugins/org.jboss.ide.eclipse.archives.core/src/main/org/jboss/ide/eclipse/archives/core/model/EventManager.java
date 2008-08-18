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

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.DirectoryScannerFactory.DirectoryScannerExtension.FileWrapper;

/**
 * The event manager to fire events
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 */
public class EventManager {
	
	public static void cleanProjectBuild(IPath project) {
			IArchiveBuildListener[] listeners = getBuildListeners();
			for( int i = 0; i < listeners.length; i++ ) {
				try { 
					listeners[i].cleanProject(project);
				} catch(Exception e ) {logError(e);}
			}
	}
	
	public static void cleanArchiveBuild(IArchive archive) {
		IArchiveBuildListener[] listeners = getBuildListeners(archive);
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].cleanArchive(archive);
			} catch(Exception e ) {logError(e);}
		}
	}
	
	public static void startedBuild(IPath project) {
		IArchiveBuildListener[] listeners = getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].startedBuild(project);
			} catch(Exception e ) {logError(e);}
		}
	}

	public static void finishedBuild(IPath project) {
		IArchiveBuildListener[] listeners = getBuildListeners();
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].finishedBuild(project);
			} catch(Exception e ) {logError(e);}
		}
	}

	public static void startedBuildingArchive(IArchive archive) {
		IArchiveBuildListener[] listeners = getBuildListeners(archive);
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].startedBuildingArchive(archive);
			} catch(Exception e ) {logError(e);}
		}
	}

	public static void finishedBuildingArchive(IArchive archive) {
		IArchiveBuildListener[] listeners = getBuildListeners(archive);
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].finishedBuildingArchive(archive);
			} catch(Exception e ) {logError(e);}
		}
	}

	
	
	public static void startedCollectingFileSet(IArchiveFileSet fileset) {
		IArchiveBuildListener[] listeners = getBuildListeners(fileset);
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].startedCollectingFileSet(fileset);
			} catch(Exception e ) {logError(e);}
		}
	}
	public static void finishedCollectingFileSet(IArchiveFileSet fileset) {
		IArchiveBuildListener[] listeners = getBuildListeners(fileset);
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].finishedCollectingFileSet(fileset);
			} catch(Exception e ) {logError(e);}
		}
	}

	// Bulk events
	public static void filesUpdated(IArchive topLevelArchive, IArchiveFileSet fileset, FileWrapper[] filePath) {
		for( int i = 0; i < filePath.length; i++ ) {
			fileUpdated(topLevelArchive, fileset, new Path(filePath[i].getAbsolutePath()));
		}
	}
	
	// one file updated matching multiple filesets
	public static void fileUpdated(IPath path, IArchiveFileSet[] matchingFilesets) {
		for( int i = 0; i < matchingFilesets.length; i++ ) {
			fileUpdated(matchingFilesets[i].getRootArchive(), matchingFilesets[i], path);
		}
	}
	
	public static void fileUpdated(IArchive topLevelArchive, IArchiveFileSet fileset, IPath filePath) {
		IArchiveBuildListener[] listeners = getBuildListeners(topLevelArchive);
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].fileUpdated(topLevelArchive, fileset, filePath);
			} catch(Exception e ) {logError(e);}
		}
	}

	public static void fileRemoved(IArchive topLevelArchive, IArchiveFileSet fileset, IPath filePath) {
		IArchiveBuildListener[] listeners = getBuildListeners(topLevelArchive);
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].fileRemoved(topLevelArchive, fileset, filePath);
			} catch(Exception e ) {logError(e);}
		}
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
		IArchiveBuildListener[] listeners = getBuildListeners(pkg);
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].buildFailed(pkg, status);
			} catch(Exception e ) {logError(e);}
		}
	}


	
	/**
	 * Fire events dealing with model changes
	 * @param delta
	 */
	
	public static void fireDelta(IArchiveNodeDelta delta) {
		IArchiveNode node = delta.getPostNode() == null ? delta.getPreNode() : delta.getPostNode();
		IArchiveModelListener[] listeners = getModelListeners(node);
		for( int i = 0; i < listeners.length; i++ ) {
			try {
				listeners[i].modelChanged(delta);
			} catch(Exception e ) {logError(e);}		}
	}

	
	private static IArchiveModelListener[] getModelListeners(IArchiveNode node) {
		IArchiveModelRootNode model = node.getModelRootNode();
		if( model != null && model.getModel() != null ) {
			return model.getModel().getModelListeners();
		}
		return new IArchiveModelListener[]{};
	}
	
	// get workspace default ones
	private static IArchiveBuildListener[] getBuildListeners() {
		return ArchivesModel.instance().getBuildListeners();
	}
	private static IArchiveBuildListener[] getBuildListeners(IArchiveNode node) {
		IArchiveModelRootNode model = node.getModelRootNode();
		if( model != null && model.getModel() != null ) {
			return model.getModel().getBuildListeners();
		}
		return new IArchiveBuildListener[]{};
	}

	protected static void logError(Exception e) {
		try {
			StackTraceElement[] trace = Thread.currentThread().getStackTrace();
			ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_WARN, "Archives Listener error in " + trace[1].getMethodName(), e);
		} catch( Exception f ) {
			ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_WARN, "Archives Listener error", e);
		}
	}
}
