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
package org.jboss.tools.smooks.ui.gef.editparts;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;
import org.jboss.tools.smooks.ui.gef.model.GraphicalModelListenerManager;
import org.jboss.tools.smooks.ui.gef.model.IConnectableModel;
import org.jboss.tools.smooks.ui.gef.model.IGraphicalModelListener;
import org.jboss.tools.smooks.ui.gef.model.TreeItemRelationModel;
import org.jboss.tools.smooks.ui.gef.policy.CustomGraphicalNodeEditPolicy;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart Peng
 * @Date Jul 30, 2008
 */
public class ConnectionPointEditPart extends AbstractStructuredDataEditPart
		implements NodeEditPart {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		RectangleFigure figure = new RectangleFigure();
		figure.setSize(5, 5);
		return figure;
	}

	public boolean isCollapse() {
		TreeItemRelationModel model = (TreeItemRelationModel) this.getModel();
		if (model == null)
			return false;
		return model.isCollapse();
	}

	@Override
	protected List getModelSourceConnections() {
		Object model = getModel();
		if (model instanceof IConnectableModel) {
			return ((IConnectableModel) model).getModelSourceConnections();
		}
		return super.getModelSourceConnections();
	}

	@Override
	protected List getModelTargetConnections() {
		Object model = getModel();
		if (model instanceof IConnectableModel) {
			return ((IConnectableModel) model).getModelTargetConnections();
		}
		return super.getModelTargetConnections();
	}

	public void refreshAllSourceConnectionLineStyle() {
		List sourceConnection = this.getSourceConnections();
		for (Iterator iterator = sourceConnection.iterator(); iterator
				.hasNext();) {
			StructuredDataConnectionEditPart connection = (StructuredDataConnectionEditPart) iterator
					.next();
			connection.refresh();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
				new CustomGraphicalNodeEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
	 * PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String pname = evt.getPropertyName();
		String sid = getSourceID();
		String tid = getTargetID();
		SmooksGraphicalFormPage page = getSmooksGraphicalPage();
		SmooksConfigurationFileGenerateContext context = null;
		if(page != null){
			context = page.getSmooksConfigurationFileGenerateContext();
		}
		IGraphicalModelListener listener = GraphicalModelListenerManager
				.getInstance().getPaintListener(sid, tid);
		if (IConnectableModel.P_ADD_SOURCE_CONNECTION.equals(pname) ||
				IConnectableModel.P_REMOVE_SOURCE_CONNECTION.equals(pname)) {
			if (listener != null) {
				if(IConnectableModel.P_ADD_SOURCE_CONNECTION.equals(pname)){
					listener.modelAdded(evt.getNewValue(), context);
				}
				if(IConnectableModel.P_REMOVE_SOURCE_CONNECTION.equals(pname)){
					listener.modelRemoved(evt.getOldValue(), context);
				}
			}
			this.refreshSourceConnections();
		}
		if (IConnectableModel.P_ADD_TARGET_CONNECTION.equals(pname) ||
				IConnectableModel.P_REMOVE_TARGET_CONNECTION.equals(pname)) {
			this.refreshTargetConnections();
		}
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return new ChopboxAnchor(getFigure());
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}

}
