/**
 * 
 */
package org.jboss.tools.smooks.gef.tree.editparts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.jboss.tools.smooks.gef.model.AbstractSmooksGraphicalModel;
import org.jboss.tools.smooks.gef.tree.editpolicy.TreeNodeConnectionEditPolicy;
import org.jboss.tools.smooks.gef.tree.editpolicy.TreeNodeEndpointEditPolicy;
import org.jboss.tools.smooks.gef.tree.model.TreeContainerModel;
import org.jboss.tools.smooks.gef.tree.model.TreeNodeConnection;

/**
 * @author DartPeng
 * 
 */
public class TreeNodeConnectionEditPart extends AbstractConnectionEditPart {
	private int alpha = 255;

	@Override
	protected void createEditPolicies() {
		this.installEditPolicy(EditPolicy.CONNECTION_ROLE, new TreeNodeConnectionEditPolicy());
		this.installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new TreeNodeEndpointEditPolicy());
	}

	public void changeLineAlpha(int alpha) {
		this.alpha = alpha;
	}

	public IFigure createFigure() {
		TreeNodeConnection model = (TreeNodeConnection) getModel();
		AbstractSmooksGraphicalModel sourceModel = model.getSourceNode();
		AbstractSmooksGraphicalModel targetModel = model.getTargetNode();
		PolylineConnection connection1 = new PolylineConnection() {
			@Override
			public void paintFigure(Graphics graphics) {
				graphics.setAlpha(alpha);
				super.paintFigure(graphics);
			}
		};
		if (sourceModel instanceof TreeContainerModel || targetModel instanceof TreeContainerModel) {
			// connection1.setConnectionRouter(new ManhattanConnectionRouter());
			connection1.setTargetDecoration(new PolygonDecoration());
			return connection1;
		}
		PolylineConnection connection = new PolylineConnection() {

			@Override
			public void paintFigure(Graphics graphics) {
				graphics.setAlpha(alpha);
				super.paintFigure(graphics);
			}

			public PointList getPoints() {
				PointList list = super.getPoints();
				if(list.size() == 0) return list;
				Point start = getStart();
				Point start2 = new Point(start.x + 20, start.y);
				Point end = getEnd();
				Point end2 = new Point(end.x - 20, end.y);
				list.removeAllPoints();
				list.addPoint(start);
				list.addPoint(start2);
				list.addPoint(end2);
				list.addPoint(end);
				return list;
			}
		};
		connection.setConnectionRouter(new ManhattanConnectionRouter());
		Figure targetFlagFigure = new Figure() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
			 */
			@Override
			public void paint(Graphics graphics) {
				graphics.pushState();
				graphics.setBackgroundColor(ColorConstants.button);
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
				graphics.popState();
			}

		};

		Figure sourceFlagFigure = new Figure() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
			 */
			@Override
			public void paint(Graphics graphics) {
				graphics.pushState();
				graphics.setForegroundColor(ColorConstants.black);
				graphics.setBackgroundColor(ColorConstants.listBackground);
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
				graphics.popState();
				super.paint(graphics);
			}

		};
		targetFlagFigure.setSize(10, 10);
		sourceFlagFigure.setSize(10, 10);
		ConnectionLocator targetLocator = new ConnectionLocator(connection, ConnectionLocator.TARGET);
		connection.add(targetFlagFigure, targetLocator);
		ConnectionLocator sourceLocator = new ConnectionLocator(connection, ConnectionLocator.SOURCE);
		connection.add(sourceFlagFigure, sourceLocator);
		return connection;
	}
}
