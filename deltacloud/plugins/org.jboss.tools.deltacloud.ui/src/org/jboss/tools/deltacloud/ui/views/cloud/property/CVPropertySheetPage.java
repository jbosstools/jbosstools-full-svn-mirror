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
package org.jboss.tools.deltacloud.ui.views.cloud.property;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.ui.views.cloud.CloudItem;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;

/**
 * @Jeff Johnston
 * @author André Dietisheim
 */
public class CVPropertySheetPage extends PropertySheetPage {

	private DeltaCloud deltaCloud;
	private PropertyChangeListener cloudPropertyListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			refresh();
		}
	};

	public CVPropertySheetPage() {
		super();
		setSorter(new CVPropertySheetNonSorter());
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		CloudItem cloudItem = WorkbenchUtils.getFirstAdaptedElement(selection, CloudItem.class);
		if (cloudItem != null) {
			removePropertyChangeListener(this.deltaCloud);
			DeltaCloud deltaCloud = cloudItem.getModel();
			if (deltaCloud != null) {
				addPropertyChangeListener(deltaCloud);
				this.deltaCloud = deltaCloud;
			}
		}

		super.selectionChanged(part, selection);
	}

	private void addPropertyChangeListener(DeltaCloud deltaCloud) {
		if (deltaCloud != null) {
			deltaCloud.addPropertyChangeListener(cloudPropertyListener);
		}
	}

	private void removePropertyChangeListener(DeltaCloud deltaCloud) {
		if (deltaCloud != null) {
			deltaCloud.removePropertyChangeListener(cloudPropertyListener);
		}
	}

}
