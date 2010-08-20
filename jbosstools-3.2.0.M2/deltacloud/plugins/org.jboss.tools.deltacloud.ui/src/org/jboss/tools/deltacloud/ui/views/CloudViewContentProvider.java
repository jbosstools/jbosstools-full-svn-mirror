package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class CloudViewContentProvider implements ITreeContentProvider {

	private CloudViewElement root;
	
	@Override
	public Object[] getChildren(Object parentElement) {
		CloudViewElement e = (CloudViewElement)parentElement;
		return e.getChildren();
	}

	@Override
	public Object getParent(Object element) {
		CloudViewElement e = (CloudViewElement)element;
		return e.getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		CloudViewElement e = (CloudViewElement)element;
		return e.hasChildren();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return root.getChildren();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		root = (CloudViewElement)newInput;
	}

}
