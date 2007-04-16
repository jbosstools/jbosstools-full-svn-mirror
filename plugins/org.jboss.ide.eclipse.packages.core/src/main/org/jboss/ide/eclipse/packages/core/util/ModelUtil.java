package org.jboss.ide.eclipse.packages.core.util;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.runtime.IPath;
import org.jboss.ide.eclipse.packages.core.model.IPackageFileSet;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeVisitor;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;

public class ModelUtil {
	public static IPackageFileSet[] getMatchingFilesets(final IPath path) {
		final ArrayList rets = new ArrayList();
		PackagesModel.instance().accept(new IPackageNodeVisitor() {
			public boolean visit(IPackageNode node) {
				if( node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET && 
						((IPackageFileSet)node).matchesPath(path)) {
					rets.add((IPackageFileSet)node);
				}
				return true;
			} 
		});
		return (IPackageFileSet[]) rets.toArray(new IPackageFileSet[rets.size()]);
	}

	public static IPackageFileSet[] findAllDescendentFilesets(IPackageNode node) {
		if( node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET ) 
			return new IPackageFileSet[] {(IPackageFileSet)node};
		
		final ArrayList filesets = new ArrayList();
		node.accept(new IPackageNodeVisitor() {
			public boolean visit(IPackageNode node) {
				if( node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
					filesets.add(node);
				return true;
			} 
		});
		return (IPackageFileSet[]) filesets.toArray(new IPackageFileSet[filesets.size()]);
	}


	public static boolean otherFilesetMatchesPath(IPackageFileSet fileset, IPath path) {
		IPackageFileSet[] filesets = ModelUtil.getMatchingFilesets(path);
		if( filesets.length == 0 || (filesets.length == 1 && Arrays.asList(filesets).contains(fileset))) {
			return false;
		} else {
			// other filesets DO match... but are they at the same location in the archive?
			for( int i = 0; i < filesets.length; i++ ) {
				if( fileset.equals(filesets[i])) continue;
				if( fileset.getRootPackageRelativePath(path).equals(filesets[i].getRootPackageRelativePath(path))) {
					// the two put the file in the same spot! It's a match!
					return true;
				}
			}
		}
		return false;
	}

}
