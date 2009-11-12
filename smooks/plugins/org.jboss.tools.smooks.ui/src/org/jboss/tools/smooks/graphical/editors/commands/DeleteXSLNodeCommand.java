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
package org.jboss.tools.smooks.graphical.editors.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.TagPropertyObject;
import org.jboss.tools.smooks.configuration.editors.xml.XSLTagObject;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.editparts.TreeNodeEditPart;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeModel;
import org.jboss.tools.smooks.graphical.editors.SmooksGraphicalEditorPart;
import org.jboss.tools.smooks.graphical.editors.editparts.InputDataContainerEditPart;
import org.jboss.tools.smooks.graphical.editors.model.xsl.XSLNodeGraphicalModel;
import org.jboss.tools.smooks.graphical.editors.model.xsl.XSLTemplateGraphicalModel;

/**
 * @author Dart
 * 
 */
public class DeleteXSLNodeCommand extends Command {

	private AbstractSmooksGraphicalModel graphModel = null;

	private List<TreeNodeConnection> deletedConnections = null;

	private AbstractSmooksGraphicalModel parentModel = null;

	private EditPart parentEditPart = null;

	private EditPart hostEditPart;
	
	private int index = -1;

	public DeleteXSLNodeCommand(EditPart hostEditPart) {
		this.hostEditPart = hostEditPart;
		parentEditPart = this.hostEditPart.getParent();
		Object m = hostEditPart.getModel();
		if (m != null && m instanceof AbstractSmooksGraphicalModel) {
			graphModel = (AbstractSmooksGraphicalModel) m;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		if (hostEditPart == null) {
			return false;
		}
		if (graphModel == null)
			return false;
		if (graphModel.getParent() == null)
			return false;
		Object data = graphModel.getData();
		if (data instanceof XSLTagObject) {
			return true;
		}

		if (data instanceof TagPropertyObject) {
			Object parent = ((TagPropertyObject) data).getParent();
			if (parent != null && parent instanceof XSLTagObject) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#dispose()
	 */
	@Override
	public void dispose() {
		deletedConnections = null;
		super.dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		super.execute();
		if (deletedConnections != null) {
			deletedConnections.clear();
		} else {
			deletedConnections = new ArrayList<TreeNodeConnection>();
		}

		AbstractSmooksGraphicalModel parent = graphModel.getParent();
		this.parentModel = parent;
		AbstractSmooksGraphicalModel.disconnectAllConnections(graphModel, deletedConnections);
		index = parent.getChildrenWithoutDynamic().indexOf(graphModel);
		parent.removeChild(graphModel);
	}

	private void reconnectAllConnections() {
		if (deletedConnections != null) {
			for (Iterator<?> iterator = deletedConnections.iterator(); iterator.hasNext();) {
				TreeNodeConnection connection = (TreeNodeConnection) iterator.next();
				connection.connect();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	@Override
	public void redo() {
		execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		parentModel.addChild(index , graphModel);
		if (parentModel instanceof XSLTemplateGraphicalModel) {
			// children model hasbean changed:
			List<AbstractSmooksGraphicalModel> children = parentModel.getChildren();
			for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
				AbstractSmooksGraphicalModel abstractSmooksGraphicalModel = (AbstractSmooksGraphicalModel) iterator
						.next();
				if (abstractSmooksGraphicalModel instanceof XSLNodeGraphicalModel) {
					graphModel = abstractSmooksGraphicalModel;
					break;
				}
			}
		}

		List<?> children = parentEditPart.getChildren();
		for (Iterator<?> iterator = children.iterator(); iterator.hasNext();) {
			EditPart childpart = (EditPart) iterator.next();
			if (childpart.getModel() == graphModel) {
				hostEditPart = childpart;
				break;
			}
		}
		
		if (parentModel instanceof XSLTemplateGraphicalModel) {
			GraphicalViewer viewer = (GraphicalViewer)hostEditPart.getViewer();
			IEditorPart editor = ((DefaultEditDomain)viewer.getEditDomain()).getEditorPart();
			if(editor instanceof SmooksGraphicalEditorPart){
				List<TreeNodeConnection> newConnections = new ArrayList<TreeNodeConnection>();
				((SmooksGraphicalEditorPart)editor).createConnection(parentModel.getChildren(),newConnections);
				expandConnectionNodes(newConnections);
				return;
			}
		}
		
		try {
			reconnectAllConnections();
			expandConnectionNodes(deletedConnections);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void expandConnectionNodes(List<TreeNodeConnection> connections) {
		if (connections == null || connections.isEmpty())
			return;
		List<TreeNodeModel> expanedTreeNodeList = new ArrayList<TreeNodeModel>();
		for (Iterator<?> iterator = connections.iterator(); iterator.hasNext();) {
			TreeNodeConnection treeNodeConnection = (TreeNodeConnection) iterator.next();
			AbstractSmooksGraphicalModel source = treeNodeConnection.getSourceNode();
			AbstractSmooksGraphicalModel target = treeNodeConnection.getTargetNode();
			Object data = source.getData();
			Object data2 = target.getData();
			if (source instanceof TreeNodeModel && data instanceof IXMLStructuredObject) {
				expanedTreeNodeList.add((TreeNodeModel) source);
			}
			if (target instanceof TreeNodeModel && data2 instanceof IXMLStructuredObject) {
				expanedTreeNodeList.add((TreeNodeModel) target);
			}
		}
		if (hostEditPart != null) {
			if (hostEditPart instanceof TreeNodeEditPart) {
				SmooksUIUtils.expandGraphTree(expanedTreeNodeList, (TreeNodeEditPart) hostEditPart);
			}

			EditPart rootEditPart = hostEditPart.getViewer().getContents();
			List<?> childrenEditPart = rootEditPart.getChildren();
			for (Iterator<?> iterator = childrenEditPart.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				if (object instanceof InputDataContainerEditPart) {
					SmooksUIUtils.expandGraphTree(expanedTreeNodeList, (TreeNodeEditPart) object);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (parentModel != null) {
			return true;
		}
		return false;
	}

}
