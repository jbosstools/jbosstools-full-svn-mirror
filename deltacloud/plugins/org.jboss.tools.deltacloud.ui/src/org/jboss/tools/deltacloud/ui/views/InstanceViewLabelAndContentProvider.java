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
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.IInstanceFilter;
import org.jboss.tools.deltacloud.ui.ErrorUtils;
import org.jboss.tools.deltacloud.ui.SWTImagesFactory;

/**
 * @author Jeff Jonhston
 * @author Andre Dietisheim
 */
public class InstanceViewLabelAndContentProvider extends BaseLabelProvider implements ITableContentAndLabelProvider {

	private DeltaCloud cloud;
	private DeltaCloudInstance[] instances = new DeltaCloudInstance[]{};

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
	public Object[] getElements(Object inputElement) {
		return instances;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			if (newInput instanceof DeltaCloudInstance[]) {
				instances = filter((DeltaCloudInstance[]) newInput);
			} else if (newInput instanceof DeltaCloud) {
				cloud = (DeltaCloud) newInput;
				try {
					instances = filter(cloud.getInstances());
				} catch (Exception e) {
					// TODO internationalize strings
					ErrorUtils.handleError("Error", "Could not display instances for cloud " + cloud.getName(),
							e, Display.getDefault().getActiveShell());
				}
			}
		}
	}

	private DeltaCloudInstance[] filter(DeltaCloudInstance[] input) {
		ArrayList<DeltaCloudInstance> array = new ArrayList<DeltaCloudInstance>();
		IInstanceFilter f = cloud.getInstanceFilter();
		for (int i = 0; i < input.length; ++i) {
			DeltaCloudInstance instance = input[i];
			if (f.isVisible(instance))
				array.add(instance);
		}
		return array.toArray(new DeltaCloudInstance[array.size()]);
	}

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

}
