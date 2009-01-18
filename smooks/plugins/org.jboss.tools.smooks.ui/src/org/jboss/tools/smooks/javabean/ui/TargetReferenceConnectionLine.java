/**
 * 
 */
package org.jboss.tools.smooks.javabean.ui;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Dart
 * 
 */
public class TargetReferenceConnectionLine extends PolylineConnection {
	protected void outlineShape(Graphics g) {
		try {
			g.setAlpha(100);
			g.setForegroundColor(org.eclipse.draw2d.ColorConstants.red);
			PointList displayPoints = getReferenceLinePoints();
			if (displayPoints != null)
				g.drawPolyline(displayPoints);
		} catch (Exception e) {
			e.printStackTrace();
			super.outlineShape(g);
		}
	}

	private PointList getReferenceLinePoints() {
		PointList list = new PointList();
		Point start = getStart();
		Point end = getEnd();
		
		if(getCenterWidth() <= 0){
			return getPoints();
		}

		int width = getCenterWidth();
		int h = Math.min(width/4, 15);
		
		
		Point startnext = new Point(start.x - 30, start.y + h);

		Point endnext = new Point(end.x - 30, end.y - h);

		start = new Point(start.x - 5,start.y);
		end = new Point(end.x - 5,end.y);
		
		list.addPoint(start);
		list.addPoint(startnext);
		list.addPoint(endnext);
		list.addPoint(end);

		return list;

	}

	protected int getCenterWidth() {
		return Math.abs(getStart().y - getEnd().y);
	}

	public Rectangle getBounds() {
		if (bounds == null) {
			if (this.getCenterWidth() <= 0)
				return super.getBounds();
			bounds = getReferenceLinePoints().getBounds();
			bounds.expand(lineWidth / 2, lineWidth / 2);

			for (int i = 0; i < getChildren().size(); i++) {
				IFigure child = (IFigure) getChildren().get(i);
				bounds.union(child.getBounds());
			}
		}
		return bounds;
	}

	@Override
	public boolean containsPoint(int x, int y) {
		// TODO Auto-generated method stub
		return super.containsPoint(x, y);
	}

}
