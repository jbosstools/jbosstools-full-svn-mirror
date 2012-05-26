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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Table;

/**
 * A viewer comparator that compares labels in a column of a table.<br>
 * This implementation was extracted out of the former Image- and
 * InstanceComparator which were mostly identical.
 * 
 * @see ViewerComparator
 * 
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class TableViewerColumnComparator extends ViewerComparator {

	private final static int UP = 1;

	private int column;
	private int direction;

	public TableViewerColumnComparator() {
		this(0);
	}

	public TableViewerColumnComparator(int column) {
		this.column = column;
		this.direction = UP;
	}

	public void setColumn(int newColumn) {
		if (column != newColumn)
			direction = UP;
		column = newColumn;
	}

	public int getColumn() {
		return column;
	}

	public void switchDirection() {
		if (direction == -1) {
			direction = 0;
		} else if (direction == 1) {
			direction = -1;
		} else if (direction == 0) {
			direction = 1;
		}
	}

	public void setSortIndicator(TableViewer tableViewer) {
		Table table = tableViewer.getTable();
		switch(direction) {
		case -1:
			table.setSortDirection(SWT.UP);
			break;
		case 0:
			table.setSortDirection(SWT.NONE);
			break;
		case 1:
			table.setSortDirection(SWT.DOWN);
			break;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int tmp = compareByColumn(viewer, e1, e2);
		return tmp * direction;
	}

	private int compareByColumn(Viewer viewer, Object e1, Object e2) {
		Assert.isTrue(viewer instanceof TableViewer
				&& ((TableViewer) viewer).getLabelProvider() instanceof ITableLabelProvider);

		ITableLabelProvider provider = (ITableLabelProvider) ((TableViewer) viewer).getLabelProvider();
		String s1 = provider.getColumnText(e1, column);
		String s2 = provider.getColumnText(e2, column);
		return s1.compareToIgnoreCase(s2);
	}
}
