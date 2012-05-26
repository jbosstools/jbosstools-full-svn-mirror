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
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob;
import org.jboss.tools.deltacloud.core.job.AbstractCloudElementJob.CLOUDELEMENT;
import org.jboss.tools.deltacloud.ui.views.Columns;
import org.jboss.tools.deltacloud.ui.views.DeltaCloudImageColumns;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class ImageViewLabelAndContentProvider extends AbstractSelectableCloudViewLabelAndContentProvider<DeltaCloudImage>
		implements ITableContentAndLabelProvider<DeltaCloudImage> {

	@Override
	public Columns<DeltaCloudImage> createColumns() {
		return new DeltaCloudImageColumns();
	}
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	protected ICloudElementFilter<DeltaCloudImage> getCloudFilter(DeltaCloud cloud) {
		return cloud.getImageFilter();
	}

	@Override
	protected void asyncGetCloudElements(final DeltaCloud cloud) {
		if (cloud == null) {
			clearTableViewer();
			return;
		}
		if (isCurrentCloud(cloud)) {
			new AbstractCloudElementJob(
					MessageFormat.format("Get images from cloud {0}", cloud.getName()), cloud, CLOUDELEMENT.IMAGES) {

				@Override
				protected IStatus doRun(IProgressMonitor monitor) throws DeltaCloudException {
					try {
						setCloudElements(cloud.getImages());
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
		if (DeltaCloud.PROP_IMAGES.equals(event.getPropertyName())) {
			DeltaCloud cloud = (DeltaCloud) event.getSource();
			if (isCurrentCloud(cloud)) {
				DeltaCloudImage[] images = (DeltaCloudImage[]) event.getNewValue();
				setCloudElements(images);
			}
		}
	}

	@Override
	public void addPropertyChangeListener(DeltaCloud cloud) {
		cloud.addPropertyChangeListener(DeltaCloud.PROP_IMAGES, this);
	}

	protected DeltaCloudImage[] getCloudElements(DeltaCloud cloud) throws DeltaCloudException {
		return cloud.getImages();
	}
}
