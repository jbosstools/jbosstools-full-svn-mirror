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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;
import org.jboss.tools.deltacloud.core.DeltaCloudException;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class CloudElementCategoryItem<CLOUDELEMENT> extends DeltaCloudViewItem<DeltaCloud>
		implements PropertyChangeListener {

	protected CloudElementCategoryItem(DeltaCloud model, DeltaCloudViewItem<?> parent, TreeViewer viewer) {
		super(model, parent, viewer);
		addPropertyChangeListener(model);
	}

	@Override
	public Object[] getChildren() {
		if (!areChildrenInitialized()) {
			asyncLoadCloudElements();
			setChildrenInitialized(true);
		}
		return super.getChildren();
	}

	protected void setLoadingIndicator() {
		clearChildren();
		addChild(new LoadingItem(this, getViewer()));
	}

	protected abstract void asyncLoadCloudElements();

	protected void addChildren(CLOUDELEMENT[] cloudElements) {
		if (cloudElements.length > NumericFoldingItem.FOLDING_SIZE) {
			addFoldedChildren(cloudElements);
		} else {
			addChildren(getElements(cloudElements, 0, cloudElements.length));
		}
	}

	protected void addFoldedChildren(CLOUDELEMENT[] cloudElements) {
		int min = 0;
		int max = NumericFoldingItem.FOLDING_SIZE;
		int length = cloudElements.length;
		while (length > NumericFoldingItem.FOLDING_SIZE) {
			NumericFoldingItem f = new NumericFoldingItem(min, max, this, getViewer());
			addChild(f);
			f.addChildren(getElements(cloudElements, min, max));
			min += NumericFoldingItem.FOLDING_SIZE;
			max += NumericFoldingItem.FOLDING_SIZE;
			length -= NumericFoldingItem.FOLDING_SIZE;
		}
		if (length > 0) {
			NumericFoldingItem f = new NumericFoldingItem(min, max, this, getViewer());
			addChild(f);
			f.addChildren(getElements(cloudElements, min, min + length));
		}
	}

	protected void replaceCloudElements(DeltaCloud cloud, CLOUDELEMENT[] cloudElements) throws DeltaCloudException {
		clearChildren();
		addChildren(filter(cloudElements));
		setChildrenInitialized(true); // unrequested update
		refresh();
		expand();
	}

	protected abstract CLOUDELEMENT[] filter(CLOUDELEMENT[] cloudElements) throws DeltaCloudException;

	protected abstract DeltaCloudViewItem<?>[] getElements(CLOUDELEMENT[] modelElements, int startIndex, int stopIndex);

	@Override
	public IPropertySource getPropertySource() {
		// no property source for cathegories
		return null;
	}

	protected abstract void addPropertyChangeListener(DeltaCloud cloud);

	protected void removePropertyChangeListener(DeltaCloud cloud) {
		if (cloud != null) {
			cloud.removePropertyChangeListener(this);
		}
	}

	@Override
	public abstract void propertyChange(PropertyChangeEvent evt);

	@Override
	protected void dispose() {
		removePropertyChangeListener(getModel());
	}
}
