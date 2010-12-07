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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class CVCloudElementCategoryElement extends CloudViewElement {

	public CVCloudElementCategoryElement(Object element, CloudViewElement parent, TreeViewer viewer) {
		super(element, parent, viewer);
		addCloudElementListener(getCloud());
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Object[] getChildren() {
		if (!initialized.get()) {
			setLoadingIndicator();
			asyncGetCloudElements();
			initialized.set(true);
		}
		return super.getChildren();
	}

	private void setLoadingIndicator() {
		children.add(new LoadingCloudViewElement(this, viewer));		
	}

	protected abstract void asyncGetCloudElements();

	protected void addChildren(Object[] modelElements) {
		if (modelElements.length > CVNumericFoldingElement.FOLDING_SIZE) {
			addFoldedChildren(modelElements);
		} else {
			addChildren(getElements(modelElements, 0, modelElements.length));
		}
	}

	protected void addFoldedChildren(Object[] modelElements) {
		int min = 0;
		int max = CVNumericFoldingElement.FOLDING_SIZE;
		int length = modelElements.length;
		while (length > CVNumericFoldingElement.FOLDING_SIZE) {
			CVNumericFoldingElement f = new CVNumericFoldingElement(min, max, this, viewer);
			addChild(f);
			f.addChildren(getElements(modelElements, min, max));
			min += CVNumericFoldingElement.FOLDING_SIZE;
			max += CVNumericFoldingElement.FOLDING_SIZE;
			length -= CVNumericFoldingElement.FOLDING_SIZE;
		}
		if (length > 0) {
			CVNumericFoldingElement f = new CVNumericFoldingElement(min, max, this, viewer);
			addChild(f);
			f.addChildren(getElements(modelElements, min, min + length));
		}
	}

	protected abstract CloudViewElement[] getElements(Object[] modelElements, int startIndex, int stopIndex);

	@Override
	public IPropertySource getPropertySource() {
		// no property source for cathegories
		return null;
	}

	protected DeltaCloud getCloud() {
		return (DeltaCloud) getElement();
	}

	@Override
	protected void dispose() {
		removeCloudElementListener(getCloud());
	}

	protected abstract void addCloudElementListener(DeltaCloud cloud);

	protected abstract void removeCloudElementListener(DeltaCloud cloud);

}
