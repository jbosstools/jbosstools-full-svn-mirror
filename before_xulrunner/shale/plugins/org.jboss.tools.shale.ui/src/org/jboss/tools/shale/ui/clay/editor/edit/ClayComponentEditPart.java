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
import java.beans.PropertyChangeListener;
import java.util.*;
import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Point;
import org.jboss.tools.common.model.ui.dnd.DnDUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.RelativeHandleLocator;
import org.eclipse.gef.handles.SquareHandle;
import org.eclipse.gef.requests.DropRequest;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Cursor;

import org.jboss.tools.common.meta.action.XAction;
import org.jboss.tools.common.model.*;
import org.jboss.tools.shale.ui.ShaleUiPlugin;
import org.jboss.tools.shale.ui.clay.editor.figures.ComponentFigure;
import org.jboss.tools.shale.ui.clay.editor.model.*;

public class ClayComponentEditPart extends ClayEditPart implements PropertyChangeListener, IComponentListener, EditPartListener {

	public void doDoubleClick(boolean cf){
		try {
			  XAction action = DnDUtil.getEnabledAction((XModelObject)getDefinitionModel().getSource(), null, "Open");
			  if(action != null) action.executeHandler((XModelObject)getDefinitionModel().getSource(),null);
		} catch (Exception e) {
			ShaleUiPlugin.log(e);
		}
	}
	
	public void deactivate() {
		getDefinitionModel().removePropertyChangeListener(this);
		getDefinitionModel().removeComponentListener(this);		
		super.deactivate();
	}

	public void componentChange() {
		refresh();
		fig.refreshFont();
		fig.repaint();
	}

	public boolean isComponentListenerEnable() {
		return true;
	}
	private ComponentFigure fig = null;
	
	public void doControlUp(){
	}
	
	public void doControlDown(){
	}

	public void doMouseHover(boolean cf){
	}
	
	public void setModel(Object model){
		super.setModel(model);
		((IComponent)model).addPropertyChangeListener(this);
		((IComponent)model).addComponentListener(this);
	}
	public void childAdded(EditPart child, int index){ 
	}
	public void partActivated(EditPart editpart) {
	}
	public void partDeactivated(EditPart editpart){
	} 
	public  void removingChild(EditPart child, int index){ 
	}
	public void selectedStateChanged(EditPart editpart){
	}
	
	public void propertyChange(PropertyChangeEvent evt){
	}
	
	public void linkAdd(ILink link){
		refreshTargetLink(link);
		refresh();
		List editParts = getSourceConnections();
		for(int i=0;i<editParts.size();i++){
			((ConnectionEditPart)editParts.get(i)).refresh();
		}
		getFigure().getParent().getLayoutManager().layout(getFigure().getParent());
	}
	
	private void refreshTargetLink(ILink link){
		if(link == null) return;
		ClayComponentEditPart gep = (ClayComponentEditPart)getViewer().getEditPartRegistry().get(link.getToComponent());
		if(gep == null) return;
		
		gep.refreshTargetConnections();
		gep.figure.repaint();
	}
	
	public void linkRemove(ILink link){
		refresh();
		refreshTargetLink(link);
		refresh();
		getFigure().getParent().getLayoutManager().layout(getFigure().getParent());
	}
	
	public void linkChange(ILink link, PropertyChangeEvent evet){
	}

	protected AccessibleEditPart createAccessible() {
		return new AccessibleGraphicalEditPart() {

			public void getName(AccessibleEvent e) {
				e.result = "EditPart";
			}

			public void getValue(AccessibleControlEvent e) {
				// e.result = Integer.toString(getPageModel().getValue());
			}
		};
	}

	protected List getModelTargetConnections() {
		return getDefinitionModel().getVisibleInputLinks();
	}

	protected List getModelSourceConnections() {
		if (getDefinitionModel().getLink() == null)
			return Collections.EMPTY_LIST;
		return Collections.singletonList(getDefinitionModel().getLink());// getDefinitionModel().getListOutputLinks();
	}

	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.NODE_ROLE, null);
		ClayNonResizableEditPolicy p = new ClayNonResizableEditPolicy();
		p.setDragAllowed(false);
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, p);
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ClayComponentEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
				new ClayComponentEditPolicy());
	}

	/**
	 * Returns a newly created Figure to represent this.
	 * 
	 * @return Figure of this.
	 */
	protected IFigure createFigure() {
		fig = new ComponentFigure(getDefinitionModel());
		((ComponentFigure) fig).setGroupEditPart(this);
		return fig;
	}

	public ComponentFigure getDefinitionFigure() {
		return (ComponentFigure)getFigure();
	}

	/**
	 * Returns the model of this as a LED.
	 * 
	 * @return Model of this as an LED.
	 */
	public IComponent getDefinitionModel() {
		return (IComponent)getModel();
	}

	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connEditPart) {
		ConnectionAnchor anc = getNodeFigure().getConnectionAnchor("1_IN");
		return anc;
	}

	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return getNodeFigure().getTargetConnectionAnchorAt(pt);
	}

	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connEditPart) {
		return getNodeFigure().getConnectionAnchor("1_OUT");
	}

	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point pt = new Point(((DropRequest) request).getLocation());
		return getNodeFigure().getSourceConnectionAnchorAt(pt);
	}

	protected List getModelChildren() {
		return Collections.EMPTY_LIST;
	}

	class ClayNonResizableEditPolicy extends NonResizableEditPolicy {
		public ClayNonResizableEditPolicy() {
			super();
		}

		protected List createSelectionHandles() {
			List<Handle> list = new ArrayList<Handle>();
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.SOUTH_EAST));
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.SOUTH_WEST));
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.NORTH_WEST));
			list.add(createHandle((GraphicalEditPart) getHost(),
					PositionConstants.NORTH_EAST));

			return list;
		}

		Handle createHandle(GraphicalEditPart owner, int direction) {
			SelectHandle handle = new SelectHandle(owner, direction);
			return handle;
		}

	}

	public class SelectHandle extends SquareHandle {
		public SelectHandle(GraphicalEditPart owner, int direction) {
			setOwner(owner);
			setLocator(new RelativeHandleLocator(owner.getFigure(), direction));
			setCursor(SharedCursors.NO);
		}

		public SelectHandle(GraphicalEditPart owner, Locator loc, Cursor c) {
			super(owner, loc, c);
		}

		protected DragTracker createDragTracker() {
			return null;
		}
	}

}
