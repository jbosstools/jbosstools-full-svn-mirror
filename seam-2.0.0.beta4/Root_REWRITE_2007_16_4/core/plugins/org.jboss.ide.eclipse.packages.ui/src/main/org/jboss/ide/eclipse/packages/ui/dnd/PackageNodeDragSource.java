package org.jboss.ide.eclipse.packages.ui.dnd;

import java.util.Iterator;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.jboss.ide.eclipse.packages.core.model.IPackage;
import org.jboss.ide.eclipse.packages.core.model.internal.PackageNodeImpl;
import org.jboss.ide.eclipse.packages.core.model.internal.PackagesModel;

public class PackageNodeDragSource implements DragSourceListener {

	private TreeViewer viewer;
	private ITreeSelection selection;
	
	public PackageNodeDragSource(TreeViewer viewer)
	{
		this.viewer = viewer;
		
		viewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE,
			new Transfer[] { LocalSelectionTransfer.getTransfer() }, this);
	}

	public void dragStart(DragSourceEvent event) {
		event.doit = true;
	}
	
	public void dragSetData(DragSourceEvent event) {
		selection = (ITreeSelection) viewer.getSelection();
		
		if (LocalSelectionTransfer.getTransfer().isSupportedType(event.dataType))
		{
			LocalSelectionTransfer.getTransfer().setSelection(selection);
		}
	}

	
	public void dragFinished(DragSourceEvent event) {
		if (event.detail == DND.DROP_MOVE)
		{
			for (Iterator iter = selection.iterator(); iter.hasNext(); )
			{
				PackageNodeImpl node = (PackageNodeImpl) iter.next();
				
				if (node.getParent() != null)
				{
					PackageNodeImpl parent = (PackageNodeImpl) node.getParent();
					parent.removeChild(node);
				}
				else {
					PackagesModel.instance().removePackage((IPackage)node);
				}
				viewer.refresh();
			}
		}
	}
}
