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
package org.jboss.tools.deltacloud.ui.views.cloud;

import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class CloudElementCategoryItem<CLOUDELEMENT> extends DeltaCloudViewItem<DeltaCloud> implements
		PropertyChangeListener {

	protected CloudElementCategoryItem(DeltaCloud model, DeltaCloudViewItem<?> parent, TreeViewer viewer) {
		super(model, parent, viewer);
		addPropertyChangeListener(model);
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Object[] getChildren() {
		if (!initialized.get()) {
			asyncAddCloudElements();
		}
		return super.getChildren();
	}

	protected void setLoadingIndicator() {
		children.add(new LoadingItem(this, viewer));
	}

	protected abstract void asyncAddCloudElements();

	protected void addChildren(CLOUDELEMENT[] modelElements) {
		if (modelElements.length > NumericFoldingItem.FOLDING_SIZE) {
			addFoldedChildren(modelElements);
		} else {
			addChildren(getElements(modelElements, 0, modelElements.length));
		}
	}

	protected void addFoldedChildren(CLOUDELEMENT[] modelElements) {
		int min = 0;
		int max = NumericFoldingItem.FOLDING_SIZE;
		int length = modelElements.length;
		while (length > NumericFoldingItem.FOLDING_SIZE) {
			NumericFoldingItem f = new NumericFoldingItem(min, max, this, viewer);
			addChild(f);
			f.addChildren(getElements(modelElements, min, max));
			min += NumericFoldingItem.FOLDING_SIZE;
			max += NumericFoldingItem.FOLDING_SIZE;
			length -= NumericFoldingItem.FOLDING_SIZE;
		}
		if (length > 0) {
			NumericFoldingItem f = new NumericFoldingItem(min, max, this, viewer);
			addChild(f);
			f.addChildren(getElements(modelElements, min, min + length));
		}
	}

	protected void onCloudElementsChanged(DeltaCloud cloud, CLOUDELEMENT[] cloudElements) throws DeltaCloudException {
		try {
			clearChildren();
			initialized.set(false);
			final CLOUDELEMENT[] filteredElements = filter(cloudElements);
			addChildren(filteredElements);
			expand();
		} finally {
			initialized.set(true);
		}
	}

	protected abstract CLOUDELEMENT[] filter(CLOUDELEMENT[] cloudElements) throws DeltaCloudException;

	protected abstract DeltaCloudViewItem<?>[] getElements(CLOUDELEMENT[] modelElements, int startIndex, int stopIndex);

	@Override
	public IPropertySource getPropertySource() {
		// no property source for cathegories
		return null;
	}

	protected abstract void addPropertyChangeListener(DeltaCloud cloud);
}
