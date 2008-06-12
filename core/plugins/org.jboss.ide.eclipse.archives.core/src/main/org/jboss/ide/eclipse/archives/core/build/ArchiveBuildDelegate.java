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
package org.jboss.ide.eclipse.archives.core.build;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.EventManager;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveAction;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelRootNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.util.ModelUtil;
import org.jboss.ide.eclipse.archives.core.util.internal.ModelTruezipBridge;
import org.jboss.ide.eclipse.archives.core.util.internal.TrueZipUtil;

/**
 * This delegate will either build from the model completely 
 * (if the builder has been given a full build request) or
 * incrementally update the changed files in 
 * **ANY AND ALL** filesets that they match, regardless of project. 
 * 
 * @author Rob Stryker (rob.stryker@redhat.com)
 *
 */
public class ArchiveBuildDelegate {
	
	public ArchiveBuildDelegate() {
	}
	
	
	/**
	 * A full project build has been requested. 
	 * @param project The project containing the archive model
	 */
	public void fullProjectBuild(IPath project) {
		EventManager.cleanProjectBuild(project);
		EventManager.startedBuild(project);

		IArchiveModelRootNode root = ArchivesModel.instance().getRoot(project);
		if( root == null ) return;
		IArchiveNode[] nodes = root.getChildren(IArchiveNode.TYPE_ARCHIVE);
		for( int i = 0; i < nodes.length; i++ ) {
			fullArchiveBuild(((IArchive)nodes[i]));
		}

		EventManager.finishedBuild(project);
	}
	
	/**
	 * Builds an archive entirely, overwriting whatever was in the output destination. 
	 * @param pkg The archive to build
	 */
	public void fullArchiveBuild(IArchive pkg) {
		if( !pkg.canBuild() ) {
			ArchivesCore.getInstance().getLogger().log(IStatus.WARNING, 
					"Cannot Build archive \"" + pkg.getName() + 
					"\" due to a problem in the archive's configuration.", null);
			return;
		}
		
		EventManager.cleanArchiveBuild(pkg);
		EventManager.startedBuildingArchive(pkg);
		
		ModelTruezipBridge.deleteArchive(pkg);
		if( !pkg.getGlobalDestinationPath().toFile().exists() ) {
			if( !pkg.getGlobalDestinationPath().toFile().mkdirs() ) {
				ArchivesCore.getInstance().getLogger().log(IStatus.WARNING, 
						"Cannot Build archive \"" + pkg.getName() + 
						"\". Output location " + pkg.getGlobalDestinationPath() + 
						" is not writeable", null);
				return;
			}
		}
		
		// Run the pre actions
		IArchiveAction[] actions = pkg.getActions();
		for( int i = 0; i < actions.length; i++ ) {
			if( actions[i].getTime().equals(IArchiveAction.PRE_BUILD)) {
				actions[i].execute();
			}
		}
		
		ModelTruezipBridge.createFile(pkg);
		
		// force create all folders
		IArchiveFolder[] folders = ModelUtil.findAllDescendentFolders(pkg);
		for( int i = 0; i < folders.length; i++ ) {
			ModelTruezipBridge.createFile(folders[i]);
		}
		
		// build the filesets
		IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(pkg);
		for( int i = 0; i < filesets.length; i++ ) {
			fullFilesetBuild(filesets[i], pkg);
		}
		
		// Run the post actions
		for( int i = 0; i < actions.length; i++ ) {
			if( actions[i].getTime().equals(IArchiveAction.POST_BUILD)) {
				actions[i].execute();
			}
		}
		
		EventManager.finishedBuildingArchive(pkg);
	}
	
	/**
	 * Build the given fileset
	 * @param fileset The fileset to match
	 * @param topLevel The top level archive that the fileset belongs to
	 */
	protected void fullFilesetBuild(IArchiveFileSet fileset, IArchive topLevel) {
		EventManager.startedCollectingFileSet(fileset);
		
		// reset the scanner. It *is* a full build afterall
		fileset.resetScanner();
		IPath[] paths = fileset.findMatchingPaths();
		ModelTruezipBridge.fullFilesetBuild(fileset);

		EventManager.filesUpdated(topLevel, fileset, paths);
		EventManager.finishedCollectingFileSet(fileset);
	}

	
	
	
	/**
	 * Incremental Build!! 
	 * Parameters are instances of changed IPath objects.
	 * Will search the entire model for matching filesets.
	 * 
	 * @param addedChanged  Set of changed / added resources
	 * @param setRemoved	Set of removed resources
	 */
	public void projectIncrementalBuild(Set<IPath> addedChanged, Set<IPath> removed) {
		incrementalBuild(null, addedChanged, removed);
	}
	
	/**
	 * Incremental build.
	 * Parameters are instance sof changed IPath objects
	 * Will search only the given node for matching descendent filesets
	 * @param archive   An archive to limit the scope to, or null if the entire default model 
	 * @param addedChanged  A list of added or changed resource paths
	 * @param removed       A list of removed resource paths
	 */
	public void incrementalBuild(IArchive archive, Set<IPath> addedChanged, Set<IPath> removed) {
		
		// find any and all filesets that match each file
		Iterator<IPath> i = addedChanged.iterator();
		IPath path;
		IArchiveFileSet[] matchingFilesets;
		ArrayList<IArchive> topPackagesChanged = new ArrayList<IArchive>();
		while(i.hasNext()) {
			path = i.next();
			matchingFilesets = ModelUtil.getMatchingFilesets(archive, path);
			localFireAffectedTopLevelPackages(topPackagesChanged, matchingFilesets);
			ModelTruezipBridge.copyFiles(matchingFilesets, new IPath[] { path }, false);
			EventManager.fileUpdated(path, matchingFilesets);
		}
		
		i = removed.iterator();
		while(i.hasNext()) {
			path = ((IPath)i.next());
			matchingFilesets = ModelUtil.getMatchingFilesets(archive, path);
			localFireAffectedTopLevelPackages(topPackagesChanged, matchingFilesets);
			ModelTruezipBridge.deleteFiles(matchingFilesets, new IPath[] { path }, false);
			EventManager.fileRemoved(path, matchingFilesets);
		}
		
		TrueZipUtil.sync();

		Iterator<IArchive> i2 = topPackagesChanged.iterator();
		while(i2.hasNext()) {
			EventManager.finishedBuildingArchive(i2.next());
		}		
	}
	
	private void localFireAffectedTopLevelPackages(ArrayList<IArchive> affected, IArchiveFileSet[] filesets) {
		for( int i = 0; i < filesets.length; i++ ) {
			if( !affected.contains(filesets[i].getRootArchive())) {
				affected.add(filesets[i].getRootArchive());
				EventManager.startedBuildingArchive(filesets[i].getRootArchive());
			}
		}
	}
}
