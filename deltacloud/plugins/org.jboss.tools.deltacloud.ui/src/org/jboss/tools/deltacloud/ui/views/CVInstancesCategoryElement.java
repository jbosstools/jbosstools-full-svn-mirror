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

import java.text.MessageFormat;

import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.GetInstancesCommand;
import org.jboss.tools.deltacloud.core.IInstanceFilter;
import org.jboss.tools.deltacloud.core.IInstanceListListener;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class CVInstancesCategoryElement extends CVCategoryElement implements IInstanceListListener {

	private static final String INSTANCE_CATEGORY_NAME = "InstanceCategoryName"; //$NON-NLS-1$

	public CVInstancesCategoryElement(Object element, TreeViewer viewer) {
		super(element, viewer);
		DeltaCloud cloud = (DeltaCloud) getElement();
		cloud.addInstanceListListener(this);
	}

	public String getName() {
		return CVMessages.getString(INSTANCE_CATEGORY_NAME);
	}

	@Override
	public Object[] getChildren() {
		if (!initialized) {
			new GetInstancesCommand(getCloud()).execute();
		}
		return super.getChildren();
	}

	@Override
	protected CloudViewElement[] getElements(Object[] modelElements, int startIndex, int stopIndex) {
		CloudViewElement[] elements = new CloudViewElement[stopIndex - startIndex];
		for (int i = startIndex; i < stopIndex; ++i) {
			elements[i - startIndex] = new CVInstanceElement(modelElements[i]);
		}
		return elements;
	}

	protected void addChildrenFor(Object[] modelElements, int startIndex, int stopIndex) {
		for (int i = startIndex; i < stopIndex; ++i) {
			addChild(new CVInstanceElement(modelElements[i]));
		}
	}

	@Override
	public void listChanged(DeltaCloud cloud, DeltaCloudInstance[] newInstances) {
		try {
			clearChildren();
			final DeltaCloudInstance[] instances = filter();
			addChildren(instances);
		} catch (DeltaCloudException e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					MessageFormat.format("Could not get instanceso from cloud \"{0}\"", cloud.getName()), e,
					getViewer().getControl().getShell());
		} finally {
			initialized = true;
		}
	}

	public DeltaCloudInstance[] filter() throws DeltaCloudException {
		IInstanceFilter f = getCloud().getInstanceFilter();
		return f.filter().toArray(new DeltaCloudInstance[] {});
	}

	@Override
	protected void dispose() {
		DeltaCloud cloud = (DeltaCloud) getElement();
		if (cloud != null) {
			cloud.removeInstanceListListener(this);
		}
	}
}
