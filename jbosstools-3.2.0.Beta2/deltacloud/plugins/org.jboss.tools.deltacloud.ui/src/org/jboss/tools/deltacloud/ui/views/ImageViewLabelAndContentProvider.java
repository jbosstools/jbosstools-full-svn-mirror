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
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

public class ImageViewLabelAndContentProvider extends BaseLabelProvider implements IStructuredContentProvider,
		ITableLabelProvider {

	private DeltaCloud cloud;
	private IImageFilter localFilter;
	private DeltaCloudImage[] images;

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
	public Object[] getElements(Object inputElement) {
		if (images == null) {
			return new DeltaCloudImage[]{};
		}
		return images;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			if (newInput instanceof DeltaCloudImage[]) {
				images = filter((DeltaCloudImage[]) newInput);
			} else {
				cloud = (DeltaCloud) newInput;
				try {
					images = filter(cloud.getImages());
				} catch (Exception e) {
					ErrorUtils.handleError(
							"Error",
							"Could not get images for cloud " + cloud.getName(),
							e, Display.getDefault().getActiveShell());
				}
			}
		}
	}

	// Allow override of filter for Finding Images
	public void setFilter(IImageFilter filter) {
		this.localFilter = filter;
	}

	private DeltaCloudImage[] filter(DeltaCloudImage[] input) {
		ArrayList<DeltaCloudImage> array = new ArrayList<DeltaCloudImage>();
		IImageFilter f = localFilter;
		if (localFilter == null)
			f = cloud.getImageFilter();
		for (int i = 0; i < input.length; ++i) {
			DeltaCloudImage image = input[i];
			if (f.isVisible(image))
				array.add(image);
		}
		return array.toArray(new DeltaCloudImage[array.size()]);
	}

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

}
