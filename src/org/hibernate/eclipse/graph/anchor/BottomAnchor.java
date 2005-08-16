package org.hibernate.eclipse.graph.anchor;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class BottomAnchor extends AbstractConnectionAnchor {

	private int offset;

	BottomAnchor(IFigure source, int offset) {
		super( source );
		this.offset = offset;
	}

	public Point getLocation(Point reference) {
		Rectangle r = getOwner().getBounds().getCopy();
		getOwner().translateToAbsolute( r );
		int off = offset;
		if ( off == -1 )
			off = r.width / 2;
		if ( r.contains( reference ) || r.bottom() > reference.y )
			return r.getTopLeft().translate( off, 0 );
		else
			return r.getBottomLeft().translate( off, -1 );
	}

}
