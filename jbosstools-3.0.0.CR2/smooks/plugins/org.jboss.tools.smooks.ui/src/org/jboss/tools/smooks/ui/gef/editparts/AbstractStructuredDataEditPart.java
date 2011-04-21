package org.jboss.tools.smooks.ui.gef.editparts;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.ui.editors.SmooksGraphicalFormPage;
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
	
	public String getSourceID(){
		SmooksGraphicalFormPage page = getSmooksGraphicalPage();
		if(page != null){
			return page.getSourceDataTypeID();
		}
		return null;
	}
	
	public String getTargetID(){
		SmooksGraphicalFormPage page = getSmooksGraphicalPage();
		if(page != null){
			return page.getTargetDataTypeID();
		}
		return null;
	}
	
	public SmooksGraphicalFormPage getSmooksGraphicalPage(){
		GraphicalViewer viewer = (GraphicalViewer) this.getViewer();
		DefaultEditDomain domain = (DefaultEditDomain) viewer.getEditDomain();
		IEditorPart part = domain.getEditorPart();
		if(part instanceof SmooksGraphicalFormPage){
			return (SmooksGraphicalFormPage)part;
		}
		return null;
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