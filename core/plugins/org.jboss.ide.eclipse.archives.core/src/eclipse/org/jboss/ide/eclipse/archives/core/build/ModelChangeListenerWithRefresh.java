package org.jboss.ide.eclipse.archives.core.build;


import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.jboss.ide.eclipse.archives.core.model.ArchivesModel;
import org.jboss.ide.eclipse.archives.core.model.IArchive;
import org.jboss.ide.eclipse.archives.core.model.IArchiveNode;

/**
 * This class responds to model change events. 
 * It is given a delta as to what nodes are added, removed, or changed. 
 * It then keeps the output file for the top level archive in sync with
 * the changes to the model.
 * 
 * If the automatic builder is not enabled for this project, the listener
 * does nothing. 
 * 
 * @author Rob Stryker (rob.stryker@redhat.com)
 *
 */
public class ModelChangeListenerWithRefresh extends ModelChangeListener {

	protected void postChange(IArchiveNode node) {
		IArchive pack = node.getRootArchive();
		if( pack != null ) {
			if( pack.isDestinationInWorkspace() ) {
				// refresh the root package node
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IResource res = root.getContainerForLocation(pack.getDestinationPath());
				if( res != null ) {
					try {
						res.getParent().refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
					} catch( CoreException ce ) {
					}
				}
			}
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IContainer proj = root.getContainerForLocation(pack.getProjectPath());
			try {
				proj.getFile(new Path(ArchivesModel.PROJECT_PACKAGES_FILE)).refreshLocal(IResource.DEPTH_ONE, new NullProgressMonitor());
			} catch( CoreException ce ) {
			}
			
		}
	}
}
