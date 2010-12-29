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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class DeltaCloudViewItem<DELTACLOUDITEM> implements IAdaptable, PropertyChangeListener {

	private DELTACLOUDITEM model;
	private DeltaCloudViewItem<?> parent;
	protected List<DeltaCloudViewItem<?>> children =
			Collections.synchronizedList(new ArrayList<DeltaCloudViewItem<?>>());
	protected TreeViewer viewer;
	protected AtomicBoolean initialized = new AtomicBoolean();

	protected DeltaCloudViewItem(DELTACLOUDITEM model, DeltaCloudViewItem<?> parent, TreeViewer viewer) {
		this.model = model;
		this.parent = parent;
		this.viewer = viewer;
		initDisposeListener(viewer);
		addPropertyChangeListener(model);
	}

	public abstract String getName();

	public Object[] getChildren() {
		return children.toArray();
	}

	protected void clearChildren() {
		getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				viewer.remove(children.toArray());
				children.clear();
			}
		});
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public Object getParent() {
		return parent;
	}

	public void addChild(final DeltaCloudViewItem<?> element) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				viewer.add(DeltaCloudViewItem.this, element);
				children.add(element);
			}
		});
	}

	public void addChildren(final DeltaCloudViewItem<?>[] elements) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				viewer.add(DeltaCloudViewItem.this, elements);
				for (DeltaCloudViewItem<?> element : elements) {
					children.add(element);
				}
			}
		});
	}

	public void removeChild(final DeltaCloudViewItem<?> element) {
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				if (element != null) {
					int index = children.indexOf(element);
					viewer.remove(DeltaCloudViewItem.this, index);
				}
			}
		});
	}

	protected void expand() {
		getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				viewer.setExpandedState(DeltaCloudViewItem.this, true);
			}
		});
	}

	public DELTACLOUDITEM getModel() {
		return model;
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

	protected Display getDisplay() {
		return viewer.getControl().getDisplay();
	}

	protected void dispose() {
		removePropertyChangeListener(getModel());
	}

	public void propertyChange(PropertyChangeEvent event) {
		// do nothing
	}

	protected void removePropertyChangeListener(DELTACLOUDITEM model) {
		if (model != null) {
			try {
				Method method = model.getClass()
						.getMethod("removePropertyChangeListener", PropertyChangeListener.class);
				if (method != null) {
					method.invoke(model, this);
				}
			} catch (Exception e) {
				// ignore
			}
		}
	}

	protected abstract void addPropertyChangeListener(DELTACLOUDITEM object);
}
