/**
 * 
 */
package org.jboss.tools.smooks.graphical.editors.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.jboss.tools.smooks.gef.common.RootModel;
import org.jboss.tools.smooks.gef.tree.editpolicy.RootPanelXYLayoutEditPolicy;
import org.jboss.tools.smooks.gef.tree.editpolicy.SmooksRootEditPartLayoutEditPolicy;
import org.jboss.tools.smooks.graphical.figures.ContainerFigure;
import org.jboss.tools.smooks.graphical.figures.TargetContainerFigure;

/**
 * @author Dart
 *
 */
public class OutputContainerEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener{

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		IFigure fi =  new TargetContainerFigure();
		fi.setBounds(new Rectangle(500,50,400,300));
		return fi;
	}

	@Override
	public IFigure getContentPane() {
		return ((ContainerFigure)getFigure()).getContentsPane();
	}
	
	

	@Override
	public DragTracker getDragTracker(Request request) {
		return new RightClickSelectMarqueeDragTraker();
//		return super.getDragTracker(request);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		((RootModel)getModel()).addPropertyChangeListener(this);
		super.activate();
	}



	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		((RootModel)getModel()).removePropertyChangeListener(this);
	}



	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new RootPanelXYLayoutEditPolicy());
		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new SmooksRootEditPartLayoutEditPolicy());
	}
	
	protected List<?> getModelChildren(){
		RootModel list = (RootModel) getModel();
		return list.getChildren();
	}



	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals(RootModel.ADD_CHILDREN) || evt.getPropertyName().equals(RootModel.REMOVE_CHILDREN)){
			this.refreshChildren();
		}
	}
}
