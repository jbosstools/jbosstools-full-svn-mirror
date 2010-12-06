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

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.jboss.tools.deltacloud.core.DeltaCloud;

/**
 * @author Jeff Johnston
 * @author Andre Dietisheim
 */
public abstract class CVCloudElementCategoryElement extends CloudViewElement {

	public CVCloudElementCategoryElement(Object element, TreeViewer viewer) {
		super(element, viewer);
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

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
			CVNumericFoldingElement f = new CVNumericFoldingElement(min, max, getViewer());
			addChild(f);
			f.addChildren(getElements(modelElements, min, max));
			min += CVNumericFoldingElement.FOLDING_SIZE;
			max += CVNumericFoldingElement.FOLDING_SIZE;
			length -= CVNumericFoldingElement.FOLDING_SIZE;
		}
		if (length > 0) {
			CVNumericFoldingElement f = new CVNumericFoldingElement(min, max, getViewer());
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
}
