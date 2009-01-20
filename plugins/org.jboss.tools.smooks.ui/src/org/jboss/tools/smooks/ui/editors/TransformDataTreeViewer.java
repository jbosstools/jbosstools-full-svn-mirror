/**
 * 
 */
package org.jboss.tools.smooks.ui.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.jboss.tools.smooks.ui.IXMLStructuredObject;
import org.jboss.tools.smooks.xml.model.ITransformTreeNode;

/**
 * @author Dart
 * 
 */
public class TransformDataTreeViewer extends TreeViewer implements
		PropertyChangeListener {

	public static final String NODE_PROPERTY_EVENT = "__node_property_event";

	public static final String ADD_CHILDREN_EVENT = "__add_children_event";

	public static final String REMOVE_CHILDREN_EVENT = "__remove_children_event";

	private SmooksGraphicalFormPage editor;

	public TransformDataTreeViewer(Composite parent, int style,
			SmooksGraphicalFormPage editor) {
		super(parent, style);
		this.editor = editor;
	}
	
	public void inputChanged(Object newInput,Object oldInput){
		super.inputChanged(newInput, oldInput);
		if(newInput == oldInput) return;
		if(newInput != null && newInput instanceof ITransformTreeNode){
			((ITransformTreeNode)newInput).addNodePropetyChangeListener(this);
		}
		if(oldInput != null && oldInput instanceof ITransformTreeNode){
			((ITransformTreeNode)oldInput).removeNodePropetyChangeListener(this);
		}
	}
	

	public TransformDataTreeViewer(Composite parent,
			SmooksGraphicalFormPage editor) {
		super(parent);
		this.editor = editor;
	}

	public TransformDataTreeViewer(Tree tree, SmooksGraphicalFormPage editor) {
		super(tree);
		this.editor = editor;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String type = evt.getPropertyName();
		if (NODE_PROPERTY_EVENT.equals(type) || ADD_CHILDREN_EVENT.equals(type)) {
			this.refresh(evt.getSource());
		}
		if (REMOVE_CHILDREN_EVENT.equals(type)) {
			if (editor != null) {
				Object model = evt.getOldValue();
				removeGraphModel(model);
			}
			this.refresh(evt.getSource());
		}
	}

	private void removeGraphModel(Object model) {
		if (model == null)
			return;
		ITreeContentProvider provider = (ITreeContentProvider) this
				.getContentProvider();
		if (provider != null) {
			if (editor.clearGraphModel(model)) {
				Object[] children = provider.getChildren(model);
				if (children != null) {
					for (int i = 0; i < children.length; i++) {
						Object child = children[i];
						removeGraphModel(child);
					}
				}
			}
		}
	}

}
