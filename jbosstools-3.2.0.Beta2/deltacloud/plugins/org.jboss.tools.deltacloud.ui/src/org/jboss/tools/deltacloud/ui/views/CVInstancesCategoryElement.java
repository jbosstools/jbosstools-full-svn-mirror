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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.IInstanceFilter;
import org.jboss.tools.deltacloud.core.IInstanceListListener;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CVInstancesCategoryElement extends CVCategoryElement implements IInstanceListListener {

	public CVInstancesCategoryElement(Object element, String name, TreeViewer viewer) {
		super(element, name, viewer);
		DeltaCloud cloud = (DeltaCloud) getElement();
		cloud.addInstanceListListener(this);
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
					CVInstanceElement element = new CVInstanceElement(d);
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
					CVInstanceElement element = new CVInstanceElement(d);
					f.addChild(element);
				}
			}
		} else {
			for (int i = 0; i < instances.length; ++i) {
				DeltaCloudInstance d = instances[i];
				CVInstanceElement element = new CVInstanceElement(d);
				addChild(element);
			}
		}
	}

	@Override
	public Object[] getChildren() {
		if (!initialized) {
			DeltaCloud cloud = (DeltaCloud) getElement();
			try {
				cloud.removeInstanceListListener(this);
				DeltaCloudInstance[] instances = filter(cloud.getInstances());
				addInstances(instances);
				initialized = true;
			} catch (Exception e) {
				// TODO: internationalize strings
				ErrorUtils.handleError(
						"Error",
						"Colud not get instances from cloud " + cloud.getName(),
						e, Display.getDefault().getActiveShell());
			} finally {
				cloud.addInstanceListListener(this);
			}
		}
		return super.getChildren();
	}

	@Override
	public void listChanged(DeltaCloud cloud, DeltaCloudInstance[] newInstances) {
		clearChildren();
		final DeltaCloudInstance[] instances = filter(newInstances);
		addInstances(instances);
		initialized = true;
		refresh();
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

	protected void dispose() {
		DeltaCloud cloud = (DeltaCloud) getElement();
		if (cloud != null) {
			cloud.removeInstanceListListener(this);
		}
	}
}
