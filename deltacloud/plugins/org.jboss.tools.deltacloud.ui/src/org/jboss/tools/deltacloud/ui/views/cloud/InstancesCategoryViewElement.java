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

import java.text.MessageFormat;

import org.eclipse.jface.viewers.TreeViewer;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.GetInstancesCommand;
import org.jboss.tools.deltacloud.core.IInstanceFilter;
import org.jboss.tools.deltacloud.core.IInstanceListListener;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.views.CVMessages;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public class InstancesCategoryViewElement extends CloudElementCategoryViewElement<DeltaCloudInstance> implements IInstanceListListener {

	private static final String INSTANCE_CATEGORY_NAME = "InstanceCategoryName"; //$NON-NLS-1$

	protected InstancesCategoryViewElement(Object model, DeltaCloudViewElement parent, TreeViewer viewer) {
		super(model, parent, viewer);
	}

	public String getName() {
		return CVMessages.getString(INSTANCE_CATEGORY_NAME);
	}

	@Override
	protected void asyncGetCloudElements() {
		setLoadingIndicator();
		new GetInstancesCommand(getCloud()){

			@Override
			protected void asyncGetInstances() throws DeltaCloudException {
				try {
					super.asyncGetInstances();
				} catch(DeltaCloudException e) {
					clearChildren();
					throw e;
				}
			}

		}.execute();
	}

	@Override
	protected DeltaCloudViewElement[] getElements(Object[] modelElements, int startIndex, int stopIndex) {
		DeltaCloudViewElement[] elements = new DeltaCloudViewElement[stopIndex - startIndex];
		for (int i = startIndex; i < stopIndex; ++i) {
			elements[i - startIndex] = new InstanceViewElement(modelElements[i], this, viewer);
		}
		return elements;
	}

	@Override
	public void listChanged(DeltaCloud cloud, DeltaCloudInstance[] newInstances) {
		try {
			onListChanged(cloud, newInstances);
		} catch (DeltaCloudException e) {
			// TODO: internationalize strings
			ErrorUtils.handleError(
					"Error",
					MessageFormat.format("Could not get instances from cloud \"{0}\"", cloud.getName()), e,
					viewer.getControl().getShell());
		}
	}

	@Override
	protected DeltaCloudInstance[] filter(DeltaCloudInstance[] instances) throws DeltaCloudException {
		DeltaCloud cloud = (DeltaCloud) getModel();
		IInstanceFilter f = cloud.getInstanceFilter();
		return f.filter(instances).toArray(new DeltaCloudInstance[instances.length]);
	}

	protected void addCloudElementListener(DeltaCloud cloud) {
		if (cloud != null) {
			cloud.addInstanceListListener(this);
		}
	}

	protected void removeCloudElementListener(DeltaCloud cloud) {
		if (cloud != null) {
			cloud.removeInstanceListListener(this);
		}
	}
}
