/**
 * 
 */
package org.jboss.tools.smooks.ui.gef.util;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author Dart
 * 
 */
public class LeftSourceAnchor extends ChopboxAnchor {
	public LeftSourceAnchor(IFigure owner) {
		super(owner);
	}

	public Point getLocation(Point reference) {
		Point p;
		p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		p = getOwner().getBounds().getLeft();
		getOwner().translateToAbsolute(p);
		return p;
	}
}
