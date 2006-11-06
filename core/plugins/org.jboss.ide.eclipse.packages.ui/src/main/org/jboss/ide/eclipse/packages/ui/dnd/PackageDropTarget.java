package org.jboss.ide.eclipse.packages.ui.dnd;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ResourceTransfer;
import org.jboss.ide.eclipse.packages.core.model.IPackageNode;
import org.jboss.ide.eclipse.packages.core.model.IPackageNodeWorkingCopy;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageFileSetImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageNodeImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.xb.XbFileSet;

public class PackageDropTarget extends DropTargetAdapter {

	private TreeViewer viewer;
	
	public PackageDropTarget(TreeViewer viewer) {
		this.viewer = viewer;
		
		viewer.addDropSupport(DND.DROP_MOVE | DND.DROP_COPY,
				new Transfer[] { LocalSelectionTransfer.getTransfer(), ResourceTransfer.getInstance() }, this);
	}
	
	private PackageNodeImpl getDropTarget (DropTargetEvent event)
	{
		Point p = viewer.getTree().getDisplay().map(null, viewer.getTree(), event.x, event.y);
		TreeItem dropTarget = viewer.getTree().getItem(p);
		if (dropTarget == null)
			return null;
		
		Object data = dropTarget.getData();
		
		if (data instanceof PackageNodeImpl)
			return (PackageNodeImpl) data;
		else return null;
	}
	
	public void dragEnter(DropTargetEvent event) {
		// convert the drop to a "copy" event so it doesn't get actually deleted in the file system (resources only)
		if (ResourceTransfer.getInstance().isSupportedType(event.currentDataType))
		{
			if (event.detail == DND.DROP_MOVE || event.detail == DND.DROP_DEFAULT)
			{
				if ((event.operations & DND.DROP_COPY) != 0)
					event.detail = DND.DROP_COPY;
				else
					event.detail = DND.DROP_NONE;
			}
		}
		
		PackageNodeImpl node = getDropTarget(event);
		if (node == null || node.getNodeType() == IPackageNode.TYPE_PACKAGE_FILESET)
		{
			event.detail = DND.DROP_NONE;
		}
	}
	
	public void drop(DropTargetEvent event) {
		PackageNodeImpl target = getDropTarget(event);
		
		if (target == null)
			return;
		
		if (ResourceTransfer.getInstance().isSupportedType(event.currentDataType)
			&& (event.data instanceof IResource[]))
		{
			IResource[] resources = (IResource[]) event.data;
			
			for (int i = 0; i < resources.length; i++)
			{
				IResource resource = resources[i];;
				
				if (resource.getType() == IResource.FILE)
				{	
					PackageFileSetImpl fileset = new PackageFileSetImpl(target.getProject(), new XbFileSet());
					fileset.setSingleFile((IFile)resource);
					if (!resource.getProject().equals(target.getProject()))
					{
						fileset.setSourceProject(resource.getProject());
					}
					
					target.addChild(fileset);
				}
				else if (resource.getType() == IResource.FOLDER)
				{
					PackageFileSetImpl fileset = new PackageFileSetImpl(target.getProject(), new XbFileSet());
					fileset.setSourceContainer((IFolder)resource);
					fileset.setIncludesPattern("**/*");
					if (!resource.getProject().equals(target.getProject()))
					{
						fileset.setSourceProject(resource.getProject());
					}
					
					target.addChild(fileset);
				}
			}
			
			viewer.refresh();
			event.detail = DND.DROP_COPY;
		}
		
		else if (LocalSelectionTransfer.getTransfer().isSupportedType(event.currentDataType)
			&& (LocalSelectionTransfer.getTransfer().getSelection() != null))
		{
			IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer.getTransfer().getSelection();
			
			for (Iterator iter = selection.iterator(); iter.hasNext(); )
			{
				IPackageNodeWorkingCopy node = (IPackageNodeWorkingCopy) iter.next();
				target.addChild(node);
			}
		}
	}
}
