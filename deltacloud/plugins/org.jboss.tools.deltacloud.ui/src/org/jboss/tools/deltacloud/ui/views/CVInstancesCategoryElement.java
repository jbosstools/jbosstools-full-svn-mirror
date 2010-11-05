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
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.IInstanceFilter;
import org.jboss.tools.deltacloud.core.IInstanceListListener;

public class CVInstancesCategoryElement extends CVCategoryElement implements IInstanceListListener {

	private Viewer viewer;
	private CVInstancesCategoryElement category;

	public CVInstancesCategoryElement(Object element, String name, Viewer viewer) {
		super(element, name, CVCategoryElement.INSTANCES);
		this.viewer = viewer;
		DeltaCloud cloud = (DeltaCloud) getElement();
		cloud.addInstanceListListener(this);
		viewer.getControl().addDisposeListener(onDispose());
		this.category = this;
	}

	private DisposeListener onDispose() {
		return new DisposeListener() {
			
			@Override
			public void widgetDisposed(DisposeEvent e) {
				DeltaCloud cloud = (DeltaCloud) getElement();
				if (cloud != null) {
					cloud.removeInstanceListListener(CVInstancesCategoryElement.this);
				}
			}
		};
	}

	protected void finalize() throws Throwable {
		DeltaCloud cloud = (DeltaCloud) getElement();
		cloud.removeInstanceListListener(this);
		super.finalize();
	}

	private void addInstances(DeltaCloudInstance[] instances) {
		if (instances.length > CVNumericFoldingElement.FOLDING_SIZE) {
			int min = 0;
			int max = CVNumericFoldingElement.FOLDING_SIZE;
			int length = instances.length;
			while (length > CVNumericFoldingElement.FOLDING_SIZE) {
				CVNumericFoldingElement f = new CVNumericFoldingElement(null,
						"[" + min + ".." + (max - 1) + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addChild(f);
				for (int i = min; i < max; ++i) {
					DeltaCloudInstance d = instances[i];
					CVInstanceElement element = new CVInstanceElement(d, d.getName());
					f.addChild(element);
				}
				min += CVNumericFoldingElement.FOLDING_SIZE;
				max += CVNumericFoldingElement.FOLDING_SIZE;
				length -= CVNumericFoldingElement.FOLDING_SIZE;
			}
			if (length > 0) {
				CVNumericFoldingElement f = new CVNumericFoldingElement(null,
						"[" + min + ".." + (max - 1) + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				addChild(f);
				for (int i = min; i < min + length; ++i) {
					DeltaCloudInstance d = instances[i];
					CVInstanceElement element = new CVInstanceElement(d, d.getName());
					f.addChild(element);
				}
			}
		} else {
			for (int i = 0; i < instances.length; ++i) {
				DeltaCloudInstance d = instances[i];
				CVInstanceElement element = new CVInstanceElement(d, d.getName());
				addChild(element);
			}
		}
	}

	@Override
	public Object[] getChildren() {
		if (!initialized) {
			DeltaCloud cloud = (DeltaCloud) getElement();
			cloud.removeInstanceListListener(this);
			DeltaCloudInstance[] instances = filter(cloud.getCurrInstances());
			addInstances(instances);
			initialized = true;
			cloud.addInstanceListListener(this);
		}
		return super.getChildren();
	}

	@Override
	public void listChanged(DeltaCloud cloud, DeltaCloudInstance[] newInstances) {
		clearChildren();
		final DeltaCloudInstance[] instances = filter(newInstances);
		addInstances(instances);
		initialized = true;
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				IStructuredSelection oldSelection = (IStructuredSelection) viewer.getSelection();
				((TreeViewer) viewer).refresh(category, false);
				restoreSelection(oldSelection);
			}

			/**
			 * This is a workaround:
			 * 
			 * When a change in the list of instances happens, DeltaCloud
			 * notifies this class with the list of all instances (@see
			 * DeltaCloud#performInstanceAction). This class then removes all
			 * children and readds new children with the same DeltaCloudInstance
			 * instances.
			 * 
			 * <p>
			 * I also tried an alternative approach where I implemented an
			 * equals method in CVInstanceElement that returns <code>true</code>
			 * if both elements have the same DeltaCloudInsta instance. The
			 * consequence is that the viewer keeps the selection, but is not
			 * aware of a change in the underlying items. The consequence is
			 * that the context-menu does not change its state (the instance
			 * action, that was executed, should disappear) and the properties
			 * view does not update either.
			 * 
			 * @param selection
			 * 
			 * @see DeltaCloud#performInstanceAction
			 * @see #listChanged
			 */
			private void restoreSelection(IStructuredSelection selection) {
				ISelection newSelection = new StructuredSelection(getChildrenWithSameDeltaCloudInstance(selection
						.toList()));
				viewer.setSelection(newSelection);
			}

			private List<CVInstanceElement> getChildrenWithSameDeltaCloudInstance(List<?> cvInstanceElements) {
				List<CVInstanceElement> cvInstances = new ArrayList<CVInstanceElement>();
				Object[] children = getChildren();
				for (Object member : cvInstanceElements) {
					CVInstanceElement cvInstance = (CVInstanceElement) member;
					DeltaCloudInstance instance = (DeltaCloudInstance) cvInstance.getElement();
					for (Object child : children) {
						CVInstanceElement childCvInstanceElement = (CVInstanceElement) child;
						DeltaCloudInstance childInstance = (DeltaCloudInstance) childCvInstanceElement.getElement();
						if (instance != null && instance.equals(childInstance)) {
							cvInstances.add(childCvInstanceElement);
						}
					}
				}
				return cvInstances;
			}

		});
	}

	private DeltaCloudInstance[] filter(DeltaCloudInstance[] input) {
		ArrayList<DeltaCloudInstance> array = new ArrayList<DeltaCloudInstance>();
		DeltaCloud cloud = (DeltaCloud) getElement();
		IInstanceFilter f = cloud.getInstanceFilter();
		for (int i = 0; i < input.length; ++i) {
			DeltaCloudInstance instance = input[i];
			if (f.isVisible(instance))
				array.add(instance);
		}
		return array.toArray(new DeltaCloudInstance[array.size()]);
	}

}
