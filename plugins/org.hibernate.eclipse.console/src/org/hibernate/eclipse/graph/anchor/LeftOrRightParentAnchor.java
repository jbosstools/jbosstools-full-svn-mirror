package org.hibernate.eclipse.graph.anchor;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * Anchor used for figures which are inside a parent which defines the left/right bounds
 */
public class LeftOrRightParentAnchor extends ChopboxAnchor {

	public LeftOrRightParentAnchor(IFigure owner) {
		super(owner);
	}
	public Point getLocation(Point reference) {
		Point p = getOwner().getParent().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		if (reference.x < p.x) {
			p = p.setLocation(getOwner().getParent().getBounds().getLeft().x,p.y);
		} else {
			p = p.setLocation(getOwner().getParent().getBounds().getRight().x,p.y);
		}
		getOwner().translateToAbsolute(p);
		return p;
	}

}
