/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.smooks.ui.popup.SmooksAction;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;

/**
 * @author Dart
 * 
 */
public class RemoveXMLNodeAction extends SmooksAction {
	@Override
	public void run() {
		ISelection s = getSelection();
		if (s != null) {
			IStructuredSelection selection = (IStructuredSelection) s;
			Object obj = selection.getFirstElement();
			if (obj != null && obj instanceof AbstractXMLObject) {
				Object v = this.getViewer();
				AbstractXMLObject parent = ((AbstractXMLObject) obj)
						.getParent();
				if(parent instanceof TagObject){
					if(obj instanceof TagObject){
						((TagObject)parent).removeChildTag((TagObject)obj);
					}
					if(obj instanceof TagPropertyObject){
						((TagObject)parent).removeProperty((TagPropertyObject)obj);
					}
				}
				if(parent instanceof TagList){
					if(obj instanceof TagObject){
						((TagList)parent).removeRootTag((TagObject)obj);
					}
				}
				
				if (v != null && v instanceof PropertyChangeListener) {
					((AbstractXMLObject)obj).removeNodePropetyChangeListener((PropertyChangeListener)v);
				}
			}
		}
	}

	@Override
	public void setSelection(ISelection selection) {
		super.setSelection(selection);
		this.setEnabled(true);
		IStructuredSelection s = (IStructuredSelection) selection;
		Object obj = s.getFirstElement();
		if (obj instanceof AbstractXMLObject) {
			this.setEnabled(((AbstractXMLObject) obj).isCanEdit());
		}
	}
}
