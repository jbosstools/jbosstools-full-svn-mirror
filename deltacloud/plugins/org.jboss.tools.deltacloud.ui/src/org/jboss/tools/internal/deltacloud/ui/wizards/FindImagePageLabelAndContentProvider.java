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
package org.jboss.tools.internal.deltacloud.ui.wizards;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudImage;
import org.jboss.tools.deltacloud.core.ICloudElementFilter;
import org.jboss.tools.deltacloud.ui.views.cloudelements.ITableContentAndLabelProvider;

/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class FindImagePageLabelAndContentProvider implements ITableContentAndLabelProvider {

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
	public Object[] getElements(Object input) {
		Assert.isTrue(input instanceof DeltaCloudImage[]);
		return (DeltaCloudImage[]) input;
	}

	@Override
	public void dispose() {
		// do nothing
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing
	}

	@Override
	public void addListener(ILabelProviderListener listener) {
		// do nothing
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// do nothing
	}
}
