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

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author Dart Peng
 *
 */
public class ContainerLeftEdgeAnchor extends ChopboxAnchor {

	public ContainerLeftEdgeAnchor(IFigure owner) {
		super(owner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.draw2d.ChopboxAnchor#getLocation(org.eclipse.draw2d.geometry
	 * .Point)
	 */
	@Override
	public Point getLocation(Point reference) {
		Point p;
		IFigure figure = getOwner();
		IFigure parent = findTheTreeContainerFigure(figure);
		// caculate the right edge of parent figure
		if (parent == null)
			return getOwner().getBounds().getLeft();
		p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		p = getOwner().getBounds().getLeft();
		getOwner().translateToAbsolute(p);
		return new Point(parent.getBounds().x , p.y);
	}

	protected IFigure findTheTreeContainerFigure(IFigure figure) {
		if (figure == null)
			return null;
		IFigure parent = figure.getParent();
		if (parent == null)
			return null;
		if (parent instanceof ITreeContainer) {
			return parent;
		} else {
			return findTheTreeContainerFigure(parent);
		}
	}

}
