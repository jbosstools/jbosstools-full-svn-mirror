package org.jboss.ide.eclipse.archives.core.build;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.events.EventManager;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.util.ModelTruezipBridge;
import org.jboss.ide.eclipse.archives.core.util.ModelUtil;
import org.jboss.ide.eclipse.archives.core.util.TrueZipUtil;

public class ArchiveBuildDelegate {
	
	// TODO:  Create lock mechanism which will interlock with model changes
	public ArchiveBuildDelegate() {
		
	}
	
	// full build
	public void fullProjectBuild(IProject project) {
		EventManager.cleanProjectBuild(project);
		EventManager.startedBuild(project);

		ArchiveModelNode root = ArchivesModel.instance().getRoot(project);
		IArchiveNode[] nodes = root.getChildren(IArchiveNode.TYPE_ARCHIVE);
		for( int i = 0; i < nodes.length; i++ ) {
			fullArchiveBuild(((IArchive)nodes[i]));
		}

		EventManager.finishedBuild(project);
	}
	
	public void fullArchiveBuild(IArchive pkg) {
		EventManager.cleanArchiveBuild(pkg);
		EventManager.startedBuildingArchive(pkg);
		
		ModelTruezipBridge.deleteArchive(pkg);
		ModelTruezipBridge.createFile(pkg);
		IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(pkg);
		for( int i = 0; i < filesets.length; i++ ) {
			fullFilesetBuild(filesets[i], pkg);
		}
		
		EventManager.finishedBuildingArchive(pkg);
	}
	
	public void fullFilesetBuild(IArchiveFileSet fileset, IArchive topLevel) {
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
	 * Parameters are instances of changed IResource objects
	 * @param addedChanged  Set of changed / added resources
	 * @param setRemoved	Set of removed resources
	 */
	public void projectIncrementalBuild(Set addedChanged, Set removed) {
		
		// find any and all filesets that match each file
		Iterator i = addedChanged.iterator();
		IPath path;
		IArchiveFileSet[] matchingFilesets;
		ArrayList topPackagesChanged = new ArrayList();
		while(i.hasNext()) {
			path = ((IResource)i.next()).getLocation();
			matchingFilesets = ModelUtil.getMatchingFilesets(path);
			localFireAffectedTopLevelPackages(topPackagesChanged, matchingFilesets);
			ModelTruezipBridge.copyFiles(matchingFilesets, new IPath[] { path }, false);
			EventManager.fileUpdated(path, matchingFilesets);
		}
		
		i = removed.iterator();
		while(i.hasNext()) {
			path = ((IResource)i.next()).getLocation();
			matchingFilesets = ModelUtil.getMatchingFilesets(path);
			localFireAffectedTopLevelPackages(topPackagesChanged, matchingFilesets);
			ModelTruezipBridge.deleteFiles(matchingFilesets, new IPath[] { path }, false);
			EventManager.fileRemoved(path, matchingFilesets);
		}
		
		TrueZipUtil.sync();

		i = topPackagesChanged.iterator();
		while(i.hasNext()) {
			EventManager.finishedBuildingArchive((IArchive)i.next());
		}
	}
	
	private void localFireAffectedTopLevelPackages(ArrayList affected, IArchiveFileSet[] filesets) {
		for( int i = 0; i < filesets.length; i++ ) {
			if( !affected.contains(filesets[i].getRootArchive())) {
				affected.add(filesets[i].getRootArchive());
				EventManager.startedBuildingArchive(filesets[i].getRootArchive());
			}
		}
	}
}
