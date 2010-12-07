/*******************************************************************************
 * Copyright (c) 2010 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *******************************************************************************/
package org.jboss.tools.deltacloud.ui.views.cloud;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class CloudViewContentProvider implements ITreeContentProvider {

	private CloudViewElement root;

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof CloudViewElement) {
			return ((CloudViewElement) element).getChildren();	
		} else {
			return null;
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof CloudViewElement) {
			return ((CloudViewElement) element).getParent();
		} else {
			return null;
		}
	}

	@Override
	public boolean hasChildren(Object element) {
		CloudViewElement e = (CloudViewElement) element;
		return e.hasChildren();
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return root.getChildren();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		root = (CloudViewElement) newInput;
	}

}
