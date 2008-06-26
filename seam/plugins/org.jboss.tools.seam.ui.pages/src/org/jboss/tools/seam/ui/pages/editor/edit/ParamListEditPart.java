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
package org.jboss.tools.seam.ui.pages.editor.edit;

import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.jboss.tools.seam.ui.pages.editor.figures.NodeFigure;
import org.jboss.tools.seam.ui.pages.editor.figures.ParamListFigure;

public class ParamListEditPart extends PagesEditPart implements PropertyChangeListener, Adapter {
	private ParamListFigure fig = null;

	private boolean single = true;

	public boolean isSingle() {
		return single;
	}


	protected AccessibleEditPart createAccessible() {
		return new AccessibleGraphicalEditPart() {

			public void getName(AccessibleEvent e) {
				e.result = "EditPart";
			}

			public void getValue(AccessibleControlEvent e) {
			}

		};
	}

	protected void createEditPolicies() {
		//super.createEditPolicies();
		installEditPolicy(EditPolicy.NODE_ROLE, null);
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);
		//installEditPolicy(EditPolicy.COMPONENT_ROLE, new PageEditPolicy());
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new JSFFlowEditPolicy());
		//installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
		//		new PageEditPolicy());
	}

	/**
	 * Returns a newly created Figure to represent this.
	 * 
	 * @return Figure of this.
	 */

	protected IFigure createFigure() {
		fig = new ParamListFigure(getExceptionModel());
		((ParamListFigure) fig).setEditPart(this);
		return fig;
	}

	/**
	 * Returns the model of this as a LED.
	 * 
	 * @return Model of this as an LED.
	 */
	public PageWrapper getExceptionModel() {
		return (PageWrapper) getModel();
	}

	Dimension size;

	

	protected void refreshVisuals() {
		Point loc = getExceptionModel().getPage().getLocation().getCopy();
		loc.y += 25+getExceptionModel().getPage().getOutputLinks().size()*NodeFigure.LINK_HEIGHT;
		size = new Dimension(200, getExceptionModel().getPage().getChildren().size()*19);
		adjustForGrid(loc);

		Rectangle r = new Rectangle(loc, size);

		((GraphicalEditPart) getParent()).setLayoutConstraint(this,
				getFigure(), r);
		
		((PagesDiagramEditPart) ParamListEditPart.this.getParent())
		.setToFront(this);
		
	}

	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connEditPart) {
		ConnectionAnchor anc = getNodeFigure().getConnectionAnchor("1_IN");
		return anc;
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return getNodeFigure().getTargetConnectionAnchorAt(pt);
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		if (single) {
			Point pt = new Point(((DropRequest) request).getLocation());
			return getNodeFigure().getSourceConnectionAnchorAt(pt);
		} else
			return super.getSourceConnectionAnchor(request);
	}

	protected List getModelChildren() {
		return getExceptionModel().getPage().getChildren();
	}

	protected void refreshChildren() {
		super.refreshChildren();
		for (int i = 0; i < getChildren().size(); i++) {
			((ParamEditPart) getChildren().get(i)).refresh();

		}
	}
	
	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		if (isActive())
			return;
		((Notifier) getExceptionModel().getPage()).eAdapters().add(this);
		super.activate();
	}
	
	public void deactivate(){
		if (!isActive())
			return;
		((Notifier) getExceptionModel().getPage()).eAdapters().remove(this);
		super.deactivate();
	}
	
	/**
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		refresh();
		refreshVisuals();
	}
	/**
	 * )
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {

		return null;
	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return false;
	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
	}
}
