package org.jboss.ide.eclipse.packages.core.build;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageModelNode;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;
import org.jboss.ide.eclipse.packages.core.util.ModelTruezipBridge;
import org.jboss.ide.eclipse.packages.core.util.ModelUtil;

public class PackageBuildDelegate {
	
	// TODO:  Create lock mechanism which will interlock with model changes
	public PackageBuildDelegate() {
		
	}
	
	
	// full build
	public void fullProjectBuild(IProject project) {
		PackageModelNode root = PackagesModel.instance().getRoot(project);
		IPackageNode[] nodes = root.getChildren(IPackageNode.TYPE_PACKAGE);
		for( int i = 0; i < nodes.length; i++ ) {
			fullPackageBuild(((IPackage)nodes[i]));
		}
	}
	
	public void fullPackageBuild(IPackage pkg) {
		ModelTruezipBridge.deletePackage(pkg);
		IPackageFileSet[] filesets = ModelUtil.findAllDescendentFilesets(pkg);
		for( int i = 0; i < filesets.length; i++ ) {
			fullFilesetBuild(filesets[i]);
		}
	}
	
	public void fullFilesetBuild(IPackageFileSet fileset) {
		fileset.resetScanner();
		IPath[] paths = fileset.findMatchingPaths();
		ModelTruezipBridge.copyFiles(fileset, paths);
	}

	
	
	
	/**
	 * Incremental Build!! 
	 * @param addedChanged  Set of changed / added resources
	 * @param setRemoved	Set of removed resources
	 */
	public void projectIncrementalBuild(Set addedChanged, Set removed) {
		
		
		// find any and all filesets that match each file
		Iterator i = addedChanged.iterator();
		IPath path;
		IPackageFileSet[] matchingFilesets;
		while(i.hasNext()) {
			path = ((IResource)i.next()).getLocation();
			matchingFilesets = ModelUtil.getMatchingFilesets(path);
			ModelTruezipBridge.copyFiles(matchingFilesets, new IPath[] { path });
		}
		
		i = removed.iterator();
		while(i.hasNext()) {
			path = ((IResource)i.next()).getLocation();
			matchingFilesets = ModelUtil.getMatchingFilesets(path);
			ModelTruezipBridge.deleteFiles(matchingFilesets, new IPath[] { path });
		}
	}
}
