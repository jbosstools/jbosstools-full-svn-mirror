package org.jboss.ide.eclipse.archives.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchivesModel;

public class ModelUtil {
	public static IArchiveFileSet[] getMatchingFilesets(final IPath path) {
		final ArrayList rets = new ArrayList();
		ArchivesModel.instance().accept(new IArchiveNodeVisitor() {
			public boolean visit(IArchiveNode node) {
				if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET && 
						((IArchiveFileSet)node).matchesPath(path)) {
					rets.add((IArchiveFileSet)node);
				}
				return true;
			} 
		});
		return (IArchiveFileSet[]) rets.toArray(new IArchiveFileSet[rets.size()]);
	}

	public static IArchiveFileSet[] findAllDescendentFilesets(IArchiveNode node) {
		if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET ) 
			return new IArchiveFileSet[] {(IArchiveFileSet)node};
		
		final ArrayList filesets = new ArrayList();
		node.accept(new IArchiveNodeVisitor() {
			public boolean visit(IArchiveNode node) {
				if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET)
					filesets.add(node);
				return true;
			} 
		});
		return (IArchiveFileSet[]) filesets.toArray(new IArchiveFileSet[filesets.size()]);
	}


	public static boolean otherFilesetMatchesPath(IArchiveFileSet fileset, IPath path) {
		IArchiveFileSet[] filesets = ModelUtil.getMatchingFilesets(path);
		if( filesets.length == 0 || (filesets.length == 1 && Arrays.asList(filesets).contains(fileset))) {
			return false;
		} else {
			// other filesets DO match... but are they at the same location in the archive?
			for( int i = 0; i < filesets.length; i++ ) {
				if( fileset.equals(filesets[i])) continue;
				if( fileset.getRootArchiveRelativePath(path).equals(filesets[i].getRootArchiveRelativePath(path))) {
					// the two put the file in the same spot! It's a match!
					return true;
				}
			}
		}
		return false;
	}

	public static IResource getResource(IArchiveNode node) {
		IPath dest = null;
		if( node.getRootArchive().isDestinationInWorkspace() ) {
			// our root is somewhere in workspace... good sign
			if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE)
				dest = ((IArchive)node).getDestinationPath();
			else if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FOLDER) {
				dest = node.getRootArchive().getDestinationPath();
				dest = dest.append(((IArchiveFolder)node).getRootArchiveRelativePath());
			}
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource res = root.getContainerForLocation(dest);
			return res;
		}
		return null;
	}
	
	public static IPath getBaseFile(IArchiveNode node) {
		return getBaseFile(node, null);
	}
	public static IPath getBaseFile(IArchiveNode node, IPath absolutePath) {
		ArrayList list = new ArrayList();
		while( node != null && !(node instanceof ArchiveModelNode)) {
			list.add(node);
			node = node.getParent();
		}
		Collections.reverse(list);
		IArchiveNode[] nodes = (IArchiveNode[]) list.toArray(new IArchiveNode[list.size()]);
		
		IPath lastConcrete = null;
		for( int i = 0; i < nodes.length; i++ ) {
			if( nodes[i] instanceof IArchive) {
				if( lastConcrete == null ) 
					lastConcrete = ((IArchive)nodes[i]).getArchiveFilePath();
				else
					lastConcrete = lastConcrete.append(((IArchive)nodes[i]).getName());
				
				if( !((IArchive)nodes[i]).isExploded())
					return lastConcrete;
			}  else if( nodes[i] instanceof IArchiveFolder ) {
				lastConcrete = lastConcrete.append(((IArchiveFolder)nodes[i]).getName());
			}
		}
		
		if( absolutePath != null && node.getNodeType() ==  IArchiveNode.TYPE_ARCHIVE_FILESET ) {
			 IArchiveFileSet fs = ((IArchiveFileSet)node);
			 if( fs.getSourcePath().isPrefixOf(absolutePath)) {
				 lastConcrete.append(absolutePath.removeFirstSegments(fs.getSourcePath().segmentCount()));
			 }
		} 
		return lastConcrete; 
	}
	
}
