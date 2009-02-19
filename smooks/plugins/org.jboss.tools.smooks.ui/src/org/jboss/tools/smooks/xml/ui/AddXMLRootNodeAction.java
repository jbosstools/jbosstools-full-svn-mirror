/**
 * 
 */
package org.jboss.tools.smooks.xml.ui;

import java.beans.PropertyChangeListener;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.tree.DefaultElement;
import org.eclipse.emf.ecore.xmi.DOMHelper;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.smooks.ui.popup.SmooksAction;
import org.jboss.tools.smooks.xml.model.TagList;
import org.jboss.tools.smooks.xml.model.TagObject;

/**
 * @author Dart
 * 
 */
public class AddXMLRootNodeAction extends SmooksAction {
	@Override
	public void run() {
		ISelection s = getSelection();
		if (s != null) {
			TreeViewer viewer = (TreeViewer) getViewer();
			Object obj = viewer.getInput();
			if (obj == null) {
				obj = new TagList();
			}
			if(!(obj instanceof TagList) ) return;
			TagObject tag = new TagObject();
			tag.setCanEdit(true);
			tag.setName("Node");
			Element element = new DefaultElement("Node");
			Document doc = DOMDocumentFactory.getInstance().createDocument();
			doc.setRootElement(element);
			tag.setReferenceElement(element);
			((TagList) obj).addRootTag(tag);
			Object v = this.getViewer();
			if (v != null && v instanceof TreeViewer) {
				((TreeViewer) v).setInput(obj);
				
			}
			if (v != null && v instanceof PropertyChangeListener) {
				((TagList)obj).addNodePropetyChangeListener((PropertyChangeListener) v);
				tag.addNodePropetyChangeListener((PropertyChangeListener) v);
			}
		}
	}

	@Override
	public void setSelection(ISelection selection) {
		super.setSelection(selection);
		this.setEnabled(true);
	}
}
