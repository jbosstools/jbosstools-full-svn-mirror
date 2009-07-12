package org.jboss.tools.smooks.gef.tree.figures;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

public class LeftOrRightAnchor extends ChopboxAnchor {
	
	private boolean isLeft = false;
	
	public LeftOrRightAnchor(IFigure owner , boolean isLeft) {
		super(owner);
		this.isLeft = isLeft;
	}
	
	public Point getReferencePoint(){
		Point point = super.getReferencePoint();
		if(isLeft){
			return new Point(point.x + 30,point.y);
		}else{
			return new Point(point.x - 30,point.y);
		}
	}
	
	public Point getLocation(Point reference) {
		Point p;
		IFigure hostFigure = getOwner();
		IFigure parent = hostFigure.getParent();
		while(parent != null && !(parent instanceof TreeContainerFigure)){
			if(parent == null || parent instanceof TreeContainerFigure){
				break;
			}
			parent = parent.getParent();
		}
		p = getOwner().getBounds().getCenter();
		getOwner().translateToAbsolute(p);
		if (isLeft){
			p = getOwner().getBounds().getLeft();
			p.x = parent.getBounds().getLeft().x - 2;
		}
		else{
			p = getOwner().getBounds().getRight();
			p.x = parent.getBounds().getRight().x + 2;
		}
		getOwner().translateToAbsolute(p);
		return p;
	}
}

