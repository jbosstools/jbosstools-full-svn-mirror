/**
 * JBoss, a Division of Red Hat
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
package org.jboss.ide.eclipse.archives.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;

/**
 * Utility class for matching model elements and stuff
 * @author <a href="rob.stryker@redhat.com">Rob Stryker</a>
 *
 */
public class ModelUtil {
	
	/**
	 * Get any declared fileset that matches this path and are registered with the model
	 * @param path
	 * @return
	 */
	public static IArchiveFileSet[] getMatchingFilesets(final IPath path) {
		return getMatchingFilesets(null, path);
	}
	
	/**
	 * Get any declared filesets that match this path and are
	 * a child of the given node
	 * @param node
	 * @param path
	 * @return
	 */
	public static IArchiveFileSet[] getMatchingFilesets(IArchiveNode node, final IPath path) {
		final ArrayList rets = new ArrayList();
		IArchiveNodeVisitor visitor = new IArchiveNodeVisitor() {
			public boolean visit(IArchiveNode node) {
				if( node.getNodeType() == IArchiveNode.TYPE_ARCHIVE_FILESET && 
						((IArchiveFileSet)node).matchesPath(path)) {
					rets.add((IArchiveFileSet)node);
				}
				return true;
			}
		};

		if( node == null )
			ArchivesModel.instance().accept(visitor);
		else
			node.accept(visitor);
		
		return (IArchiveFileSet[]) rets.toArray(new IArchiveFileSet[rets.size()]);
	}

	/**
	 * Find all filesets that are a child to this node
	 * @param node
	 * @return
	 */
	public static IArchiveFileSet[] findAllDescendentFilesets(IArchiveNode node) {
		ArrayList matches = findAllDescendents(node, IArchiveNode.TYPE_ARCHIVE_FILESET, true);
		return (IArchiveFileSet[]) matches.toArray(new IArchiveFileSet[matches.size()]);
	}

	/**
	 * Find all folders that are a child to this node
	 * @param node
	 * @return
	 */
	public static IArchiveFolder[] findAllDescendentFolders(IArchiveNode node) {
		ArrayList matches = findAllDescendents(node, IArchiveNode.TYPE_ARCHIVE_FOLDER, false);
		return (IArchiveFolder[]) matches.toArray(new IArchiveFolder[matches.size()]);
	}
	
	/**
	 * Find all nodes of one type that are a child to this one
	 * @param node
	 * @return
	 */
	public static ArrayList findAllDescendents(IArchiveNode node, final int type, final boolean includeSelf) {
		final ArrayList matches = new ArrayList();
		final IArchiveNode original = node;
		node.accept(new IArchiveNodeVisitor() {
			public boolean visit(IArchiveNode node) {
				if( ((node.getNodeType() == type) && !matches.contains(node)) && (includeSelf || node != original))
					matches.add(node);
				return true;
			} 
		});
		return matches;
	}

	
	public static boolean otherFilesetMatchesPathAndOutputLocation(IArchiveFileSet fileset, IPath path) {
		return otherFilesetMatchesPathAndOutputLocation(fileset, path, null);
	}


	/**
	 * Do any filesets other than the parameter match this path?
	 * @param fileset
	 * @param path
	 * @param root a node to start with, or null if the entire model
	 * @return
	 */
	public static boolean otherFilesetMatchesPathAndOutputLocation(IArchiveFileSet fileset, IPath path, IArchiveNode root) {
		IArchiveFileSet[] filesets = ModelUtil.getMatchingFilesets(root, path);
		if( filesets.length == 0 || (filesets.length == 1 && Arrays.asList(filesets).contains(fileset))) {
			return false;
		} else {
			// other filesets DO match... but are they at the same location in the archive?
			boolean relativePathsMatch;
			boolean destinationsMatch;
			for( int i = 0; i < filesets.length; i++ ) {
				if( fileset.equals(filesets[i])) continue;
				relativePathsMatch = fileset.getRootArchiveRelativePath(path).equals(filesets[i].getRootArchiveRelativePath(path));
				destinationsMatch = fileset.getRootArchive().getArchiveFilePath().equals(filesets[i].getRootArchive().getArchiveFilePath());
				if( relativePathsMatch && destinationsMatch ) {
					// the two put the file in the same spot, within the same archive! It's a match!
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Get the raw file for this node, specifically, 
	 * the file actually saved as an OS file.
	 * @param node
	 * @return
	 */
	public static IPath getBaseDestinationFile(IArchiveNode node) {
		return getBaseDestinationFile(node, null);
	}

	/**
	 * Get the raw file for this node, specifically, 
	 * the file actually saved as an OS file.
	 * @param node
	 * @return
	 */
	public static IPath getBaseDestinationFile(IArchiveNode node, IPath absolutePath) {
		IArchiveNode parameterNode = node;
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
		
		if( absolutePath != null && parameterNode.getNodeType() ==  IArchiveNode.TYPE_ARCHIVE_FILESET ) {
			 IArchiveFileSet fs = ((IArchiveFileSet)parameterNode);
			 IPath sourcePath = fs.getGlobalSourcePath();
			 if( sourcePath.isPrefixOf(absolutePath)) {
				 lastConcrete = lastConcrete.append(absolutePath.removeFirstSegments(sourcePath.segmentCount()));
			 }
		} 
		return lastConcrete; 
	}
	
	
	public static IPath workspacePathToAbsolutePath (IPath workspacePath) {
		String projectName = workspacePath.segment(0);
		IPath projectPath = ArchivesCore.getInstance().getVariables().getProjectPath(projectName);
		
		return projectPath.append(workspacePath.removeFirstSegments(1));
	}
}
