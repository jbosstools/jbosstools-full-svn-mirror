/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.ui.views;

import org.eclipse.swt.graphics.Image;


/**
 * @author Jeff Johnston
 * @author André Dietisheim
 */
public class Columns<MODEL> {

	public abstract static class Column<MODEL> {

		private String name;
		private int weight;

		public Column(String nameKey, int weight) {
			this.name = CVMessages.getString(nameKey);
			this.weight = weight;
		}

		public String getName() {
			return name;
		}

		public int getWeight() {
			return weight;
		}
		
		public abstract String getColumnText(MODEL model);

		public Image getColumnImage(MODEL model) {
			return null;
		}
	}

	private Column<MODEL>[] columns;

	public Columns(Column<MODEL>... columns) {
		this.columns = columns;
	}

	public Column<MODEL>[] getColumns() {
		return columns;
	}

	public Column<MODEL> getColumn(int columnIndex) {
		if (columns == null || columnIndex < 0 || columnIndex >= columns.length) {
			return null;
		}
		return columns[columnIndex];
	}

	public int getSize() {
 		if (columns == null) {
 			return 0;
 		}
		return columns.length;
	}
}