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
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class CloudViewElement implements IAdaptable {

	private TreeViewer viewer;
	private Object element;
	private CloudViewElement parent;
	private Collection<CloudViewElement> children;
	protected AtomicBoolean initialized = new AtomicBoolean();

	public CloudViewElement(Object element, TreeViewer viewer) {
		this.element = element;
		this.viewer = viewer;
		children = Collections.synchronizedCollection(new ArrayList<CloudViewElement>());
		initDisposeListener(viewer);
	}

	public abstract String getName();

	public Object[] getChildren() {
		return children.toArray();
	}

	protected void clearChildren() {
		children.clear();
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public Object getParent() {
		return parent;
	}

	public void addChild(final CloudViewElement e) {
		addChildToModel(e);
		getViewer().getControl().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				getViewer().add(this, e);
			}
		});
	}

	public void addChildToModel(CloudViewElement element) {
		children.add(element);
		element.setParent(this);
	}

	public void addChildren(final CloudViewElement[] elements) {
		for (CloudViewElement element : elements) {
			addChildToModel(element);
		}

		getViewer().getControl().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				getViewer().add(this, elements);
			}
		});
	}

	public void setParent(CloudViewElement e) {
		parent = e;
	}

	public Object getElement() {
		return element;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IPropertySource.class) {
			IPropertySource p = getPropertySource();
			return p;
		}
		return null;
	}

	public abstract IPropertySource getPropertySource();

	protected TreeViewer getViewer() {
		return viewer;
	}

	private void initDisposeListener(Viewer viewer) {
		final Control control = viewer.getControl();
		control.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				control.addDisposeListener(new DisposeListener() {

					@Override
					public void widgetDisposed(DisposeEvent e) {
						dispose();
					}
				});
			}
		});
	}

	protected void dispose() {
		// nothing to do
	}
}
