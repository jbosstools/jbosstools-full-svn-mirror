/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.editparts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.jboss.tools.smooks.gef.tree.editpolicy.TreeNodeConnectionEditPolicy;
import org.jboss.tools.smooks.gef.tree.editpolicy.TreeNodeEndpointEditPolicy;
import org.jboss.tools.smooks.gef.tree.figures.LeftOrRightAnchor;

/**
 * @author DartPeng
 * 
 */
public class TreeNodeConnectionEditPart extends AbstractConnectionEditPart {
	protected int alpha = 255;

	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.CONNECTION_ROLE, new TreeNodeConnectionEditPolicy());
		this.installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new TreeNodeEndpointEditPolicy());
	}

	public void changeLineAlpha(int alpha) {
		this.alpha = alpha;
	}
	
	protected IFigure createSourceFlagFigure(){
		Figure sourceFlagFigure = new Figure() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
			 */
			@Override
			public void paint(Graphics graphics) {
				graphics.pushState();
				ConnectionAnchor sourceAnchor = getSourceConnectionAnchor();
				ConnectionAnchor targetAnchor = getTargetConnectionAnchor();
				boolean startLeft = false;
				if (sourceAnchor instanceof LeftOrRightAnchor) {
					((LeftOrRightAnchor) sourceAnchor).getLocation(targetAnchor.getReferencePoint());
					startLeft = ((LeftOrRightAnchor) sourceAnchor).isLeft();
				}
				graphics.setForegroundColor(ColorConstants.black);
				graphics.setBackgroundColor(ColorConstants.listBackground);
				if (!startLeft) {
					Point p = this.getBounds().getTopLeft();
					Point p2 = this.getBounds().getBottomLeft();
					Point p3 = this.getBounds().getTopRight();
					p3 = new Point(p3.x, p3.y + this.getSize().height / 2);
					PointList pointList = new PointList();
					pointList.addPoint(p);
					pointList.addPoint(p2.x, p2.y - 1);
					pointList.addPoint(p3);
					graphics.fillPolygon(pointList);
					graphics.drawPolygon(pointList);
				} else {
					Point p = this.getBounds().getTopRight();
					Point p2 = this.getBounds().getBottomRight();
					Point p3 = this.getBounds().getTopLeft();
					p3 = new Point(p3.x, p3.y + this.getSize().height / 2);
					PointList pointList = new PointList();
					pointList.addPoint(p.x - 1, p.y);
					pointList.addPoint(p2.x - 1, p2.y - 1);
					pointList.addPoint(p3);
					graphics.fillPolygon(pointList);
					graphics.drawPolygon(pointList);
				}
				graphics.popState();
				super.paint(graphics);
			}

		};
		return sourceFlagFigure;
	}
	
	protected IFigure createTargetFlagFigure(){
		Figure targetFlagFigure = new Figure() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
			 */
			@Override
			public void paint(Graphics graphics) {

				ConnectionAnchor sourceAnchor = getSourceConnectionAnchor();
				ConnectionAnchor targetAnchor = getTargetConnectionAnchor();
				// boolean startLeft = false;
				// if(sourceAnchor instanceof LeftOrRightAnchor){
				// ((LeftOrRightAnchor)sourceAnchor).getLocation(targetAnchor.getReferencePoint());
				// startLeft = ((LeftOrRightAnchor)sourceAnchor).isLeft();
				// }
				boolean targetLeft = false;
				if (targetAnchor instanceof LeftOrRightAnchor) {
					((LeftOrRightAnchor) targetAnchor).getLocation(sourceAnchor.getReferencePoint());
					targetLeft = ((LeftOrRightAnchor) targetAnchor).isLeft();
				}

				graphics.pushState();
				graphics.setBackgroundColor(ColorConstants.button);
				if (targetLeft) {
					Point p = this.getBounds().getTopLeft();
					Point p2 = this.getBounds().getBottomLeft();
					Point p3 = this.getBounds().getTopRight();
					p3 = new Point(p3.x, p3.y + this.getSize().height / 2);
					PointList pointList = new PointList();
					pointList.addPoint(p);
					pointList.addPoint(p2.x, p2.y - 1);
					pointList.addPoint(p3);
					graphics.fillPolygon(pointList);
					graphics.drawPolygon(pointList);
				} else {
					Point p = this.getBounds().getTopRight();
					Point p2 = this.getBounds().getBottomRight();
					Point p3 = this.getBounds().getTopLeft();
					p3 = new Point(p3.x, p3.y + this.getSize().height / 2);
					PointList pointList = new PointList();
					pointList.addPoint(p.x - 1, p.y);
					pointList.addPoint(p2.x - 1, p2.y - 1);
					pointList.addPoint(p3);
					graphics.fillPolygon(pointList);
					graphics.drawPolygon(pointList);
				}
				graphics.popState();
			}

		};
		return targetFlagFigure;
	}
	
	protected Connection createConnectionFigure(){
		PolylineConnection connection = new PolylineConnection() {

			@Override
			public void paintFigure(Graphics graphics) {
				graphics.setAlpha(alpha);
				graphics.setLineWidth(3);
				super.paintFigure(graphics);
			}

			public PointList getPoints() {
				ConnectionAnchor sourceAnchor = getSourceConnectionAnchor();
				ConnectionAnchor targetAnchor = getTargetConnectionAnchor();
				boolean startLeft = false;
				if (sourceAnchor instanceof LeftOrRightAnchor) {
					((LeftOrRightAnchor) sourceAnchor).getLocation(targetAnchor.getReferencePoint());
					startLeft = ((LeftOrRightAnchor) sourceAnchor).isLeft();
				}

				boolean targetLeft = false;
				if (targetAnchor instanceof LeftOrRightAnchor) {
					((LeftOrRightAnchor) targetAnchor).getLocation(sourceAnchor.getReferencePoint());
					targetLeft = ((LeftOrRightAnchor) targetAnchor).isLeft();
				}

				PointList list = super.getPoints();
				if (list.size() == 0)
					return list;
				Point start = getStart();
				int slength = 20;
				int tlength = 20;
				if (startLeft) {
					slength = (-slength);
				}
				if (targetLeft) {
					tlength = (-tlength);
				}

				Point start2 = new Point(start.x + slength, start.y);
				Point end = getEnd();
				Point end2 = new Point(end.x + tlength, end.y);
				list.removeAllPoints();
				list.addPoint(start);
				list.addPoint(start2);
				list.addPoint(end2);
				list.addPoint(end);
				return list;
			}
		};
		connection.setConnectionRouter(new ManhattanConnectionRouter());
		return connection;
	}

	public IFigure createFigure() {
		Connection connection = createConnectionFigure();
		connection.setConnectionRouter(new ManhattanConnectionRouter());
		IFigure targetFlagFigure = createTargetFlagFigure();
		IFigure sourceFlagFigure = createSourceFlagFigure();
		targetFlagFigure.setSize(7, 7);
		sourceFlagFigure.setSize(7, 7);
		ConnectionLocator targetLocator = new ConnectionLocator(connection, ConnectionLocator.TARGET);
		connection.add(targetFlagFigure, targetLocator);
		ConnectionLocator sourceLocator = new ConnectionLocator(connection, ConnectionLocator.SOURCE);
		connection.add(sourceFlagFigure, sourceLocator);
		return connection;
	}
}
