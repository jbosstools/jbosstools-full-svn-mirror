package org.jboss.tools.smooks.ui.gef.editparts;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.jboss.tools.smooks.ui.gef.figures.GraphRootFigureLayout;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;
import org.jboss.tools.smooks.ui.gef.policy.RootGraphicsXYLayoutEditPolicy;

public class RootModelEditPart extends AbstractStructuredDataEditPart {

	protected IFigure createFigure() {
		IFigure figure = new ScalableFreeformLayeredPane();
		figure.setLayoutManager(new GraphRootFigureLayout());
		return figure;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName()
				.equals(AbstractStructuredDataModel.P_CHILDREN)) {
			refreshChildren();
		}
		if (evt.getPropertyName().equals(
				AbstractStructuredDataModel.P_REFRESH_PANEL)) {
			this.getFigure().invalidate();
			this.getFigure().validate();
			refreshAllConnectionLineStyle();
		}
	}

	protected void refreshAllConnectionLineStyle() {
		List children = this.getChildren();
		for (Iterator iterator = children.iterator(); iterator.hasNext();) {
			ConnectionPointEditPart editPart = (ConnectionPointEditPart) iterator
					.next();
			if (editPart instanceof SourceConnectionPointEditPart) {
				editPart.refreshAllSourceConnectionLineStyle();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.LAYOUT_ROLE,
				new RootGraphicsXYLayoutEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren() {
		return ((AbstractStructuredDataModel) getModel()).getChildren();
	}
}