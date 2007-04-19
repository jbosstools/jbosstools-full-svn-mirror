package org.jboss.ide.eclipse.archives.core.build;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
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
		ArchiveModelNode root = ArchivesModel.instance().getRoot(project);
		IArchiveNode[] nodes = root.getChildren(IArchiveNode.TYPE_ARCHIVE);
		for( int i = 0; i < nodes.length; i++ ) {
			fullArchiveBuild(((IArchive)nodes[i]));
		}
	}
	
	public void fullArchiveBuild(IArchive pkg) {
		ModelTruezipBridge.deleteArchive(pkg);
		ModelTruezipBridge.createFile(pkg);
		IArchiveFileSet[] filesets = ModelUtil.findAllDescendentFilesets(pkg);
		for( int i = 0; i < filesets.length; i++ ) {
			fullFilesetBuild(filesets[i]);
		}
	}
	
	public void fullFilesetBuild(IArchiveFileSet fileset) {
		fileset.resetScanner();
		IPath[] paths = fileset.findMatchingPaths();
		ModelTruezipBridge.copyFiles(fileset, paths);
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
		while(i.hasNext()) {
			path = ((IResource)i.next()).getLocation();
			matchingFilesets = ModelUtil.getMatchingFilesets(path);
			ModelTruezipBridge.copyFiles(matchingFilesets, new IPath[] { path }, false);
		}
		
		i = removed.iterator();
		while(i.hasNext()) {
			path = ((IResource)i.next()).getLocation();
			matchingFilesets = ModelUtil.getMatchingFilesets(path);
			ModelTruezipBridge.deleteFiles(matchingFilesets, new IPath[] { path }, false);
		}
		TrueZipUtil.sync();
	}
}
