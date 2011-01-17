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

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.deltacloud.core.IDeltaCloudElement;
import org.jboss.tools.deltacloud.ui.views.Columns;
import org.jboss.tools.deltacloud.ui.views.Columns.Column;

/**
 * A common superclass for content- and label-providers that operate on
 * IDeltaCloudElements (currently DeltaCloudImage and DeltaCloudInstance)
 * 
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class AbstractCloudElementViewLabelAndContentProvider<CLOUDELEMENT extends IDeltaCloudElement> extends
		BaseLabelProvider implements ITableContentAndLabelProvider<CLOUDELEMENT> {

	private Columns<CLOUDELEMENT> columns;

	@SuppressWarnings("unchecked")
	@Override
	public String getColumnText(Object element, int columnIndex) {
		Columns<CLOUDELEMENT> columns = getColumns();
		Column<CLOUDELEMENT> c = columns.getColumn(columnIndex);
		if (c == null) {
			return null;
		}

		return c.getColumnText((CLOUDELEMENT) element);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Columns<CLOUDELEMENT> columns = getColumns();
		Column<CLOUDELEMENT> c = columns.getColumn(columnIndex);
		if (c == null
				|| c == null) {
			return null;
		}
		return c.getColumnImage((CLOUDELEMENT) element);
	}

	public Columns<CLOUDELEMENT> getColumns() {
		if (columns == null) {
			this.columns = createColumns();
		}
		return columns;
	}

	protected abstract Columns<CLOUDELEMENT> createColumns();
}
