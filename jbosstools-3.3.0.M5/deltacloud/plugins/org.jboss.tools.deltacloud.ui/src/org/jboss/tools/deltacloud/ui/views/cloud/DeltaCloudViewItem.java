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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class DeltaCloudViewItem<DELTACLOUDITEM> implements IAdaptable {

	private DELTACLOUDITEM model;
	private DeltaCloudViewItem<?> parent;
	private List<DeltaCloudViewItem<?>> children = new ArrayList<DeltaCloudViewItem<?>>();
	private TreeViewer viewer;
	private AtomicBoolean initialized = new AtomicBoolean();
	private Lock childrenLock = new ReentrantLock();

	protected DeltaCloudViewItem(DELTACLOUDITEM model, DeltaCloudViewItem<?> parent, TreeViewer viewer) {
		this.model = model;
		this.parent = parent;
		this.viewer = viewer;
		initDisposeListener(viewer);
	}

	public abstract String getName();

	public Object[] getChildren() {
		try {
			childrenLock.lock();
			return children.toArray();
		} finally {
			childrenLock.unlock();
		}
	}

	protected void clearChildren() {
		try {
			childrenLock.lock();
			children.clear();
		} finally {
			childrenLock.unlock();
		}
	}

	public boolean hasChildren() {
		try {
			childrenLock.lock();
			if (areChildrenInitialized()) {
				return children.size() > 0;
			}
			return true;
		} finally {
			childrenLock.unlock();
		}
	}

	public Object getParent() {
		return parent;
	}

	public void addChild(final DeltaCloudViewItem<?> element) {
		try {
			childrenLock.lock();
			children.add(element);
		} finally {
			childrenLock.unlock();
		}
	}

	public void addChildren(final DeltaCloudViewItem<?>[] elements) {
		try {
			childrenLock.lock();
			for (DeltaCloudViewItem<?> element : elements) {
				children.add(element);
			}
		} finally {
			childrenLock.unlock();
		}
	}

	public void removeChild(final DeltaCloudViewItem<?> element) {
		try {
			childrenLock.lock();
			children.remove(element);
		} finally {
			childrenLock.unlock();
		}
	}

	protected DeltaCloudViewItem<?> getCloudViewElement(DeltaCloud cloudToMatch) {
		try {
			childrenLock.lock();
			for (DeltaCloudViewItem<?> cloudElement : children) {
				DeltaCloud cloud = (DeltaCloud) cloudElement.getModel();
				if (cloudToMatch.equals(cloud)) {
					return cloudElement;
				}
			}
			return null;
		} finally {
			childrenLock.unlock();
		}
	}

	protected void expand() {
		getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				viewer.setExpandedState(DeltaCloudViewItem.this, true);
			}
		});
	}

	protected void refresh() {
		getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				viewer.refresh();
			}
		});
	}

	public DELTACLOUDITEM getModel() {
		return model;
	}

	protected void setChildrenInitialized(boolean initialized) {
		this.initialized.set(initialized);
	}

	protected boolean areChildrenInitialized() {
		return initialized.get();
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

	private void initDisposeListener(Viewer viewer) {
		final Control control = viewer.getControl();
		control.getDisplay().syncExec(new Runnable() {

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
	}

	protected Display getDisplay() {
		return viewer.getControl().getDisplay();
	}

	protected TreeViewer getViewer() {
		return viewer;
	}
}
