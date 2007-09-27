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

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.*;
import org.eclipse.gef.*;

import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.tools.DeselectAllTracker;
import org.eclipse.gef.tools.MarqueeDragTracker;
import org.eclipse.swt.accessibility.AccessibleEvent;

import org.jboss.tools.shale.ui.clay.editor.figures.DiagramFigure;
import org.jboss.tools.shale.ui.clay.editor.model.*;

public class ClayDiagramEditPart extends ClayContainerEditPart
	implements LayerConstants, IClayModelListener {

	private DiagramFigure fig;

	public void setModel(Object model){
		super.setModel(model);
		((IClayModel)model).addClayModelListener(this);
	}

	public IClayModel getClayModel() {
		return (IClayModel)getModel();
	}

	public boolean isClayModelListenerEnabled(){
		return true;
	}

	public void processChanged(){
		fig.getLayoutManager().layout(fig);
	}

	public void setToFront(EditPart ep) {
		int index = getChildren().indexOf(ep);
		if (index == -1)
			return;
		if (index != getChildren().size() - 1)
			reorderChild(ep, getChildren().size() - 1);
	}

	public void componentAdd(IComponent group) {
		refresh();
	}

	public void componentRemove(IComponent group) {
		refresh();
	}

	public void linkAdd(ILink link) {}

	public void linkRemove(ILink link) {}

	protected AccessibleEditPart createAccessible() {
		return new AccessibleGraphicalEditPart() {
			public void getName(AccessibleEvent e) {
				e.result = "Clay Diagram";
			}
		};
	}

	/**
	 * Installs EditPolicies specific to this.
	 */
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.NODE_ROLE, null);
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, null);
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new ClayDiagramEditPolicy());
	}

/**
 * Returns a Figure to represent this. 
 *
 * @return  Figure.
 */
protected IFigure createFigure() {
	fig = new DiagramFigure(this);
	return fig;
}

public FreeformViewport getFreeformViewport(){
	return (FreeformViewport)getAncestor(fig, FreeformViewport.class);
}

public IFigure getAncestor(IFigure figure, Class cls){
	IFigure parent = fig;
	while(parent != null){
		if(parent.getClass().equals(cls)) return parent;
		parent = parent.getParent();
	}
	return null;
}

public DragTracker getDragTracker(Request req){
	if (req instanceof SelectionRequest 
		&& ((SelectionRequest)req).getLastButtonPressed() == 3)
			return new DeselectAllTracker(this);
	return new MarqueeDragTracker();
}

/**
 * Returns <code>NULL</code> as it does not hold any connections.
 *
 * @return  ConnectionAnchor
 */
public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart editPart) {
	return null;
}

/**
 * Returns <code>NULL</code> as it does not hold any connections.
 *
 * @return  ConnectionAnchor
 */
public ConnectionAnchor getSourceConnectionAnchor(int x, int y) {
	return null;
}

/**
 * Returns <code>NULL</code> as it does not hold any connections.
 *
 * @return  ConnectionAnchor
 */
public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart editPart) {
	return null;
}

	/**
	 * Returns <code>NULL</code> as it does not hold any connections.
	 * 
	 * @return ConnectionAnchor
	 */
	public ConnectionAnchor getTargetConnectionAnchor(int x, int y) {
		return null;
	}

	public void propertyChange(PropertyChangeEvent evt) {}

	protected void refreshVisuals() {}

	protected List getModelChildren() {
		return getClayModel().getVisibleComponentList();
	}

	public Object getAdapter(Class adapter) {
		if (adapter == SnapToHelper.class) {
			List<Object> snapStrategies = new ArrayList<Object>();
			Boolean val = (Boolean) getViewer().getProperty(
					RulerProvider.PROPERTY_RULER_VISIBILITY);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGuides(this));
			val = (Boolean) getViewer().getProperty(
					SnapToGeometry.PROPERTY_SNAP_ENABLED);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGeometry(this));
			val = (Boolean) getViewer().getProperty(
					SnapToGrid.PROPERTY_GRID_ENABLED);
			if (val != null && val.booleanValue())
				snapStrategies.add(new SnapToGrid(this));
			if (snapStrategies.size() == 0)
				return null;
			if (snapStrategies.size() == 1)
				return (SnapToHelper) snapStrategies.get(0);

			SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
			for (int i = 0; i < snapStrategies.size(); i++)
				ss[i] = (SnapToHelper) snapStrategies.get(i);
			return new CompoundSnapToHelper(ss);
		}
		return super.getAdapter(adapter);
	}

}
