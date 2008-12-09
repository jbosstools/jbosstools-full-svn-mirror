/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.tools;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Tool;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;

/**
 * @author Dart Peng
 * @Date Aug 1, 2008
 */
public class MappingPanelDropTargetListener implements
		TransferDropTargetListener {
	GraphicalViewer graphicalViewer = null;
	
	public MappingPanelDropTargetListener(GraphicalViewer viewer){
		setGraphicalViewer(viewer);
	}

	public GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}

	public void setGraphicalViewer(GraphicalViewer graphicalViewer) {
		this.graphicalViewer = graphicalViewer;
	}

	public void dragEnter(DropTargetEvent event) {
		IStructuredSelection selection = (IStructuredSelection) TemplateTransfer
				.getInstance().getTemplate();
		if (selection == null)
			return;
		if (selection.isEmpty())
			return;
		if (selection.toList().size() > 1)
			return;
		getGraphicalViewer().getEditDomain().setActiveTool(
				new SmooksCustomConnectionCreationTool(selection.getFirstElement(),
						getGraphicalViewer()));
	}

	public void dragLeave(DropTargetEvent event) {
	}

	public void dragOperationChanged(DropTargetEvent event) {
	}

	public void dragOver(DropTargetEvent event) {
		Event temp = new Event();
		Point p = getGraphicalViewer().getControl().toControl(
				new Point(event.x, event.y));
		temp.x = p.x;
		temp.y = p.y;
		temp.widget = event.widget;
		MouseEvent e = new MouseEvent(temp);
		Tool tool = getGraphicalViewer().getEditDomain().getActiveTool();
		if (tool != null) {
			tool.mouseMove(e, getGraphicalViewer());
		}
	}

	public void drop(DropTargetEvent event) {
		EditDomain domain = getGraphicalViewer().getEditDomain();
		domain.setActiveTool(domain.getDefaultTool());
	}

	public void dropAccept(DropTargetEvent event) {
	}

	public Transfer getTransfer() {
		return TemplateTransfer.getInstance();
	}

	public boolean isEnabled(DropTargetEvent event) {
		return true;
	}

}
