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
package org.jboss.tools.deltacloud.ui.views.cloudelements;

import java.beans.PropertyChangeEvent;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;
import org.jboss.tools.deltacloud.ui.views.Columns;
import org.jboss.tools.deltacloud.ui.views.DeltaCloudInstanceColumns;

/**
 * @author Jeff Jonhston
 * @author Andre Dietisheim
 */
public class InstanceViewLabelAndContentProvider extends
AbstractSelectableCloudViewLabelAndContentProvider<DeltaCloudInstance> implements ITableContentAndLabelProvider<DeltaCloudInstance> {

	protected ICloudElementFilter<DeltaCloudInstance> getCloudFilter(DeltaCloud cloud) {
		return cloud.getInstanceFilter();
	}

	@Override
	protected void asyncGetCloudElements(final DeltaCloud cloud) {
		if (cloud == null) {
			clearTableViewer();
			return;
		}
		if (isCurrentCloud(cloud)) {
			new AbstractCloudElementJob(
					MessageFormat.format("Get instances from cloud {0}", cloud.getName()), cloud,
					CLOUDELEMENT.INSTANCES) {

				@Override
				protected IStatus doRun(IProgressMonitor monitor) throws DeltaCloudException {
					try {
						setCloudElements(cloud.getInstances());
						return Status.OK_STATUS;
					} catch (DeltaCloudException e) {
						throw e;
					}
				}
			}.schedule();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (DeltaCloud.PROP_INSTANCES.equals(event.getPropertyName())) {
			DeltaCloud cloud = (DeltaCloud) event.getSource();
			if (isCurrentCloud(cloud)) {
				DeltaCloudInstance[] instances = (DeltaCloudInstance[]) event.getNewValue();
				setCloudElements(instances);
			}
		}
	}

	@Override
	public void addPropertyChangeListener(DeltaCloud currentCloud) {
		if (currentCloud != null) {
			currentCloud.addPropertyChangeListener(DeltaCloud.PROP_INSTANCES, this);
		}
	}

	@Override
	protected Columns<DeltaCloudInstance> createColumns() {
		return new DeltaCloudInstanceColumns();
	}
}
