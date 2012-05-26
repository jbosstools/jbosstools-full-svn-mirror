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
package org.jboss.tools.deltacloud.ui.views.cloudelements;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.jboss.tools.deltacloud.ui.views.Columns;

/**
 * @author Andre Dietisheim
 */
public interface ITableContentAndLabelProvider<MODEL> extends IStructuredContentProvider, ITableLabelProvider{

	public Columns<MODEL> getColumns();
	
}
