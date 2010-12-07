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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.GetInstancesCommand;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.core.IInstanceListListener;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

/**
 * @author Jeff Jonhston
 * @author Andre Dietisheim
 */
public class InstanceViewLabelAndContentProvider extends
		AbstractCloudElementViewLabelAndContentProvider<DeltaCloudInstance> implements ITableContentAndLabelProvider, IInstanceListListener {

	public enum Column {
		NAME(0, 20),
		ID(1, 20),
		STATUS(2, 6),
		HOSTNAME(3, 40);

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
		Column c = Column.getColumn(columnIndex);
		DeltaCloudInstance i = (DeltaCloudInstance) element;
		switch (c) {
		case STATUS:
			return getStatusIcon(i.getState());
		default:
			return null;
		}
	}

	private Image getStatusIcon(String status) {
		if (DeltaCloudInstance.STOPPED.equals(status)) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_STOPPED);
		} else if (DeltaCloudInstance.RUNNING.equals(status)) {
			return SWTImagesFactory.get(SWTImagesFactory.IMG_RUNNING);
		} else if (DeltaCloudInstance.BOGUS.equals(status)) {
			return PlatformUI.getWorkbench().getSharedImages().
					getImage(ISharedImages.IMG_DEC_FIELD_WARNING);
		} else {
			return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Column c = Column.getColumn(columnIndex);
		DeltaCloudInstance i = (DeltaCloudInstance) element;
		if (i != null) {
			switch (c) {
			case NAME:
				return i.getName();
			case ID:
				return i.getId();
			case STATUS:
				return "";
			case HOSTNAME:
				return i.getHostName();
			}
		}
		return "";
	}

	@Override
	public void dispose() {
	}

	protected ICloudElementFilter<DeltaCloudInstance> getCloudFilter(DeltaCloud cloud) {
		return cloud.getInstanceFilter();
	}

	@Override
	protected void asyncGetCloudElements(DeltaCloud cloud) {
		new GetInstancesCommand(cloud).execute();
	}

	@Override
	protected void addListener(DeltaCloud currentCloud) {
		if (currentCloud != null) {
			currentCloud.addInstanceListListener(this);
		}
	}

	@Override
	protected void removeListener(DeltaCloud currentCloud) {
		if (currentCloud != null) {
			currentCloud.removeInstanceListListener(this);
		}
	}

}
