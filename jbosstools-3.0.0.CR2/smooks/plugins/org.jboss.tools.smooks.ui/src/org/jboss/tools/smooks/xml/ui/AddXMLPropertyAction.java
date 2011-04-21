/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.smooks.ui.popup.SmooksAction;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;

/**
 * @author Dart
 *
 */
public class AddXMLPropertyAction extends SmooksAction {
	@Override
	public void run() {
		ISelection s = getSelection();
		if(s != null){
			IStructuredSelection selection = (IStructuredSelection)s;
			Object obj = selection.getFirstElement();
			if(obj instanceof TagObject){
				TagPropertyObject tag = new TagPropertyObject();
				tag.setCanEdit(true);
				tag.setName("property");
				Object v = this.getViewer();
				if(v != null && v instanceof PropertyChangeListener){
					tag.addNodePropetyChangeListener((PropertyChangeListener)v);
				}
				((TagObject)obj).addProperty(tag);
			}
		}
	}

	@Override
	public void setSelection(ISelection selection) {
		super.setSelection(selection);
		this.setEnabled(true);
		IStructuredSelection s = (IStructuredSelection)selection;
		Object obj = s.getFirstElement();
		if(obj instanceof AbstractXMLObject){
			this.setEnabled(((AbstractXMLObject)obj).isCanEdit());
		}
		if(obj == null || obj instanceof TagPropertyObject){
			this.setEnabled(false);
		}
	}
}
