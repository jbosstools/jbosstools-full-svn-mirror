/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.shale.ui.clay.editor.edit;

import java.beans.*;
import java.util.*;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.gef.*;
import org.jboss.tools.shale.ui.clay.editor.figures.NodeFigure;

abstract public class ClayEditPart
	extends org.eclipse.gef.editparts.AbstractGraphicalEditPart
	implements NodeEditPart, PropertyChangeListener {

	private AccessibleEditPart acc;

	public void activate() {
		if (isActive())	return;
		super.activate();
	}

	public void doDoubleClick(boolean cf){}
	public void doMouseUp(boolean cf){}
	public void doMouseDown(boolean cf){}
	public void doMouseHover(boolean cf){}
	public void doControlUp(){}
	public void doControlDown(){}

	protected void createEditPolicies(){
		installEditPolicy(EditPolicy.COMPONENT_ROLE,new ClayElementEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ClayNodeEditPolicy());
	}

	abstract protected AccessibleEditPart createAccessible();

	/**
	 * Makes the EditPart insensible to changes in the model
	 * by removing itself from the model's list of listeners.
	 */
	public void deactivate(){
		if (!isActive()) return;
		super.deactivate();
	}

	protected AccessibleEditPart getAccessibleEditPart() {
		if (acc == null)
			acc = createAccessible();
		return acc;
	}

	protected NodeFigure getNodeFigure(){
		return (NodeFigure) getFigure();
	}

	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connEditPart) {
		return null;
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connEditPart) {
		return null;
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return null;
	}

	final protected String mapConnectionAnchorToTerminal(ConnectionAnchor c){
		return getNodeFigure().getConnectionAnchorName(c);
	}

	public void propertyChange(PropertyChangeEvent evt){
		refreshVisuals();
	}

	protected void refreshVisuals() {
	}

	protected void refreshSourceConnections() {
		int i;
		ConnectionEditPart editPart;
		Object model;

		Map<Object,Object> modelToEditPart = new HashMap<Object,Object>();
		List editParts = getSourceConnections();

		for (i = 0; i < editParts.size(); i++) {
			editPart = (ConnectionEditPart)editParts.get(i);
			modelToEditPart.put(editPart.getModel(), editPart);
		}

		List modelObjects = getModelSourceConnections();
		if (modelObjects == null) modelObjects = new ArrayList();

		for (i = 0; i < modelObjects.size(); i++) {
			model = modelObjects.get(i);
		
			if (i < editParts.size()) {
				editPart =  (ConnectionEditPart)editParts.get(i);
				if (editPart.getModel() == model) {
					if (editPart.getSource() != this)
						editPart.setSource(this);
					continue;
				}
			}
		
			editPart = (ConnectionEditPart) modelToEditPart.get(model);
			if (editPart != null)
				reorderSourceConnection(editPart, i);
			else {
				editPart = createOrFindConnection(model);
				addSourceConnection(editPart, i);
			}
		}

		//Remove the remaining EditParts
		List<Object> trash = new ArrayList<Object>();
		for (; i < editParts.size(); i++)
			trash.add(editParts.get(i));
		for (i = 0; i < trash.size(); i++)
			removeSourceConnection((ConnectionEditPart)trash.get(i));
	}

	protected void refreshTargetConnections() {
		int i;
		ConnectionEditPart editPart;
		Object model;

		Map<Object,Object> mapModelToEditPart = new HashMap<Object,Object>();
		List connections = getTargetConnections();

		for (i = 0; i < connections.size(); i++) {
			editPart = (ConnectionEditPart)connections.get(i);
			mapModelToEditPart.put(editPart.getModel(), editPart);
		}

		List modelObjects = getModelTargetConnections();
		if (modelObjects == null) modelObjects = new ArrayList();

		for (i = 0; i < modelObjects.size(); i++) {
			model = modelObjects.get(i);
		
			if (i < connections.size()) {
				editPart =  (ConnectionEditPart)connections.get(i);
				if (editPart.getModel() == model) {
					if (editPart.getTarget() != this)
						editPart.setTarget(this);
					continue;
				}
			}

			editPart = (ConnectionEditPart)mapModelToEditPart.get(model);
			if (editPart != null)
				reorderTargetConnection(editPart, i);
			else {
				editPart = createOrFindConnection(model);
				addTargetConnection(editPart, i);
			}
		}

		//Remove the remaining Connection EditParts
		List<Object> trash = new ArrayList<Object>();
		for (; i < connections.size(); i++)
			trash.add(connections.get(i));
		for (i = 0; i < trash.size(); i++)
			removeTargetConnection((ConnectionEditPart)trash.get(i));
	}

	protected void removeSourceConnection(ConnectionEditPart connection) {
		if(connection.getSource() != this) return;
		fireRemovingSourceConnection(connection, getSourceConnections().indexOf(connection));
		connection.deactivate();
		connection.setSource(null);
		primRemoveSourceConnection(connection);
	}

	protected void removeTargetConnection(ConnectionEditPart connection) {
		if(connection.getTarget() != this) return;
		fireRemovingTargetConnection(connection, getTargetConnections().indexOf(connection));
		connection.setTarget(null);
		primRemoveTargetConnection(connection);
	}

}
