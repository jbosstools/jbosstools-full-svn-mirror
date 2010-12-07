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
public abstract class DeltaCloudViewElement implements IAdaptable {

	private Object model;
	private DeltaCloudViewElement parent;
	protected List<DeltaCloudViewElement> children =
			Collections.synchronizedList(new ArrayList<DeltaCloudViewElement>());
	protected TreeViewer viewer;
	protected AtomicBoolean initialized = new AtomicBoolean();

	protected DeltaCloudViewElement(Object model, DeltaCloudViewElement parent, TreeViewer viewer) {
		this.model = model;
		this.parent = parent;
		this.viewer = viewer;
		initDisposeListener(viewer);
	}

	public abstract String getName();

	public Object[] getChildren() {
		return children.toArray();
	}

	protected void clearChildren() {
		getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				// viewer.getTree().setRedraw(false);
				for (final DeltaCloudViewElement element : children) {
					viewer.remove(element);
				}
				// viewer.getTree().setRedraw(true);
			}
		});
		children.clear();
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public Object getParent() {
		return parent;
	}

	public void addChild(final DeltaCloudViewElement element) {
		children.add(element);

		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				viewer.add(DeltaCloudViewElement.this, element);
			}
		});
	}

	public void addChildren(final DeltaCloudViewElement[] elements) {
		for (DeltaCloudViewElement element : elements) {
			children.add(element);
		}

		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				viewer.add(DeltaCloudViewElement.this, elements);
			}
		});
	}

	public void removeChild(final DeltaCloudViewElement element) {

		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (element != null) {
					int index = children.indexOf(element);
					viewer.remove(DeltaCloudViewElement.this, index);
				}
			}
		});
	}

	protected void expand() {
		getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				viewer.setExpandedState(DeltaCloudViewElement.this, true);
			}
		});
	}

	public Object getModel() {
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

	protected Display getDisplay() {
		return viewer.getControl().getDisplay();
	}

	protected void dispose() {
		// nothing to do
	}
}
