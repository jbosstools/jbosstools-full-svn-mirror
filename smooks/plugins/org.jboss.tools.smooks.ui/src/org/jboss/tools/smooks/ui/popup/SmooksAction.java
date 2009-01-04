/**
 * 
 */
package org.jboss.tools.smooks.ui.popup;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext;

/**
 * @author Dart
 *
 */
public class SmooksAction extends Action implements ISmooksAction {

	protected SmooksConfigurationFileGenerateContext context;
	
	protected Object viewer;
	
	public Object getViewer() {
		return viewer;
	}

	public void setViewer(Object viewer) {
		this.viewer = viewer;
	}

	protected ISelection selection;
	
	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.ui.popup.ISmooksAction#getSelection()
	 */
	public ISelection getSelection() {
		return selection;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.ui.popup.ISmooksAction#getSmooksContext()
	 */
	public SmooksConfigurationFileGenerateContext getSmooksContext() {
		return context;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.ui.popup.ISmooksAction#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
		this.selection = selection;
	}

	/* (non-Javadoc)
	 * @see org.jboss.tools.smooks.ui.popup.ISmooksAction#setSmooksContext(org.jboss.tools.smooks.ui.modelparser.SmooksConfigurationFileGenerateContext)
	 */
	public void setSmooksContext(SmooksConfigurationFileGenerateContext context) {
		this.context = context;
	}
	
	public void run(){
		
	}

	public void selectionChanged(ISelection selection) {
		this.setSelection(selection);
	}

}
