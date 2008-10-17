/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.ui.gef.figures;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.jboss.tools.smooks.ui.gef.util.figures.ContainerLayout;

/**
 * @author Dart Peng
 *
 */
public class RightAlignContainerLayout extends ContainerLayout {

	public RightAlignContainerLayout() {
		super();
		this.setAlign(PositionConstants.RIGHT);
		this.setHorizontal(false);
	}

	@Override
	public void layout(IFigure parent) {
		super.layout(parent);
	}

}
