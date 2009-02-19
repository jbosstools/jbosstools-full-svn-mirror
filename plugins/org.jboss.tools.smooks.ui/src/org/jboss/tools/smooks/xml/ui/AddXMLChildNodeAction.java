/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import java.beans.PropertyChangeListener;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.jboss.tools.smooks.ui.popup.SmooksAction;
import org.jboss.tools.smooks.xml.model.AbstractXMLObject;
import org.jboss.tools.smooks.xml.model.TagObject;
import org.jboss.tools.smooks.xml.model.TagPropertyObject;

/**
 * @author Dart
 * 
 */
public class AddXMLChildNodeAction extends SmooksAction {

	@Override
	public void run() {
		ISelection s = getSelection();
		if (s != null) {
			IStructuredSelection selection = (IStructuredSelection) s;
			Object obj = selection.getFirstElement();

			if (obj != null && obj instanceof TagObject) {
				TagObject tag = new TagObject();
				tag.setCanEdit(true);
				tag.setName("Node");
				Object v = this.getViewer();
				if (v != null && v instanceof PropertyChangeListener) {
					tag
							.addNodePropetyChangeListener((PropertyChangeListener) v);
				}
				Element element = new DefaultElement("Node");
				Element parentElement = ((TagObject) obj).getReferenceElement();
				if(parentElement != null){
					parentElement.add(element);
				}
				tag.setReferenceElement(element);
				((TagObject) obj).addChildTag(tag);
			}
		}
	}

	@Override
	public void setSelection(ISelection selection) {
		super.setSelection(selection);
		this.setEnabled(true);
		IStructuredSelection s = (IStructuredSelection) selection;
		Object obj = s.getFirstElement();
		if (obj == null) {
			this.setEnabled(false);
		}
		if (obj instanceof AbstractXMLObject) {
			this.setEnabled(((AbstractXMLObject) obj).isCanEdit());
		}
		if (obj instanceof TagPropertyObject) {
			this.setEnabled(false);
		}
	}
}
