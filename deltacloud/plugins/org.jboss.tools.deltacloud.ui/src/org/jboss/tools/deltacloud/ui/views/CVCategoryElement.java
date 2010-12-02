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
package org.jboss.tools.deltacloud.ui.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class CVCategoryElement extends CloudViewElement {

	protected boolean initialized;
	private TreeViewer viewer;

	public CVCategoryElement(Object element, TreeViewer viewer) {
		super(element);
		this.viewer = viewer;
		viewer.getControl().addDisposeListener(onDispose());
	}

	@Override
	public Object[] getChildren() {
		return super.getChildren();
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public IPropertySource getPropertySource() {
		// no property source for cathegories
		return null;
	}

	protected Viewer getViewer() {
		return viewer;
	}

	protected void refresh() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				IStructuredSelection oldSelection = (IStructuredSelection) viewer.getSelection();
				((TreeViewer) viewer).refresh(CVCategoryElement.this, false);
				restoreSelection(oldSelection);
			}
		});
	}

	/**
	 * This is a workaround:
	 * 
	 * When a change in the list of instances happens, DeltaCloud notifies this
	 * class with the list of all instances (@see
	 * DeltaCloud#performInstanceAction). This class then removes all children
	 * and readds new children with the same DeltaCloudInstance instances.
	 * 
	 * <p>
	 * I also tried an alternative approach where I implemented an equals method
	 * in CVInstanceElement that returns <code>true</code> if both elements have
	 * the same DeltaCloudInsta instance. The consequence is that the viewer
	 * keeps the selection, but is not aware of a change in the underlying
	 * items. The consequence is that the context-menu does not change its state
	 * (the instance action, that was executed, should disappear) and the
	 * properties view does not update either.
	 * 
	 * @param selection
	 * 
	 * @see DeltaCloud#performInstanceAction
	 * @see #listChanged
	 */
	private void restoreSelection(IStructuredSelection selection) {
		List<?> newSelectedElements = getChildrenWithSameElement(selection.toList());
		if (newSelectedElements != null && newSelectedElements.size() > 0) {
			ISelection newSelection = new StructuredSelection(newSelectedElements);
			viewer.setSelection(newSelection);
		}
	}

	/**
	 * Returns the children of this category element that have the same elements
	 * (#getElement) as the given items. This method is used to restore
	 * selection after the {@link CVInstanceElement} or {@link CVImageElement}
	 * have been recreated. They still have the same {@link DeltaCloudInstance}
	 * or {@link DeltaCloudImage}.
	 * 
	 * @param elementsToMatch
	 *            the elements to match
	 * @return the children with same element
	 * 
	 * @see #CloudViewElement#getElement
	 * @see CVInstanceElement
	 * @see CVImageElement
	 */
	private List<?> getChildrenWithSameElement(List<?> itemsToMatch) {
		if (itemsToMatch == null || itemsToMatch.size() == 0) {
			return Collections.EMPTY_LIST;
		}
		List<Object> children = new ArrayList<Object>();
		for (Object itemToMatch : itemsToMatch) {
			Object elementToMatch = ((CloudViewElement) itemToMatch).getElement();
			if (elementToMatch != null) {
				for (Object child : getChildren()) {
					Object childElement = ((CloudViewElement) child).getElement();
					if (elementToMatch.equals(childElement)) {
						children.add(child);
					}
				}
			}
		}
		return children;
	}

	private DisposeListener onDispose() {
		return new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				dispose();
			}
		};
	}

	protected abstract void dispose();
}
