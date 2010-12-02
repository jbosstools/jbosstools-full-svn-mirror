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
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.IImageFilter;
import org.jboss.tools.deltacloud.ui.ErrorUtils;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class ImageViewLabelAndContentProvider extends BaseLabelProvider implements ITableContentAndLabelProvider {

	private DeltaCloud cloud;
	private IImageFilter localFilter;
	private DeltaCloudImage[] images = new DeltaCloudImage[] {};

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
			return new DeltaCloudImage[] {};
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
			if (newInput instanceof DeltaCloud) {
				this.cloud = (DeltaCloud) newInput;
			}
			this.images = filter(cloud, getImages(newInput));
		}
	}

	private DeltaCloudImage[] getImages(Object newInput) {
		DeltaCloudImage[] images = new DeltaCloudImage[] {};
		if (newInput instanceof DeltaCloudImage[]) {
			images = ((DeltaCloudImage[]) newInput);
		} else if (newInput instanceof DeltaCloud) {
			cloud = (DeltaCloud) newInput;
			try {
				return cloud.getImages();
			} catch (Exception e) {
				ErrorUtils.handleError(
						"Error",
						"Could not display images for cloud " + cloud.getName(),
						e, Display.getDefault().getActiveShell());
			}

		}
		return images;
	}

	// Allow override of filter for Finding Images
	public void setFilter(IImageFilter filter) {
		this.localFilter = filter;
	}

	private DeltaCloudImage[] filter(DeltaCloud cloud, DeltaCloudImage[] input) {
		ArrayList<DeltaCloudImage> array = new ArrayList<DeltaCloudImage>();
		IImageFilter f = localFilter;
		if (localFilter == null && cloud != null) {
			f = cloud.getImageFilter();
		}
		for (int i = 0; i < input.length; ++i) {
			DeltaCloudImage image = input[i];
			if (f != null && f.isVisible(image)) {
				array.add(image);
			}
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
