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
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.archives.core.ArchivesCore;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModelException;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFileSet;
import org.jboss.ide.eclipse.archives.core.model.IArchiveFolder;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModel;
import org.jboss.ide.eclipse.archives.core.model.IArchiveModelRootNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNodeVisitor;
import org.jboss.ide.eclipse.archives.core.model.IArchivesLogger;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveActionImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveFileSetImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveFolderImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveModelNode;
import org.jboss.ide.eclipse.archives.core.model.internal.ArchiveNodeImpl;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbAction;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFileSet;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbFolder;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackage;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackageNode;
import org.jboss.ide.eclipse.archives.core.model.internal.xb.XbPackages;

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
		final ArrayList<IArchiveFileSet> rets = new ArrayList<IArchiveFileSet>();
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
		
		return rets.toArray(new IArchiveFileSet[rets.size()]);
	}

	/**
	 * Find all filesets that are a child to this node
	 * @param node
	 * @return
	 */
	public static IArchiveFileSet[] findAllDescendentFilesets(IArchiveNode node) {
		ArrayList<IArchiveNode> matches = findAllDescendents(node, IArchiveNode.TYPE_ARCHIVE_FILESET, true);
		return matches.toArray(new IArchiveFileSet[matches.size()]);
	}

	/**
	 * Find all folders that are a child to this node
	 * @param node
	 * @return
	 */
	public static IArchiveFolder[] findAllDescendentFolders(IArchiveNode node) {
		ArrayList<IArchiveNode> matches = findAllDescendents(node, IArchiveNode.TYPE_ARCHIVE_FOLDER, false);
		return matches.toArray(new IArchiveFolder[matches.size()]);
	}
	
	/**
	 * Find all nodes of one type that are a child to this one
	 * @param node
	 * @return
	 */
	public static ArrayList<IArchiveNode> findAllDescendents(IArchiveNode node, final int type, final boolean includeSelf) {
		final ArrayList<IArchiveNode> matches = new ArrayList<IArchiveNode>();
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
		ArrayList<IArchiveNode> list = new ArrayList<IArchiveNode>();
		while( node != null && !(node instanceof ArchiveModelNode)) {
			list.add(node);
			node = node.getParent();
		}
		Collections.reverse(list);
		IArchiveNode[] nodes = list.toArray(new IArchiveNode[list.size()]);
		
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
		if( projectName == null ) return null;
		IPath projectPath = ArchivesCore.getInstance().getVariables().getProjectPath(projectName);
		if( projectPath == null ) return null;
		return projectPath.append(workspacePath.removeFirstSegments(1));
	}
	
	public static void fillArchiveModel( XbPackages node, IArchiveModelRootNode modelNode) throws ArchivesModelException { 
		for (Iterator iter = node.getAllChildren().iterator(); iter.hasNext(); ) {
			XbPackageNode child = (XbPackageNode) iter.next();
			ArchiveNodeImpl childImpl = (ArchiveNodeImpl)createPackageNodeImpl(child, modelNode);
			if (modelNode != null && childImpl != null) {
				try {
					if( modelNode instanceof ArchiveNodeImpl )
						((ArchiveNodeImpl)modelNode).addChild(childImpl, false);
					else 
						modelNode.addChild(childImpl);
				} catch( ArchivesModelException ame ) {
					ArchivesCore.getInstance().getLogger().log(IArchivesLogger.MSG_WARN, "Error Adding Child", ame);
				}
			}
		}
	}

	protected static IArchiveNode createPackageNodeImpl (XbPackageNode node, IArchiveNode parent) throws ArchivesModelException {
		ArchiveNodeImpl nodeImpl = null;
		if (node instanceof XbPackage) {
			nodeImpl = new ArchiveImpl((XbPackage)node);
		} else if (node instanceof XbFolder) {
			nodeImpl = new ArchiveFolderImpl((XbFolder)node);
		} else if (node instanceof XbFileSet) {
			nodeImpl = new ArchiveFileSetImpl((XbFileSet)node);
		} else if( node instanceof XbAction ) {
			nodeImpl = new ArchiveActionImpl((XbAction)node);
		}
		
		for (Iterator iter = node.getAllChildren().iterator(); iter.hasNext(); ) {
			XbPackageNode child = (XbPackageNode) iter.next();
			ArchiveNodeImpl childImpl = (ArchiveNodeImpl)createPackageNodeImpl(child, nodeImpl);
			if (nodeImpl != null && childImpl != null) {
				nodeImpl.addChild(childImpl, false);
			}
		}
		return nodeImpl;
	}
	
	public static IArchive[] getProjectArchives(IPath project) {
		return getProjectArchives(project, ArchivesModel.instance());
	}
	
	public static IArchive[] getProjectArchives(IPath project, IArchiveModel model) {
		if( model != null ) {
			IArchiveModelRootNode root = model.getRoot(project);
			if( root == null ) return new IArchive[0];
			IArchiveNode[] archives = model.getRoot(project).getAllChildren();
			List<IArchiveNode> list = Arrays.asList(archives);
			return (IArchive[]) list.toArray(new IArchive[list.size()]);
		}
		return null;
	}
}
