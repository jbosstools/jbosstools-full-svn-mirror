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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;

public class InstanceViewLabelAndContentProvider extends BaseLabelProvider implements IStructuredContentProvider, ITableLabelProvider {

	private DeltaCloud cloud;
	private DeltaCloudInstance[] instances;

	public enum Column {
		NAME(0, 20), 
		ID(1, 20), 
		STATUS(2, 20), 
		HOSTNAME(3, 40);
		
		private int column;
		private int weight;
		private static final Map<Integer,Column> lookup 
		= new HashMap<Integer,Column>();

		static {
			for(Column c : EnumSet.allOf(Column.class))
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
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			if (newInput instanceof DeltaCloudInstance[]) {
				instances = (DeltaCloudInstance[])newInput;
			} else {
				cloud = (DeltaCloud)newInput;
				instances = cloud.getCurrInstances();
			}
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Column c = Column.getColumn(columnIndex);
		DeltaCloudInstance i = (DeltaCloudInstance)element;
		if (i != null) {
			switch (c) {
			case NAME:
				return i.getName();
			case ID:
				return i.getId();
			case STATUS:
				return i.getState();
			case HOSTNAME:
				return i.getHostName();
			}
		}
		return "";
	}

}
