package org.jboss.tools.smooks.gef.tree.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;

public class TreeNodeModel extends AbstractSmooksGraphicalModel {

	protected ITreeContentProvider contentProvider;

	protected ILabelProvider labelProvider;

	public TreeNodeModel(Object data, ITreeContentProvider contentProvider, ILabelProvider labelProvider) {
		super(data);
		setContentProvider(contentProvider);
		setLabelProvider(labelProvider);
		setData(data);
	}

	public boolean hasChildren() {
		if (contentProvider != null && data != null) {
			return contentProvider.hasChildren(data);
		}
		return true;
	}

	public ITreeContentProvider getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(ITreeContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}

	public void setLabelProvider(ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}

	protected TreeNodeModel createChildModel(Object model, ITreeContentProvider contentProvider,
			ILabelProvider labelProvider) {
		return new TreeNodeModel(model, contentProvider, labelProvider);
	}

	public List<AbstractSmooksGraphicalModel> getChildren() {
		if (children == null) {
			if (this.contentProvider != null && data != null) {
				Object[] models = contentProvider.getChildren(data);
				if (models != null) {
					children = new ArrayList<AbstractSmooksGraphicalModel>();
					for (int i = 0; i < models.length; i++) {
						Object model = models[i];
						TreeNodeModel n = createChildModel(model, contentProvider, labelProvider);
						if (n != null) {
							children.add(n);
							n.setParent(this);
						}
					}
					return children;
				}
			}
			return Collections.emptyList();
		} else {
			if (this.contentProvider != null && data != null) {
				Object[] models = contentProvider.getChildren(data);
				if (models != null) {
					for (int i = 0; i < models.length; i++) {
						Object model = models[i];
						// model = AdapterFactoryEditingDomain.unwrap(model);
						if (!childExsit(model)) {
							TreeNodeModel n = createChildModel(model, contentProvider, labelProvider);
							if (n != null) {
								children.add(n);
								n.setParent(this);
							}
						}
					}
					List<AbstractSmooksGraphicalModel> temp = new ArrayList<AbstractSmooksGraphicalModel>(children);
					for (Iterator<?> iterator = temp.iterator(); iterator.hasNext();) {
						AbstractSmooksGraphicalModel node = (AbstractSmooksGraphicalModel) iterator.next();
						Object data = node.getData();
						if (!graphicalChildExist(data, models)) {
							disconnectAllConnections(node);
							children.remove(node);
							node.setParent(null);
						}
					}
					temp.clear();
					temp = null;
				}
			}
		}
		return children;
	}

	public String getText() {
		if (data != null && labelProvider != null) {
			return labelProvider.getText(data);
		}
		return ""; //$NON-NLS-1$
	}

	public Image getImage() {
		if (data != null && labelProvider != null) {
			return labelProvider.getImage(data);
		}
		return null;
	}

	// public boolean isLinkable() {
	// return linkable;
	// }
	//
	// public void setLinkable(boolean linkable) {
	// this.linkable = linkable;
	// }

	public boolean canLinkWithSource(Object model) {
		return true;
	}

	public boolean canLinkWithTarget(Object model) {
		return true;
	}

	public void setText(String text) {
		// if (text != null && !text.equals(this.text)) {
		// String oldtext = this.text;
		// this.text = text;
		// support.firePropertyChange(PRO_TEXT_CHANGED, oldtext, this.text);
		// }
	}
}
