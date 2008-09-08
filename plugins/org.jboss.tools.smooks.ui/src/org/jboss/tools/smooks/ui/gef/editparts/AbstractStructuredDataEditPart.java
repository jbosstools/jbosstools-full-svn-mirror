package org.jboss.tools.smooks.ui.gef.editparts;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.jboss.tools.smooks.ui.gef.model.AbstractStructuredDataModel;

/**
 * 
 * @author Dart Peng
 * 
 */
public abstract class AbstractStructuredDataEditPart extends
		AbstractGraphicalEditPart implements PropertyChangeListener {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	public void activate() {
		super.activate();
		((AbstractStructuredDataModel) getModel())
				.addPropertyChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		((AbstractStructuredDataModel) getModel())
				.removePropertyChangeListener(this);
		super.deactivate();
	}
}