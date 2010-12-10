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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.core.job.AbstractCloudJob;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class ImageViewLabelAndContentProvider extends AbstractCloudElementViewLabelAndContentProvider<DeltaCloudImage>
		implements ITableContentAndLabelProvider {

	public enum Column {
		NAME(0, 20),
		ID(1, 20),
		ARCH(2, 20),
		DESC(3, 40);

		private int column;
		private int weight;
		private static final Map<Integer, Column> lookup = new HashMap<Integer, Column>();

		static {
			for (Column c : EnumSet.allOf(Column.class))
				lookup.put(c.getColumnNumber(), c);
		}

		private Column(int column, int weight) {
			this.column = column;
			this.weight = weight;
		}

		public int getColumnNumber() {
			return column;
		}

		public int getWeight() {
			return weight;
		}

		public static Column getColumn(int number) {
			return lookup.get(number);
		}

		public static int getSize() {
			return lookup.size();
		}

	};

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Column c = Column.getColumn(columnIndex);
		DeltaCloudImage i = (DeltaCloudImage) element;
		switch (c) {
		case NAME:
			return i.getName();
		case ID:
			return i.getId();
		case ARCH:
			return i.getArchitecture();
		case DESC:
			return i.getDescription();
		}
		return "";
	}

	protected ICloudElementFilter<DeltaCloudImage> getCloudFilter(DeltaCloud cloud) {
		return cloud.getImageFilter();
	}

	@Override
	protected void asyncAddCloudElements(final DeltaCloud cloud) {
		new AbstractCloudJob(
				MessageFormat.format("Get images from cloud {0}", cloud.getName()), cloud) {

			@Override
			protected IStatus doRun(IProgressMonitor monitor) throws DeltaCloudException {
				try {
					addToViewer(cloud.getImages());;
					return Status.OK_STATUS;
				} catch(DeltaCloudException e) {
					throw e;
				}
			}
		}.schedule();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if (DeltaCloud.PROP_IMAGES.equals(event.getPropertyName())) {
			DeltaCloud cloud = (DeltaCloud) event.getSource();
			DeltaCloudImage[] images = (DeltaCloudImage[]) event.getNewValue();
			updateCloudElements(images, cloud);
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
