package org.jboss.tools.birt.oda.ui.impl;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.hibernate.console.node.BaseNode;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;

public class HibernateDSDragSource implements DragSourceListener {

	private TreeViewer viewer;

	public HibernateDSDragSource(TreeViewer viewer) {
		this.viewer = viewer;
		DragSource source = new DragSource(viewer.getControl(),DND.DROP_COPY | DND.DROP_MOVE);
        source.setTransfer(new Transfer[] { LocalSelectionTransfer.getTransfer()});
        source.addDragListener(this);
	}

	public void dragFinished(DragSourceEvent event) {
	}

	public void dragSetData(DragSourceEvent event) {
		if (LocalSelectionTransfer.getTransfer()
				.isSupportedType(event.dataType)) {
			LocalSelectionTransfer.getTransfer().setSelection(
					viewer.getSelection());
		}
	}

	public void dragStart(DragSourceEvent event) {
		ISelection sel = viewer.getSelection();
		if (sel instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) sel;
			if (selection.size() == 1) {
				Object object = selection.getFirstElement();
				if (object instanceof RootClass
						|| object instanceof Property || object instanceof BaseNode) {
					event.doit = true;
					return;
				}
			}
		}
		event.doit = false;
	}

}
