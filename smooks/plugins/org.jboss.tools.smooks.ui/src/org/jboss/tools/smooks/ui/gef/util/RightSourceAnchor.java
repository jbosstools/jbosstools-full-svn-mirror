package org.jboss.tools.smooks.ui.gef.util;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * 
 * @author Dart Peng
 * 
 */
public class RightSourceAnchor extends ChopboxAnchor {
	public RightSourceAnchor(IFigure owner) {
		super(owner);
	}

	public Point getLocation(Point reference) {
		Point p;
		p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		p = getOwner().getBounds().getRight();
		getOwner().translateToAbsolute(p);
		return p;
	}
}
