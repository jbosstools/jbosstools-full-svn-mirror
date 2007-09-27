/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.shale.ui.clay.editor.figures;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.*;

public class FixedConnectionAnchor extends AbstractConnectionAnchor {
//	private Object direction;
	public boolean leftToRight = true;
	public int offsetH;
	public int offsetV;
	public boolean topDown = true;

	public FixedConnectionAnchor(IFigure owner) {
		super(owner);
	}

	/**
	 * @see org.eclipse.draw2d.AbstractConnectionAnchor#ancestorMoved(IFigure)
	 */
	public void ancestorMoved(IFigure figure) {
		if (figure instanceof ScalableFreeformLayeredPane)
			return;
		super.ancestorMoved(figure);
	}

	public Point getLocation(Point reference) {
		Rectangle r = getOwner().getBounds();
		int x, y;
		if (topDown)
			y = r.y + offsetV;
		else
			y = r.bottom() - 1 - offsetV;

		if (leftToRight)
			x = r.x + offsetH;
		else
			x = r.right() - 1 - offsetH;

		Point p = new PrecisionPoint(x, y);
		getOwner().translateToAbsolute(p);
		return p;
	}

	public Point getReferencePoint() {
		return getLocation(null);
	}

}