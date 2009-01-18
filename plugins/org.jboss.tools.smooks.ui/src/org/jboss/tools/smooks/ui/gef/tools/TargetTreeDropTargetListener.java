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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Tool;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.smooks.ui.gef.commandprocessor.CommandProcessorFactory;
import org.jboss.tools.smooks.ui.gef.commands.CreateConnectionCommand;

/**
 * @author Dart Peng
 * @Date Aug 1, 2008
 */
public class TargetTreeDropTargetListener extends DropTargetAdapter {
	// GraphicalViewer
	TreeViewer hostViewer = null;
	GraphicalViewer graphicalViewer = null;

	public GraphicalViewer getGraphicalViewer() {
		return graphicalViewer;
	}

	public void setGraphicalViewer(GraphicalViewer graphicalViewer) {
		this.graphicalViewer = graphicalViewer;
	}

	public TargetTreeDropTargetListener(TreeViewer treeViewer,
			GraphicalViewer graphicalViewer) {
		hostViewer = treeViewer;
		setGraphicalViewer(graphicalViewer);
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		super.dragEnter(event);
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		super.dragLeave(event);
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		// TODO Auto-generated method stub
		super.dragOperationChanged(event);
	}

	@Override
	public void dragOver(DropTargetEvent event) {
		try {
			Point p = hostViewer.getTree().toControl(
					new Point(event.x, event.y));
			TreeItem item = hostViewer.getTree().getItem(p);
			if (item == null)
				return;
			Object model = item.getData();
			Tool activeTool = getGraphicalViewer().getEditDomain()
					.getActiveTool();
			SmooksCustomConnectionCreationTool tool = null;
			if (activeTool instanceof SmooksCustomConnectionCreationTool) {
				tool = (SmooksCustomConnectionCreationTool) activeTool;
			}
			if (tool != null) {
				tool.activeTargetEditPart(model, graphicalViewer);
				Event temp = new Event();
				temp.widget = event.widget;
				temp.x = p.x;
				temp.y = p.y;
				MouseEvent me = new MouseEvent(temp);
				getGraphicalViewer().getEditDomain().mouseMove(me,
						graphicalViewer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.dragOver(event);
	}

	@Override
	public void drop(DropTargetEvent event) {
		try {
			Object source = TemplateTransfer.getInstance().getTemplate();
			Tool activeTool = getGraphicalViewer().getEditDomain()
					.getActiveTool();
			SmooksCustomConnectionCreationTool tool = null;
			if (activeTool instanceof SmooksCustomConnectionCreationTool) {
				tool = (SmooksCustomConnectionCreationTool) activeTool;
			}
			if(tool == null) return;
			if (source == null) {
				source = tool.getSourceModel();
			}
			TreeItem item = (TreeItem) event.item;
			if (item != null) {
				Object target = item.getData();
				if (source != null && target != null) {
					source = tool.findTheEditPart(source,
							this.getGraphicalViewer()).getModel();
					EditPart targetEditPart = tool.findTheEditPart(target, this
							.getGraphicalViewer());
					if (targetEditPart != null) {
						target = targetEditPart.getModel();
						CreateConnectionCommand command = new CreateConnectionCommand();
						command.setSource(source);
						command.setTarget(target);
						CommandStack stack = getGraphicalViewer()
								.getEditDomain().getCommandStack();
						try {
							boolean cando = CommandProcessorFactory
									.getInstance().processGEFCommand(command,
											targetEditPart);
							if (!cando)
								return;
						} catch (CoreException e) {
							// ignore
						}
						stack.execute(command);
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			Tool dt = getGraphicalViewer().getEditDomain().getDefaultTool();
			getGraphicalViewer().getEditDomain().setActiveTool(dt);
		}
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		// TODO Auto-generated method stub
		super.dropAccept(event);
	}

	public TreeViewer getHostViewer() {
		return hostViewer;
	}

	public void setHostViewer(TreeViewer hostViewer) {
		this.hostViewer = hostViewer;
	}

}
