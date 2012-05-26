package org.jboss.tools.birt.oda.ui.impl;

import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.hibernate.console.node.BaseNode;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;

public class HibernateDSDropSource implements DropTargetListener {

	private SourceViewer viewer;

	public HibernateDSDropSource(SourceViewer viewer) {
		this.viewer = viewer;
		int operations = DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(viewer.getTextWidget(), operations);
		Transfer[] types = new Transfer[] { LocalSelectionTransfer
				.getTransfer() };
		target.setTransfer(types);
		target.addDropListener(this);
	}

	public void dragEnter(DropTargetEvent event) {
		viewer.getTextWidget().setFocus( );
		if ( event.detail == DND.DROP_DEFAULT )
			event.detail = DND.DROP_COPY;
		if ( event.detail != DND.DROP_COPY )
			event.detail = DND.DROP_NONE;
		
	}

	public void dragLeave(DropTargetEvent event) {
	}

	public void dragOperationChanged(DropTargetEvent event) {
		dragEnter(event);
	}

	public void dragOver(DropTargetEvent event) {
		event.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
	}

	public void drop(DropTargetEvent event) {
		if (LocalSelectionTransfer.getTransfer().isSupportedType(
				event.currentDataType)) {

			Object data = LocalSelectionTransfer.getTransfer().getSelection();
			if (data instanceof IStructuredSelection) {
				IStructuredSelection selection = (IStructuredSelection) event.data;
				Object source = selection.getFirstElement();
				String text = null;
				if (source instanceof RootClass) {
					RootClass table = (RootClass) source;
					text = getShortName(table.getEntityName());
				} else if (source instanceof Property) {
					Property property = (Property) source;
					text = property.getName();
				} else if (source instanceof BaseNode) {
					BaseNode node = (BaseNode) source;
					text = getShortName(node.getName());
				}
				if (text == null)
					return;
				StyledText textWidget = viewer.getTextWidget();
				int selectionStart = textWidget.getSelection().x;
				textWidget.insert(text);
				textWidget.setSelection(selectionStart + text.length());
				textWidget.setFocus();
			}
		}
	}

	private String getShortName(String name) {
		while (name.indexOf(".") > -1) //$NON-NLS-1$
			name = name.substring(name.indexOf(".") + 1); //$NON-NLS-1$
		return name;
	}

	public void dropAccept(DropTargetEvent event) {
	}

}
